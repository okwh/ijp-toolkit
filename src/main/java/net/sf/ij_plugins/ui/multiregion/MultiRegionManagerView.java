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

package net.sf.ij_plugins.ui.multiregion;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.beans.PropertyConnector;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;
import net.sf.ij_plugins.ui.UIUtils;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


/**
 * @author Jarek Sacha
 */
public class MultiRegionManagerView extends JPanel {

    private final MultiRegionManagerModel model = new MultiRegionManagerModel();

    private final ListModel<Region> regionListModel = this.model.getRegions();
    private final ValueHolder regionSelectedItemHolder = new ValueHolder();
    private final SelectionInList<Region> regionSelectionInList = new SelectionInList<>(regionListModel, regionSelectedItemHolder);

    private final PresentationModel<Region> detailsModel = new PresentationModel<Region>(regionSelectionInList);
    private final ValueModel subRegionsValueModel = detailsModel.getModel(Region.PROPERTYNAME_SUB_REGIONS);
    private final ValueHolder subRegionSelectedItemHolder = new ValueHolder();
    private final SelectionInList<Region> subRegionSelectionInList = new SelectionInList<>(subRegionsValueModel, subRegionSelectedItemHolder);

    private static final long serialVersionUID = 3522389261778035936L;


    /**
     * Creates new form MultiRegionManagerView
     */
    public MultiRegionManagerView() {

        this.model.setParent(this);

        initComponents();
        regionList.setCellRenderer(new RegionCellRenderer());

        // Bind selection in regionList to model's selectedRegion
        PropertyConnector.connectAndUpdate(regionSelectedItemHolder, model, "selectedRegion");
        PropertyConnector.connectAndUpdate(subRegionSelectedItemHolder, model, "selectedSubRegion");

        regionSelectedItemHolder.setIdentityCheckEnabled(true);
    }


    public MultiRegionManagerModel getModel() {
        return model;
    }


    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
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
        final JMenuItem toROIManagerMenuItem = new JMenuItem();
        final JMenuItem fromROIManagerMenuItem = new JMenuItem();
        final JScrollPane regionScrollPane = new JScrollPane();
        final JScrollPane subRegionScrollPane = new JScrollPane();
        final JButton addRegionButton = new JButton(model.createNewRegionAction());
        final JButton addROIButton = new JButton(model.createAddCurrentROIAction());
        final JButton removeRegionButton = new JButton(model.createRemoveRegionAction());
        final JButton removeROIButton = new JButton(model.createRemoveSubRegionAction());
        final JLabel regionLabel = new JLabel();
        final JLabel roiLabel = new JLabel();
        final JButton redrawOverlaysButton = new JButton(model.createRedrawOverlaysAction());
        final JButton removeOverlayButton = new JButton(model.createRemoveOverlaysAction());
        final JSeparator jSeparator1 = new JSeparator();

