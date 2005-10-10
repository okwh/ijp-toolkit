/***
 * Image/J Plugins
 * Copyright (C) 2002-2005 Jarek Sacha
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
package net.sf.ij_plugins.util;

import ij.gui.GenericDialog;
import net.sf.ij_plugins.IJPluginsRuntimeException;
import org.apache.commons.beanutils.PropertyUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

/**
 * Utilities for simplifying creation of property editor dialogs.
 *
 * @author Jarek Sacha
 */
public class DialogUtil {
    private DialogUtil() {
    }

    /**
     * Utility to automatically create ImageJ's GenericDialog for editing bean properties. It uses BeanInfo to extract
     * display names for each field. If a fields type is not supported irs name will be displayed with a tag
     * "[Unsupported type: class_name]".
     *
     * @param bean
     * @return <code>true</code> if user closed bean dialog using OK button, <code>false</code> otherwise.
     */
    static public boolean showGenericDialog(final Object bean, final String title) {
        final BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(bean.getClass());
        } catch (IntrospectionException e) {
            throw new IJPluginsRuntimeException("Error extracting bean info.", e);
        }

        final GenericDialog genericDialog = new GenericDialog(title);

        // Create generic dialog fields for each bean's property
        final PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        try {
            for (int i = 0; i < propertyDescriptors.length; i++) {
                final PropertyDescriptor pd = propertyDescriptors[i];
                final Class type = pd.getPropertyType();
                if (type.equals(Class.class) && "class".equals(pd.getName())) {
                    continue;
                }

                final Object o = PropertyUtils.getSimpleProperty(bean, pd.getName());
                if (type.equals(Boolean.TYPE)) {
                    boolean value = ((Boolean) o).booleanValue();
                    genericDialog.addCheckbox(pd.getDisplayName(), value);
                } else if (type.equals(Integer.TYPE)) {
                    int value = ((Integer) o).intValue();
                    genericDialog.addNumericField(pd.getDisplayName(), value, 0);
                } else if (type.equals(Float.TYPE)) {
                    double value = ((Float) o).doubleValue();
                    genericDialog.addNumericField(pd.getDisplayName(), value, 6, 10, "");
                } else if (type.equals(Double.TYPE)) {
                    double value = ((Double) o).doubleValue();
                    genericDialog.addNumericField(pd.getDisplayName(), value, 6, 10, "");
                } else {
                    genericDialog.addMessage(pd.getDisplayName() + "[Unsupported type: " + type + "]");
                }

            }
        } catch (IllegalAccessException e) {
            throw new IJPluginsRuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new IJPluginsRuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new IJPluginsRuntimeException(e);
        }

//        final Panel helpPanel = new Panel();
//        helpPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
//        final Button helpButton = new Button("Help");
////                helpButton.addActionListener(this);
//        helpPanel.add(helpButton);
//        genericDialog.addPanel(helpPanel, GridBagConstraints.EAST, new Insets(15,0,0,0));

        // Show modal dialog
        genericDialog.showDialog();
        if (genericDialog.wasCanceled()) {
            return false;
        }

        // Read fields from generic dialog into bean's properties.
        try {
            for (int i = 0; i < propertyDescriptors.length; i++) {
                final PropertyDescriptor pd = propertyDescriptors[i];
                final Class type = pd.getPropertyType();
                final Object propertyValue;
                if (type.equals(Boolean.TYPE)) {
                    boolean value = genericDialog.getNextBoolean();
                    propertyValue = Boolean.valueOf(value);
                } else if (type.equals(Integer.TYPE)) {
                    int value = (int) Math.round(genericDialog.getNextNumber());
                    propertyValue = new Integer(value);
                } else if (type.equals(Float.TYPE)) {
                    double value = genericDialog.getNextNumber();
                    propertyValue = new Float(value);
                } else if (type.equals(Double.TYPE)) {
                    double value = genericDialog.getNextNumber();
                    propertyValue = new Double(value);
                } else {
                    continue;
                }
                PropertyUtils.setProperty(bean, pd.getName(), propertyValue);
            }
        } catch (IllegalAccessException e) {
            throw new IJPluginsRuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new IJPluginsRuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new IJPluginsRuntimeException(e);
        }

        return true;
    }
}