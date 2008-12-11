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
package net.sf.ij_plugins.grow;

import net.sf.ij_plugins.ui.multiregion.MultiRegionManagerView;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

/**
 * @author Jarek Sacha
 * @since February 18, 2008
 */
public class RegionGrowingView extends JPanel {

    private final RunAction runAction;
    private static final long serialVersionUID = 4217582431710992600L;

    /**
     * Creates new form RegionGrowingView
     */
    public RegionGrowingView() {
        final RegionGrowingModel model = new RegionGrowingModel(multiRegionManagerView.getModel());
        runAction = new RunAction(model, this);

        initComponents();
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
        final JSeparator seedSelectionSeparator = new JSeparator();
        final JLabel seedSelectionLabel = new JLabel();
        final JSeparator bottomSeparator = new JSeparator();
        final JButton runButton = new JButton();

        seedSelectionLabel.setText("Seed Selection");

        runButton.setAction(runAction);

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(Alignment.LEADING)
                                .addComponent(bottomSeparator, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 531, Short.MAX_VALUE)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(seedSelectionLabel)
                                        .addPreferredGap(ComponentPlacement.UNRELATED)
                                        .addComponent(seedSelectionSeparator, GroupLayout.DEFAULT_SIZE, 451, Short.MAX_VALUE))
                                .addComponent(multiRegionManagerView, GroupLayout.DEFAULT_SIZE, 531, Short.MAX_VALUE)
                                .addComponent(runButton, Alignment.TRAILING))
                        .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(Alignment.TRAILING)
                                .addComponent(seedSelectionLabel)
                                .addComponent(seedSelectionSeparator, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addComponent(multiRegionManagerView, GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                        .addPreferredGap(ComponentPlacement.UNRELATED)
                        .addComponent(bottomSeparator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(runButton)
                        .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final MultiRegionManagerView multiRegionManagerView = new MultiRegionManagerView();
    // End of variables declaration//GEN-END:variables

}