        toROIManagerMenuItem.setText("Send to ROI Manager");
        toROIManagerMenuItem.setToolTipText("Send all ROIs in this region to ROI Manager");
        toROIManagerMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                toROIManagerMenuItemActionPerformed(evt);
            }
        });
        regionPopupMenu.add(toROIManagerMenuItem);

        fromROIManagerMenuItem.setText("Load selected in ROI Manager");
        fromROIManagerMenuItem.setToolTipText("Load ROIs in this region from ROI Manager. At least one must be selected in ROI Manager.");
        fromROIManagerMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                fromROIManagerMenuItemActionPerformed(evt);
            }
        });
        regionPopupMenu.add(fromROIManagerMenuItem);

        regionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        regionList.setSelectedIndex(0);
        regionList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent evt) {
                regionListMouseClicked(evt);
            }
        });
        regionScrollPane.setViewportView(regionList);

        subRegionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        subRegionScrollPane.setViewportView(subRegionList);

        addRegionButton.setIcon(new ImageIcon(getClass().getResource("/net/sf/ij_plugins/ui/multiregion/edit_add.png"))); // NOI18N
        addRegionButton.setText("Add Region");
        addRegionButton.setToolTipText("Add new region (a collection of ROIs).");
        addRegionButton.setHorizontalAlignment(SwingConstants.LEADING);

        addROIButton.setIcon(new ImageIcon(getClass().getResource("/net/sf/ij_plugins/ui/multiregion/edit_add.png"))); // NOI18N
        addROIButton.setText("Add Current ROI");
        addROIButton.setToolTipText("Add ROI from current image to selected region.");
        addROIButton.setHorizontalAlignment(SwingConstants.LEADING);

        removeRegionButton.setIcon(new ImageIcon(getClass().getResource("/net/sf/ij_plugins/ui/multiregion/edit_remove.png"))); // NOI18N
        removeRegionButton.setText("Remove Region");
        removeRegionButton.setToolTipText("Remove region selected in the list.");
        removeRegionButton.setHorizontalAlignment(SwingConstants.LEADING);

        removeROIButton.setIcon(new ImageIcon(getClass().getResource("/net/sf/ij_plugins/ui/multiregion/edit_remove.png"))); // NOI18N
        removeROIButton.setText("Remove ROI");
        removeROIButton.setToolTipText("Remove ROI selected in the list.");
        removeROIButton.setHorizontalAlignment(SwingConstants.LEADING);

        regionLabel.setText("Regions");

        roiLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        roiLabel.setText("ROIs");
        roiLabel.setHorizontalTextPosition(SwingConstants.RIGHT);

        redrawOverlaysButton.setIcon(new ImageIcon(getClass().getResource("/net/sf/ij_plugins/ui/multiregion/reload.png"))); // NOI18N
        redrawOverlaysButton.setText("Redraw Overlays");
        redrawOverlaysButton.setToolTipText("Draw ROIs overlay on current image");
        redrawOverlaysButton.setHorizontalAlignment(SwingConstants.LEADING);

        removeOverlayButton.setIcon(new ImageIcon(getClass().getResource("/net/sf/ij_plugins/ui/multiregion/delete.png"))); // NOI18N
        removeOverlayButton.setText("Remove Overlays");
        removeOverlayButton.setToolTipText("Remove overlays from the current image, if any.");
        removeOverlayButton.setHorizontalAlignment(SwingConstants.LEADING);

        final GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(Alignment.LEADING, false)
                                        .addComponent(roiLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(regionLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(12, 12, 12)
                                .addGroup(layout.createParallelGroup(Alignment.TRAILING)
                                        .addComponent(regionScrollPane, GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
                                        .addComponent(subRegionScrollPane, GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                                        .addGroup(layout.createParallelGroup(Alignment.LEADING)
                                                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                                                        .addComponent(addRegionButton, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 133, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(removeRegionButton, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 133, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(redrawOverlaysButton, Alignment.TRAILING)
                                                        .addComponent(removeOverlayButton, Alignment.TRAILING))
                                                .addComponent(jSeparator1, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 133, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createParallelGroup(Alignment.LEADING)
                                                .addComponent(addROIButton, Alignment.TRAILING)
                                                .addComponent(removeROIButton, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 133, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
        );

        layout.linkSize(SwingConstants.HORIZONTAL, new Component[]{addROIButton, addRegionButton, jSeparator1, redrawOverlaysButton, removeOverlayButton, removeROIButton, removeRegionButton});

        layout.setVerticalGroup(
                layout.createParallelGroup(Alignment.LEADING)
                        .addGroup(Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                                                        .addComponent(regionLabel)
                                                        .addComponent(regionScrollPane, GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE))
                                                .addPreferredGap(ComponentPlacement.RELATED))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(addRegionButton)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(removeRegionButton)
                                                .addGap(73, 73, 73)))
                                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                                        .addGroup(Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(addROIButton)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(removeROIButton, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                                                .addComponent(jSeparator1, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(redrawOverlaysButton)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(removeOverlayButton))
                                        .addComponent(subRegionScrollPane, GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                                        .addComponent(roiLabel))
                                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    private void regionListMouseClicked(final MouseEvent evt) {//GEN-FIRST:event_regionListMouseClicked
        // if right mouse button clicked (or me.isPopupTrigger())
        if (SwingUtilities.isRightMouseButton(evt)
                && !regionList.isSelectionEmpty()
                && regionList.locationToIndex(evt.getPoint())
                == regionList.getSelectedIndex()) {
            regionPopupMenu.show(regionList, evt.getX(), evt.getY());
        }

    }//GEN-LAST:event_regionListMouseClicked


    private void toROIManagerMenuItemActionPerformed(final ActionEvent evt) {//GEN-FIRST:event_toROIManagerMenuItemActionPerformed
        model.sentCurrentRegionToROIManager();
    }//GEN-LAST:event_toROIManagerMenuItemActionPerformed


    private void fromROIManagerMenuItemActionPerformed(final ActionEvent evt) {//GEN-FIRST:event_fromROIManagerMenuItemActionPerformed
        model.loadCurrentRegionFromROIManager();
    }//GEN-LAST:event_fromROIManagerMenuItemActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final JList regionList = BasicComponentFactory.createList(regionSelectionInList);
    private final JPopupMenu regionPopupMenu = new JPopupMenu();
    private final JList subRegionList = BasicComponentFactory.createList(subRegionSelectionInList);
    // End of variables declaration//GEN-END:variables


    static class RegionCellRenderer extends JLabel implements ListCellRenderer<Region> {

        private static final int ICON_SIZE = 10;
        private static final long serialVersionUID = 1L;

        // This is the only method defined by ListCellRenderer.
        // We just reconfigure the JLabel each time we're called.


        @Override
        public Component getListCellRendererComponent(
                final JList<? extends Region> list,              // the list
                final Region region,            // value to display
                final int index,               // cell index
                final boolean isSelected,      // is the cell selected
                final boolean cellHasFocus)    // does the cell have focus
        {
            setText(region.getName());

            // Create icon
            final Color color = new Color(region.getColor().getRGB());
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
