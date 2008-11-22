/*
 * Image/J Plugins
 * Copyright (C) 2002-2008 Jarek Sacha
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
 *
 */
package net.sf.ij_plugins.io.vtk;

import ij.plugin.PlugIn;

/**
 * Save image in <a HREF="http://public.kitware.com/VTK/">VTK</a> format. Supported image types:
 * GRAY8, GRAY16, GRAY32.
 *
 * @author Jarek Sacha
 * @see net.sf.ij_plugins.io.vtk.VtkEncoder
 * @since April 28, 2002
 */

public class VtkWriterPlugin implements PlugIn {
    /**
     * Main processing method for the net.sf.ij_plugins.io.vtk.VtkWriterPlugin plugin
     *
     * @param arg If equal "ASCII" file will be saved in text format otherwise in binary format
     *            (MSB).
     */
    @Override
    public void run(String arg) {
        new VtkEncoder().run(arg);
    }
}
