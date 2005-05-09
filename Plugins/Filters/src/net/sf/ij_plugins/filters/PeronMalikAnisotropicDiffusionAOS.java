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
package net.sf.ij_plugins.filters;

import ij.process.FloatProcessor;

import net.sf.ij_plugins.operators.Neighborhood3x3;
import net.sf.ij_plugins.operators.PixelIterator;
import net.sf.ij_plugins.operators.Neighborhood3x3;
import net.sf.ij_plugins.operators.PixelIterator;


/**
 * @author Jarek Sacha
 * @version $ Revision: $
 */
public class PeronMalikAnisotropicDiffusionAOS {
    private double lambda = 0.2;
    private int numberOfIterations = 10;
    private boolean bigRegionFunction = true;
    private float k = 10;
    private float inv_k = 1 / k;

    public FloatProcessor process(FloatProcessor src) {
        FloatProcessor fp1 = (FloatProcessor) src.duplicate();
        FloatProcessor fp2 = (FloatProcessor) src.duplicate();

        for (int i = 0; i < numberOfIterations; i++) {
            diffuse(fp1, fp2);

            // swap
            FloatProcessor tmp = fp2;
            fp2 = fp1;
            fp1 = tmp;
        }

        return fp1;
    }

    public float getK() {
        return k;
    }

    public void setK(float k) {
        this.k = k;
        inv_k = 1 / k;
    }

    public boolean isBigRegionFunction() {
        return bigRegionFunction;
    }

    public void setBigRegionFunction(boolean bigRegionFunction) {
        this.bigRegionFunction = bigRegionFunction;
    }

    public int getNumberOfIterations() {
        return numberOfIterations;
    }

    public void setNumberOfIterations(int numberOfIterations) {
        this.numberOfIterations = numberOfIterations;
    }

    public double getLambda() {
        return lambda;
    }

    public void setLambda(double lambda) {
        this.lambda = lambda;
    }

    /**
     * Perform single diffusion operation
     */
    private void diffuse(FloatProcessor src, FloatProcessor dest) {
        float[] destPixels = (float[]) dest.getPixels();
        PixelIterator iterator = new PixelIterator(src);

        /* TODO: Update: "Calculate the gradient values for each point" */
        while (iterator.hasNext()) {
            Neighborhood3x3 n = (Neighborhood3x3) iterator.next();
            float[] neighbors = n.getNeighbors();

            // 4-connected neighbors
            double sum4component = 0;

            for (int i = 1; i < neighbors.length; i += 2) {
                float gradient = neighbors[i] - n.center;
                sum4component += (g(gradient) * gradient);
            }

            //      // 8-connected neighbors
            //      double sum8component = 0;
            //      for (int i = 2; i < neighbors.length; i+=2) {
            //        float gradient = neighbors[i] - n.center;
            //        sum8component += gradient;
            //      }
            //
            //      double sqrt2div2 = .707106781;
            //      double newValue = (n.center + lambda * (sum4component + sqrt2div2 * sum8component));
            double newValue = n.center + (lambda * (sum4component));

            destPixels[n.offset] = (float) newValue;
        }
    }

    /**
     * Function preserving (and enhancing) edges
     */
    public final double g(double v) {
        if (bigRegionFunction) {
            //            return 1 / (1 + Math.pow(v / k, 2));
            double h = v * inv_k;

            return 1 / (1 + (h * h));
        } else {
            //            return Math.exp(-Math.pow(v / k, 2));
            double h = v * inv_k;

            return Math.exp(-h * h);
        }
    }
}