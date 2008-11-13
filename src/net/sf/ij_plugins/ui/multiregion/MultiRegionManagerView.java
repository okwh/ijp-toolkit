/***
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
 */

package net.sf.ij_plugins.ui.multiregion;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.beans.PropertyConnector;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;
import net.sf.ij_plugins.ui.UIUtils;

import javax.swing.*;
import java.awt.Color;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author Jarek Sacha
 */
public class MultiRegionManagerView extends JPanel {

    private final MultiRegionManagerModel model = new MultiRegionManagerModel();

    private final ListModel regionListModel = this.model.getRegions();
    private final ValueHolder regionSelectedItemHolder = new ValueHolder();
    private final SelectionInList<Region> regionSelectionInList = new SelectionInList<Region>(regionListModel, regionSelectedItemHolder);

    private final PresentationModel<Region> detailsModel = new PresentationModel<Region>(regionSelectionInList);
    private final ValueModel subRegionsValueModel = detailsModel.getModel(Region.PROPERTYNAME_SUB_REGIONS);
    private final ValueHolder subRegionSelectedItemHolder = new ValueHolder();
    private final SelectionInList<Region> subRegionSelectionInList = new SelectionInList<Region>(subRegionsValueModel, subRegionSelectedItemHolder);
    private static final long serialVersionUID = 3522389261778035936L;

    /**
     * Creates new form MultiRegionManagerView
     */
    public MultiRegionManagerView() {

        this.model.setParent(this);


        initComponents();
        System.out.println("MultiRegionManagerView.MultiRegionManagerView - after initComponents()");
        regionList.setCellRenderer(new RegionCellRenderer());

        // Bind selection in regionList to model's selectedRegion
        PropertyConnector.connectAndUpdate(regionSelectedItemHolder, model, "selectedRegion");
        PropertyConnector.connectAndUpdate(subRegionSelectedItemHolder, model, "selectedSubRegion");


        regionSelectedItemHolder.setIdentityCheckEnabled(true);
        final PropertyAdapter pa = new PropertyAdapter(regionSelectedItemHolder, "subRegions", true);
        pa.addValueChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
//                System.out.println("> evt= " + evt);
//                Object value = pa.getValue();
//                System.out.println("> value= " + value);
            }
        });
    }

    public MultiRegionManagerModel getModel() {
        return model;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                final JPanel panel = new MultiRegionManagerView();

                final JFrame frame = new JFrame();
                frame.getContentPane().add(panel);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        JScrollPane regionScrollPane = new JScrollPane();
        regionList = BasicComponentFactory.createList(regionSelectionInList);
        JScrollPane subRegionScrollPane = new JScrollPane();
        final JList subRegionList = BasicComponentFactory.createList(subRegionSelectionInList);
        JButton addRegionButton = new JButton(model.createNewRegionAction());
        JButton addROIButton = new JButton(model.createAddCurrentROIAction());
        JButton removeRegionButton = new JButton(model.createRemoveRegionAction());
        JButton removeROIButton = new JButton(model.createRemoveSubRegionAction());
        JLabel regionLabel = new JLabel();
        JLabel roiLabel = new JLabel();
        JButton redrawOverlaysButton = new JButton(model.createRedrawOverlaysAction());
        JButton removeOverlayButton = new JButton(model.createRemoveOverlaysAction());
        JSeparator jSeparator1 = new JSeparator();

        regionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        regionList.setSelectedIndex(0);
        regionScrollPane.setViewportView(regionList);

        subRegionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        subRegionScrollPane.setViewportView(subRegionList);

        addRegionButton.setText("Add Region");
        addRegionButton.setToolTipText("Add new region (a collection of ROIs).");

        addROIButton.setText("Add Current ROI");
        addROIButton.setToolTipText("Add ROI from current image to selected region.");

        removeRegionButton.setText("Remove Region");
        removeRegionButton.setToolTipText("Remove region selected in the list.");

        removeROIButton.setText("Remove ROI");
        removeROIButton.setToolTipText("Remove ROI selected in the list.");

        regionLabel.setText("Regions");

        roiLabel.setText("ROIs");

        redrawOverlaysButton.setText("Redraw Overlays");
        redrawOverlaysButton.setToolTipText("Draw ROIs overlay on current image");

        removeOverlayButton.setText("Remove Overlays");
        removeOverlayButton.setToolTipText("Remove overlays from the current image, if any.");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(regionLabel)
                                .add(roiLabel))
                        .add(12, 12, 12)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                .add(regionScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                                .add(subRegionScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 10, Short.MAX_VALUE)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                .add(jSeparator1)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, addRegionButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, removeRegionButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, removeROIButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, addROIButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                                .add(redrawOverlaysButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(removeOverlayButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(layout.createSequentialGroup()
                                        .add(addRegionButton)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(removeRegionButton))
                                .add(regionLabel)
                                .add(regionScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(roiLabel)
                                .add(layout.createSequentialGroup()
                                        .add(addROIButton)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(removeROIButton)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 13, Short.MAX_VALUE)
                                        .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(redrawOverlaysButton)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(removeOverlayButton))
                                .add(subRegionScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE))
                        .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JList regionList;
    // End of variables declaration//GEN-END:variables


    static class RegionCellRenderer extends JLabel implements ListCellRenderer {
        private static final int ICON_SIZE = 10;
        private static final long serialVersionUID = 1L;

        // This is the only method defined by ListCellRenderer.
        // We just reconfigure the JLabel each time we're called.
        public Component getListCellRendererComponent(
                final JList list,              // the list
                final Object value,            // value to display
                final int index,               // cell index
                final boolean isSelected,      // is the cell selected
                final boolean cellHasFocus)    // does the cell have focus
        {
            final String text;
            final Color color;
            if (value instanceof Region) {
                final Region r = (Region) value;
                text = r.getName();
                color = r.getColor();
            } else {
                text = value.toString();
                color = new Color(0, 0, 0, 255);
            }

            setText(text);

            // Create icon
            setIcon(UIUtils.createColorIcon(ICON_SIZE, ICON_SIZE, color));

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            setEnabled(list.isEnabled());
            setFont(list.getFont());
            setOpaque(true);
            return this;
        }
    }
}
