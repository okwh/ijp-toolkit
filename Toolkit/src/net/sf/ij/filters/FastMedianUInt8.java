/***
 * Image/J Plugins
 * Copyright (C) 2002-2004 Jarek Sacha
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
package net.sf.ij.filters;

import ij.ImagePlus;
import ij.process.ByteProcessor;
import net.sf.ij.io.IOUtils;

import java.io.IOException;

/**
 * Implements fast median filter using running median approach.
 *
 * @author Jarek Sacha
 * @version $Revision: 1.1 $
 */
public class FastMedianUInt8 extends RunningUInt8Filter {

    public FastMedianUInt8() {
        super(new RunningMedianUInt8Operator());
    }

    public static void main(String[] args) {
        try {
            ij.ImageJ.main(null);
            ImagePlus imp = null;
            imp = IOUtils.openImage("test_images/blobs_noise.tif");
            imp.show();
            ByteProcessor src = (ByteProcessor) imp.getProcessor();

            final FastMedianUInt8 fastMedianUInt8 = new FastMedianUInt8();
            ByteProcessor dest = fastMedianUInt8.run(src, 11, 11);
            new ImagePlus("Median", dest).show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
