/*
 * IJ-Plugins
 * Copyright (C) 2002-2016 Jarek Sacha
 * Author's email: jpsacha at gmail dot com
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Latest release available at http://sourceforge.net/projects/ij-plugins/
 */

package net.sf.ij_plugins.concurrent;

import ij.process.Blitter;
import ij.process.ImageProcessor;
import net.sf.ij_plugins.util.progress.DefaultProgressReporter;


/**
 * @author Jarek Sacha
 * @since 3/8/11 9:46 PM
 */
class BlitterSP extends DefaultProgressReporter implements SliceProducer {

    final ImageProcessor ip1;
    final ImageProcessor ip2;
    final int mode = Blitter.MULTIPLY;


    BlitterSP(final ImageProcessor ip1, final ImageProcessor ip2) {
        this.ip1 = ip1;
        this.ip2 = ip2;
    }


    @Override
    public ImageProcessor produce() throws Exception {
        // Forward progress notification is using algorithm that supports it.
//            ...addProgressListener(new ProgressListener() {
//                public void progressNotification(final ProgressEvent e) {
//                    notifyProgressListeners(e.getProgress(), e.getMessage());
//                }
//            });

        notifyProgressListeners(0);
        final ImageProcessor dest = ip1.duplicate();
        dest.copyBits(ip2, 0, 0, mode);
        notifyProgressListeners(1);
        return dest;
    }
}
