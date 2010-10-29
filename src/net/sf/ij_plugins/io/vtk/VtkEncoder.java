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
package net.sf.ij_plugins.io.vtk;

import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.io.ImageWriter;
import ij.io.SaveDialog;
import ij.measure.Calibration;
import ij.plugin.PlugIn;

import java.io.*;


/**
 * Save image in <a HREF="http://public.kitware.com/VTK/">VTK</a> format. Supported image types:
 * GRAY8, GRAY16, GRAY32.
 *
 * @author Jarek Sacha
 */

public class VtkEncoder implements PlugIn {

    private static final String DIALOG_CAPTION = "VTK Writer";
    private static final String TAG_SEPARATOR = " ";
    private static final String vtkFileVersion = "3.0";


    private static String createHeader(final ImagePlus imp, final boolean asciiFormat) {

        final StringBuffer header = new StringBuffer();
        header.append(VtkTag.DATA_FILE_VERSION).append(vtkFileVersion).append("\n");

        header.append(imp.getTitle()).append("\n");

        header.append(asciiFormat ? VtkDataFormat.ASCII : VtkDataFormat.BINARY);
        header.append("\n");

        header.append(VtkTag.DATASET).append(TAG_SEPARATOR).append(VtkDataSetType.STRUCTURED_POINTS).append("\n");

        final int width = imp.getWidth();
        final int height = imp.getHeight();
        final int depth = imp.getStackSize();
        header.append(VtkTag.DIMENSIONS).append(TAG_SEPARATOR).append(width).append(" ")
                .append(height).append(" ").append(depth).append("\n");

        final Calibration c = imp.getCalibration();
        header.append(VtkTag.SPACING).append(TAG_SEPARATOR).append(c.pixelWidth).append(" ")
                .append(c.pixelHeight).append(" ").append(c.pixelDepth).append("\n");

        header.append(VtkTag.ORIGIN).append(TAG_SEPARATOR).append(c.xOrigin).append(" ")
                .append(c.yOrigin).append(" ").append(c.zOrigin).append("\n");

        header.append(VtkTag.POINT_DATA).append(TAG_SEPARATOR).append(width * height * depth)
                .append("\n");

        final String scalarName;
        switch (imp.getType()) {
            case ImagePlus.GRAY8:
                scalarName = VtkScalarType.UNSIGNED_CHAR.toString();
                break;
            case ImagePlus.GRAY16:
                scalarName = VtkScalarType.UNSIGNED_SHORT.toString();
                break;
            case ImagePlus.GRAY32:
                scalarName = VtkScalarType.FLOAT.toString();
                break;
            // BEG KEESH RGB UPDATE
            case ImagePlus.COLOR_RGB:
                scalarName = VtkScalarType.UNSIGNED_CHAR.toString();
                break;
            // END KEESH RGB UPDATE
            default:
                throw new IllegalArgumentException("Unsupported image type. "
                        + "Only images of types: GRAY8, GRAY16, and GRAY32 are supported.");
        }
        // BEG KEESH RGB UPDATE
        if (imp.getType() != ImagePlus.COLOR_RGB) {
            header.append(VtkTag.SCALARS).append(TAG_SEPARATOR).append("volume_scalars ")
                    .append(scalarName).append(" 1\n");
        } else {
            header.append(VtkTag.COLOR_SCALARS).append(TAG_SEPARATOR).append("volume_scalars ")
                    .append("3\n");  // no scalar name as per VTK spec
        }
        // Writing LUT tag for RGB images incorrectly offsets image data
        if (imp.getType() != ImagePlus.COLOR_RGB) {
            header.append(VtkTag.LOOKUP_TABLE).append(TAG_SEPARATOR).append("default\n");
        }
        // END KEESH RGB UPDATE

        return header.toString();
    }


