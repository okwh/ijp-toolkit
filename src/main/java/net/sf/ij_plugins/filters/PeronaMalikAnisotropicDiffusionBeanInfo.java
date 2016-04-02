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
package net.sf.ij_plugins.filters;

import net.sf.ij_plugins.IJPluginsRuntimeException;
import net.sf.ij_plugins.util.IJPluginsSimpleBeanInfo;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

/**
 * @author Jarek Sacha
 */
public class PeronaMalikAnisotropicDiffusionBeanInfo extends IJPluginsSimpleBeanInfo {

    public PeronaMalikAnisotropicDiffusionBeanInfo() {
        super(PeronaMalikAnisotropicDiffusion.class);
    }


    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            return new PropertyDescriptor[]{
                    create("bigRegionFunction"),
                    create("k"),
            };
        } catch (final IntrospectionException e) {
            throw new IJPluginsRuntimeException(e);
        }
    }

    @Override
    public BeanInfo[] getAdditionalBeanInfo() {
        try {
            return new BeanInfo[]{Introspector.getBeanInfo(beanClass.getSuperclass())};
        } catch (final IntrospectionException e) {
            throw new IJPluginsRuntimeException(e);
        }
    }

}
