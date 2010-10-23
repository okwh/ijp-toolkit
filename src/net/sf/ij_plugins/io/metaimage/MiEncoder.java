/*
 * Image/J Plugins
 * Copyright (C) 2002-2010 Jarek Sacha
 * Author's email: jsacha at users dot sourceforge dot net
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Latest release available at http://sourceforge.net/projects/ij-plugins/
 */
package net.sf.ij_plugins.io.metaimage;

import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.io.FileInfo;
import ij.io.ImageWriter;
import ij.io.SaveDialog;
import ij.measure.Calibration;
import ij.plugin.PlugIn;
import net.sf.ij_plugins.util.Validate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;


/**
 * Encode image (including stacks) in MetaImage format. MetaImage is one of the formats supported by
 * ITK (http://www.itk.org). More information about MetaImage, including C++ code, can be found
 * http://caddlab.rad.unc.edu/technologies/MetaImage/ . This implementation is intended to be
 * compatible with ITK version of MetaImage.
 *
 * @author Jarek Sacha
 * @since June 18, 2002
 */
public class MiEncoder implements PlugIn {
    // TODO: Fix MetaImage format for unambiguous element size, e.g. MET_LONG could be 32 bit on 32 bit processors and 64 bit on 64 bit processors. A fix would be to use MET_INT32.
    // TODO: Fix MetaImage tag names to distinguish file side and memory side representation, and real word coordinates. For instance ElementType refers to how a pixel value in represented in file, ElementSize refers to size of the of the volume represented by a single pixel in real word coordinates. Naming convention seams to be confusing.

    private static final String DIALOG_CAPTION = "MetaImage Writer";
    private static final String[] HEADER_EXTENSIONS = {".mha", ".mhd"};
    private static final String RAW_DATA_EXTENSION = ".raw";
    private static final String ASSIGNMENT_SEPARATOR = " = ";
    private static final String LINE_SEPARATOR = "\n";


    /**
     * Constructor for the MiEncoder object
     */
    public MiEncoder() {
    }


    /**
     * Write image in MetaImage format. Info header and raw image data are stored in separate
     * files.
     *
     * @param imp          Image to save.
     * @param fileRootName Root file name for image files. Header will have extension ".mha", raw
     *                     image data will have extension ".raw".
     * @throws MiException In case of error when saving the image.
     */
    public static void write(final ImagePlus imp, final String fileRootName) throws MiException {
        Validate.argumentNotNull(imp, "imp");
        Validate.argumentNotNull(fileRootName, "fileRootName");

        //
        // Figure out file names.
        //
        String headerName = null;
        String rawDataName = null;
        // Check if header extension is already present.
        for (final String HEADER_EXTENSION : HEADER_EXTENSIONS) {
            if (fileRootName.endsWith(HEADER_EXTENSION)) {
                headerName = fileRootName;
                final String nameRoot = fileRootName.substring(
                        0, fileRootName.length() - HEADER_EXTENSION.length());
                rawDataName = nameRoot + RAW_DATA_EXTENSION;
                break;
            }
        }
        // If not present then use default extension.
        if (headerName == null) {
            headerName = fileRootName + HEADER_EXTENSIONS[0];
            rawDataName = fileRootName + RAW_DATA_EXTENSION;
        }

        // Remove path from rawDataName
        String rawDataNameShort = rawDataName;
        final int lastSeparatorIndex = rawDataName.lastIndexOf(File.separator);
        if (lastSeparatorIndex >= 0) {
            rawDataNameShort = rawDataName.substring(lastSeparatorIndex + 1);
        }

        //
        // Create MetaImage files
        //
        writeHeader(imp, headerName, rawDataNameShort);
        writeRawImage(imp, rawDataName);
    }