    private static void saveAsVtkBinary(final String fileName, final ImagePlus imp)
            throws FileNotFoundException, IOException {

        final BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileName));
        try {
            final String header = createHeader(imp, false);
            bos.write(header.getBytes());

            final ImageWriter imageWriter = new ImageWriter(imp.getFileInfo());
            imageWriter.write(bos);
        } finally {
            bos.close();
        }
    }


    public static void save(final String fileName, final ImagePlus imp) throws IOException {
        saveAsVtkBinary(fileName, imp);
    }


    /**
     * Save image using ASCII variant of the VTK format.
     *
     * @param fileName File name.
     * @param imp      Image to be saved.
     * @throws IOException In case of I/O errors.
     */
    private static void saveAsVtkAscii(final String fileName, final ImagePlus imp)
            throws IOException {

        final int oldSlice = imp.getCurrentSlice();

        final BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        try {
            IJ.showProgress(0);

            final String header = createHeader(imp, true);
            writer.write(header);

            final int width = imp.getWidth();
            final int height = imp.getHeight();
            final int depth = imp.getStackSize();
            final int sliceSize = width * height;
            final Object[] pixels = imp.getStack().getImageArray();
            for (int z = 0; z < depth; ++z) {
                writeArray(pixels[z], sliceSize, writer, width);
                IJ.showProgress((double) z / (double) depth);
            }
        } finally {
            writer.close();
        }
        IJ.showProgress(1);

        imp.setSlice(oldSlice);
    }


    private static void writeArray(final Object a, final int length, final Writer writer, final int lineSize)
            throws IOException {

        if (a instanceof byte[]) {
            writeArray((byte[]) a, length, writer, lineSize);
        } else if (a instanceof short[]) {
            writeArray((short[]) a, length, writer, lineSize);
        } else if (a instanceof float[]) {
            writeArray((float[]) a, length, writer, lineSize);
            // BEG KEESH RGB UPDATE
        } else if (a instanceof int[]) {
            writeArray((int[]) a, length, writer, lineSize);
            // END KEESH RGB UPDATE
        } else {
            throw new IllegalArgumentException("Unsupported array type: " + a);
        }
    }


    private static void writeArray(final byte[] a, final int length, final Writer writer, final int lineSize)
            throws IOException {

        final StringBuffer buf = new StringBuffer();
        for (int i = 0; i < length; ++i) {
            buf.append(a[i] & 0xff);
            if (i > 0 && (i % lineSize) == 0) {
                buf.append('\n');
                writer.write(buf.toString());
                buf.delete(0, buf.length());
            } else {
                buf.append(' ');
            }
        }
        writer.write(buf.toString());
    }


    private static void writeArray(final short[] a, final int length, final Writer writer, final int lineSize)
            throws IOException {

        int c = 0;
        for (int i = 0; i < length; ++i) {
            writer.write("" + (a[i] & 0xffff));
            c++;
            writer.write((c % lineSize) == 0 ? "\n" : " ");
        }
    }


    private static void writeArray(final float[] a, final int length, final Writer writer, final int lineSize)
            throws IOException {

        int c = 0;
        for (int i = 0; i < length; ++i) {
            writer.write("" + a[i]);
            c++;
            writer.write((c % lineSize) == 0 ? "\n" : " ");
        }
    }

    // BEG KEESH RGB UPDATE


    private static void writeArray(final int[] a, final int length, final Writer writer, final int lineSize)
            throws IOException {

        int c = 0;
        for (int i = 0; i < length; ++i) {
            // Need to convert to [0,1] for VTK
            final int val = (a[i] & 0xffffffff);  // extract 32-bit integer
            final int r = (val & 0xff0000) >> 16;  // extract red
            final int g = (val & 0xff00) >> 8;     // extract green
            final int b = val & 0xff;            // extract blue
            final float fr = r / 255.0f;  // normalize to [0,1]
            final float fg = g / 255.0f;
            final float fb = b / 255.0f;
            writer.write("" + fr + " " + fg + " " + fb);
            c++;
            writer.write((c % lineSize) == 0 ? "\n" : " ");
        }
    }
    // END KEESH RGB UPDATE


    /**
     * Main processing method for the VtkEncoder plugin
     *
     * @param arg If equal "ASCII" file will be saved in text format otherwise in binary format
     *            (MSB).
     */
    @Override
    public void run(final String arg) {

        final ImagePlus imp = WindowManager.getCurrentImage();
        if (imp == null) {
            IJ.showMessage(DIALOG_CAPTION, "No image to save.");
            return;
        }

        final SaveDialog saveDialog = new SaveDialog("Save as VTK", imp.getTitle(), ".vtk");

        if (saveDialog.getFileName() == null) {
            return;
        }

        IJ.showStatus("Saving current image as '" + saveDialog.getFileName() + "'...");
        final String fileName = saveDialog.getDirectory() + File.separator + saveDialog.getFileName();

        try {
            final long tStart = System.currentTimeMillis();
            if (arg.compareToIgnoreCase("ASCII") == 0) {
                saveAsVtkAscii(fileName, imp);
            } else {
                saveAsVtkBinary(fileName, imp);
            }
            final long tStop = System.currentTimeMillis();
            IJ.showStatus("Saving of '" + saveDialog.getFileName() + "' completed in " + (tStop - tStart) + " ms.");
        } catch (final Exception ex) {
            ex.printStackTrace();
            String msg = ex.getMessage();
            if (msg == null) {
                msg = "";
            } else {
                msg = "\n" + msg;
            }

            IJ.showMessage(DIALOG_CAPTION, "Error writing file '" + fileName + "'." + msg);
        }
    }
}
