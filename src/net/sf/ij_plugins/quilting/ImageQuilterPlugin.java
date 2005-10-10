/***
 * Image/J Plugins
 * Copyright (C) 2004 Jarek Sacha
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
package net.sf.ij_plugins.quilting;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.io.Opener;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import net.sf.ij_plugins.IJPluginsRuntimeException;

public class ImageQuilterPlugin implements PlugInFilter, Cloneable {
    final private static String DEST_WIDTH_LABEL = "Output width";
    final private static String DEST_HEIGHT_LABEL = "Output height";
    final private static String PATCH_SIZE_LABEL = "Patch size";
    final private static String PATCH_OVERLAP_LABEL = "Patch overlap";
    final private static String ENABLE_HORIZ_PATHS_LABEL = "Allow horizontal paths";
    final private static String PATCH_COST_WEIGHT_LABEL = "Patch cost weight";

    final private static String PLUGIN_NAME = "Image Quilter";
    final private static String ABOUT_COMMAND = "about";
    final private static String ABOUT_MESSAGE =
            "Image Quilter plugin performs texture synthesis using image quilting\n"
                    + "algorithms of Efros and Freeman:\n" +
                    "http://www.cs.berkeley.edu/~efros/research/quilting.html\n" +
                    "  \n" +
                    "Parameters:\n" +
                    "   " + DEST_WIDTH_LABEL + " - desired width of the output image, actual width may be\n " +
                    "       slightly smaller, depending on patch size.\n" +
                    "   " + DEST_HEIGHT_LABEL + " - desired height of the output image, actual height may be\n" +
                    "       slightly smaller, depending on patch size.\n" +
                    "   " + PATCH_SIZE_LABEL + " - width and height of a patch used for quilting.\n" +
                    "   " + PATCH_OVERLAP_LABEL + " - amount of overlap between patches when matching.\n" +
                    "   " + ENABLE_HORIZ_PATHS_LABEL + " - enable improved matching by weighted paths.\n" +
                    "   " + PATCH_COST_WEIGHT_LABEL + " - weight used for improved matching.\n" +
                    "  \n" +
                    "The code of this plugin was originally developed by by Nick Vavra.\n" +
                    "Original code and description is available at: http://www.cs.wisc.edu/~vavra/cs766/.\n" +
                    "ImageJ port and info is available at: http://ij-plugins.sf.net/plugins/texturesynthesis/.";

    private static final Config config = new Config();

    private String imageTitle;


    private static final class Config implements Cloneable {
        int width = 256;
        int height = 256;
        int patchSize = ImageQuilter.DEFAULT_PATCH_SIZE;
        int overlapSize = ImageQuilter.DEFAULT_OVERLAP_SIZE;
        boolean allowHorizontalPaths = false;
        double pathCostWeight = 0.1;

        public Config duplicate() {
            try {
                return (Config) clone();
            } catch (CloneNotSupportedException e) {
                throw new IJPluginsRuntimeException(e);
            }
        }
    }


    public int setup(String s, ImagePlus imagePlus) {

        if (imagePlus != null) {
            imageTitle = imagePlus.getTitle();
        }

        if (ABOUT_COMMAND.equalsIgnoreCase(s)) {
            IJ.showMessage("About " + PLUGIN_NAME, ABOUT_MESSAGE);
            return DONE;
        } else {
            return DOES_8G | DOES_16 | DOES_32 | DOES_RGB | NO_CHANGES;
        }
    }


    public void run(ImageProcessor input) {

        final GenericDialog dialog = new GenericDialog(PLUGIN_NAME + " options");
        synchronized (config) {
            dialog.addNumericField(DEST_WIDTH_LABEL, config.width, 0);
            dialog.addNumericField(DEST_HEIGHT_LABEL, config.height, 0);
            dialog.addNumericField(PATCH_SIZE_LABEL, config.patchSize, 0);
            dialog.addNumericField(PATCH_OVERLAP_LABEL, config.overlapSize, 0);
            dialog.addCheckbox(ENABLE_HORIZ_PATHS_LABEL, config.allowHorizontalPaths);
            dialog.addNumericField(PATCH_COST_WEIGHT_LABEL, config.pathCostWeight, 4);
        }

        dialog.showDialog();
        if (dialog.wasCanceled()) {
            return;
        }

        final Config localConfig;
        synchronized (config) {
            config.width = (int) Math.round(dialog.getNextNumber());
            config.height = (int) Math.round(dialog.getNextNumber());
            config.patchSize = (int) Math.round(dialog.getNextNumber());
            config.overlapSize = (int) Math.round(dialog.getNextNumber());
            config.allowHorizontalPaths = dialog.getNextBoolean();
            config.pathCostWeight = dialog.getNextNumber();
            localConfig = config.duplicate();
        }

        final ImagePlus impPreview = new ImagePlus("Preview: Quilting of " + imageTitle, input.duplicate());
        impPreview.show();

        final ImageProcessor output = quilt(localConfig, input, impPreview);

        impPreview.setProcessor("Quilting of " + imageTitle, output);
        impPreview.updateAndDraw();
    }


    public static ImageProcessor quilt(final Config config,
                                       final ImageProcessor input,
                                       final ImagePlus previewImp) {
        // run the synthesis algorithm
        final ImageQuilter synther = new ImageQuilter(input,
                config.patchSize,
                config.overlapSize,
                config.allowHorizontalPaths,
                config.pathCostWeight);

        synther.setPreviewImage(previewImp);

        IJ.write("Quilting started at " + new java.util.Date());

        ImageProcessor output = synther.synthesize(config.width, config.height);

        IJ.write("Quilting ended at " + new java.util.Date());

        return output;
    }


    public static void main(final String[] args) {
        final Config config = new Config();
        config.width = 128;
        config.height = 128;
        config.patchSize = 64;
        config.overlapSize = 12;
        config.allowHorizontalPaths = false;
        config.pathCostWeight = 0.1;

        final Opener opener = new Opener();
        final ImagePlus imp = opener.openImage("images/3.tif");

        System.out.println("Input image size:" + imp.getWidth() + "x" + imp.getHeight());

        quilt(config, imp.getProcessor(), null);

    }
}