    /**
     * Write only MetaImage header.
     *
     * @param imp             Image for which to write the header.
     * @param headerFileName  Name of the file to write header to.
     * @param rawDataFileName Name of the file where raw image data is saved.
     * @throws MiException In case of error when saving the header.
     */
    private static void writeHeader(final ImagePlus imp, final String headerFileName,
                                    final String rawDataFileName) throws MiException {

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(headerFileName);
            fileWriter.write(createHeaderText(imp, rawDataFileName));
        } catch (final IOException ex) {
            throw new MiException("Error writing to header file '" + headerFileName + "'.\n" + ex.getMessage());
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (final IOException ex) {
                    //
                }
            }
        }
    }


    /**
     * Create content of a MetaImage header as string.
     *
     * @param imp             Image for which to write the header.
     * @param rawDataFileName Name of the file where raw image data is saved.
     * @return String containing MetaImage header.
     * @throws MiException In case of error when saving the header.
     */
    private static String createHeaderText(final ImagePlus imp, final String rawDataFileName)
            throws MiException {
        final StringBuffer header = new StringBuffer();

        // NDims
        final String nDims = imp.getStackSize() > 1 ? "3" : "2";
        header.append(MiTag.N_DIMS).append(ASSIGNMENT_SEPARATOR).append(nDims).append(LINE_SEPARATOR);

        // DimSize
        header.append(MiTag.DIM_SIZE).append(ASSIGNMENT_SEPARATOR).append(imp.getWidth()).append(" ").append(imp.getHeight());
        if (imp.getStackSize() > 1) {
            header.append(" ").append(imp.getStackSize());
        }
        header.append(LINE_SEPARATOR);

        // ElementType
        final String elementType;
        switch (imp.getType()) {
            case ImagePlus.COLOR_256:
            case ImagePlus.COLOR_RGB:
                throw new MiException("COLOR_256 and COLOR_RGB images are not supported.");
            case ImagePlus.GRAY8:
                elementType = MiElementType.MET_UCHAR.toString();
                break;
            case ImagePlus.GRAY16:
                elementType = imp.getFileInfo().fileType == FileInfo.GRAY16_SIGNED
                        ? MiElementType.MET_SHORT.toString()
                        : MiElementType.MET_USHORT.toString();
                break;
            case ImagePlus.GRAY32:
                elementType = imp.getFileInfo().fileType == FileInfo.GRAY32_INT
                        ? MiElementType.MET_UINT.toString()
                        : MiElementType.MET_FLOAT.toString();
                break;
            default:
                throw new MiException("Unrecognized ImagePlus type id: " + imp.getType());
        }
        header.append(MiTag.ELEMENT_TYPE).append(ASSIGNMENT_SEPARATOR).append(elementType).append(LINE_SEPARATOR);

        // ElementByteOrderMSB
        // JVM always uses MSB, independent of the underlying hardware platform.
        header.append(MiTag.ELEMENT_BYTE_ORDER_MSB).append(ASSIGNMENT_SEPARATOR).append(MiBoolean.TRUE).append(LINE_SEPARATOR);

        // ElementSize
        // ElementSpacing
        String elementSize = null;
        final Calibration cal = imp.getCalibration();
        if (cal != null && cal.pixelWidth > 0 && cal.pixelHeight > 0) {
            elementSize = "" + cal.pixelWidth + " " + cal.pixelHeight;
            if (imp.getStackSize() > 1) {
                if (cal.pixelDepth > 0) {
                    elementSize += " " + cal.pixelDepth;
                } else {
                    elementSize = null;
                }
            }
        }
        if (elementSize != null) {
            header.append(MiTag.ELEMENT_SIZE).append(ASSIGNMENT_SEPARATOR).append(elementSize).append(LINE_SEPARATOR);
            header.append(MiTag.ELEMENT_SPACING).append(ASSIGNMENT_SEPARATOR).append(elementSize).append(LINE_SEPARATOR);
        }

        // ElementDataFile, this should always be last.
        header.append(MiTag.ELEMENT_DATA_FILE).append(ASSIGNMENT_SEPARATOR).append(rawDataFileName).append(LINE_SEPARATOR);

        return header.toString();
    }


    /**
     * Save only the raw image data.
     *
     * @param imp      Image to be saved.
     * @param fileName Raw data file name.
     * @throws MiException In case of error when saving the raw data.
     */
    private static void writeRawImage(final ImagePlus imp, final String fileName) throws MiException {
        final File file = new File(fileName);
        final FileInfo fileInfo = imp.getFileInfo();
        fileInfo.directory = file.getParent();
        fileInfo.fileName = file.getName();
        fileInfo.fileFormat = FileInfo.RAW;

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(fileName);
            final ImageWriter imageWriter = new ImageWriter(fileInfo);
            imageWriter.write(fileOutputStream);
        } catch (final IOException ex) {
            throw new MiException("Error writing to raw image file '" + fileName + "'.\n" + ex.getMessage());
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (final IOException ex) {
                    IJ.log("Failed to close output stream. " + ex.getMessage());
                }
            }
        }
    }


    /**
     * Main processing method for the MiEncoder object, required by PlugIn interface.
     *
     * @param parm1 Not used.
     */
    @Override
    public void run(final String parm1) {
        // Get current image
        final ImagePlus imp = WindowManager.getCurrentImage();
        if (imp == null) {
            IJ.noImage();
            return;
        }

        // Get file name
        final SaveDialog saveDialog = new SaveDialog(DIALOG_CAPTION,
                imp.getTitle(), HEADER_EXTENSIONS[0]);
        if (saveDialog.getFileName() == null) {
            return;
        }

        final File file = new File(saveDialog.getDirectory(), saveDialog.getFileName());
        try {
            write(imp, file.getAbsolutePath());
        } catch (final MiException ex) {
            IJ.showMessage(DIALOG_CAPTION, ex.getMessage());
            return;
        }

        IJ.showStatus("MetaImage " + saveDialog.getFileName() + " saved.");
    }
}
