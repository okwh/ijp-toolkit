/***
 * Image/J Plugins
 * Copyright (C) 2002 Jarek Sacha
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
package net.sf.ij.im3d.grow;

import ij.*;

import java.util.*;

import net.sf.ij.im3d.*;

/**
 *  Simple region growing algorithm that extracts all pixels connected to the
 *  seed as long as they intensities are within given threshold limits. Min
 *  limit is inclusive, max limit is exclusive.
 *
 *@author     Jarek Sacha
 *@created    April 29, 2002
 *@version    $Revision: 1.1 $
 */

public class ConnectedThresholdFilterUInt8 extends ConnectedThresholdFilterBase {

  /** Source pixels */
  protected byte[][] srcPixels = null;


  /*
   *
   */
  /**
   *  Description of the Method
   *
   *@param  src  Description of the Parameter
   */
  final protected void createHandleToSrcPixels(ImageStack src) {
    Object[] imageArray = src.getImageArray();

    int n = src.getSize();
    srcPixels = new byte[n][];
    for (int z = 0; z < n; ++z) {
      srcPixels[z] = (byte[]) imageArray[z];
      if (!(imageArray[z] instanceof byte[])) {
        throw new IllegalArgumentException("Expecting stack of byte images.");
      }
    }
  }


  /**
   *  Check if point with coordinates (x,y,z) is a new candidate. Point is a
   *  candidate if 1) its coordinates are within ROI, 2) it was not yet
   *  analyzed, 3) its value is within limits. <p>
   *
   *  This method modifies 'candidatePoints' and 'destPixels'.
   *
   *@param  x
   *@param  y
   *@param  z
   */
  final protected void checkForGrow(int x, int y, int z) {
    if (x < xMin || x >= xMax ||
        y < yMin || y >= yMax ||
        z < zMin || z >= zMax) {
      return;
    }

    int offset = y * xSize + x;
    if (destPixels[z][offset] == BACKGROUND) {
      int value = srcPixels[z][offset] & 0xff;
      if (value >= valueMin && value < valueMax) {
        destPixels[z][offset] = MARKER;
        candidatePoints.addLast(new Point3D(x, y, z));
      } else {
        destPixels[z][offset] = NOT_MEMBER;
      }
    }
  }


  /*
   *
   */
  /**
   *  Description of the Method
   *
   *@return    Description of the Return Value
   */
  private ImageStack createOutputStack() {
    ImageStack dest = new ImageStack(xSize, ySize);
    int sliceSize = xSize * ySize;
    for (int z = 0; z < zSize; ++z) {
      byte[] slicePixels = destPixels[z];
      for (int i = 0; i < sliceSize; ++i) {
        if (slicePixels[i] == NOT_MEMBER) {
          slicePixels[i] = BACKGROUND;
        }
      }
      dest.addSlice(null, slicePixels);
    }

    return dest;
  }
}
