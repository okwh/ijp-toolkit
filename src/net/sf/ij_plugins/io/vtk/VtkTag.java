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

import net.sf.ij_plugins.util.Enumeration;


/**
 * Possible tags in the VTK file header.
 *
 * @author Jarek Sacha
 * @version $Revision: 1.1 $
 * @created June 21, 2002
 */

class VtkTag extends Enumeration {

    /**
     * # vtk DataFile Version
     */
    public final static VtkTag DATA_FILE_VERSION = new VtkTag("# vtk DataFile Version");
    /**
     * DATASET
     */
    public final static VtkTag DATASET = new VtkTag("DATASET");
    /**
     * DIMENSIONS
     */
    public final static VtkTag DIMENSIONS = new VtkTag("DIMENSIONS");
    /**
     * ORIGIN
     */
    public final static VtkTag ORIGIN = new VtkTag("ORIGIN");
    /**
     * SPACING
     */
    public final static VtkTag SPACING = new VtkTag("SPACING");
    /**
     * ASPECT_RATIO
     */
    public final static VtkTag ASPECT_RATIO = new VtkTag("ASPECT_RATIO");
    /**
     * CELL_DATA
     */
    public final static VtkTag CELL_DATA = new VtkTag("CELL_DATA");
    /**
     * POINT_DATA
     */
    public final static VtkTag POINT_DATA = new VtkTag("POINT_DATA");
    /**
     * SCALARS
     */
    public final static VtkTag SCALARS = new VtkTag("SCALARS");
    /**
     * COLOR_SCALARS
     */
    public final static VtkTag COLOR_SCALARS = new VtkTag("COLOR_SCALARS");
    /**
     * LOOKUP_TABLE
     */
    public final static VtkTag LOOKUP_TABLE = new VtkTag("LOOKUP_TABLE");


    /**
     * Constructor for the VtkTag object
     *
     * @param name Description of the Parameter
     */
    private VtkTag(final String name) {
        super(name);
    }
}
