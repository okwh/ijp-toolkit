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
package net.sf.ij.vtk;

import vtk.vtkImageAnisotropicDiffusion3D;
import vtk.vtkImageData;
import vtk.vtkImageCast;

/**
 *  Wrapper for vtkImageAnisotropicDiffusion3D.
 *
 * vtkImageAnisotropicDiffusion3D diffuses an volume iteratively.
 * "DiffusionFactor" determines how far a pixel value moves toward its
 * neighbors, and is insensitive to the number of neighbors chosen.
 * The diffusion is anisotropic because it only occurs when a gradient
 * measure is below "GradientThreshold". Two gradient measures exist and
 * are toggled by the "GradientMagnitudeThreshold" flag. When
 * "GradientMagnitudeThreshold" is on, the magnitude of the gradient,
 * computed by central differences, above "DiffusionThreshold" a voxel is
 * not modified. The alternative measure examines each neighbor independently.
 * The gradient between the voxel and the neighbor must be below the
 * "DiffusionThreshold" for diffusion to occur with THAT neighbor.
 *
 *
 *
 * @author   Jarek Sacha
 * @version  $Revision: 1.4 $
 */

public class AnisotropicDiffusion extends VtkImageFilter {

  private vtkImageAnisotropicDiffusion3D filter;
  private vtkImageCast inputCast;
  private VtkProgressObserver progressObserver;
  private static final String HELP_STRING =
      "This is a wrapper for vtkImageAnisotropicDiffusion3D filter.\n " +
      "vtkImageAnisotropicDiffusion3D diffuses an volume iteratively. " +
      "\"DiffusionFactor\" determines how far a pixel value moves toward its " +
      "neighbors, and is insensitive to the number of neighbors chosen. " +
      "The diffusion is anisotropic because it only occurs when a gradient " +
      "measure is below \"GradientThreshold\". Two gradient measures exist and  " +
      "are toggled by the \"GradientMagnitudeThreshold\" flag. When " +
      "\"GradientMagnitudeThreshold\" is on, the magnitude of the gradient, " +
      "computed by central differences, above \"DiffusionThreshold\" a voxel is " +
      "not modified. The alternative measure examines each neighbor independently. " +
      "The gradient between the voxel and the neighbor must be below the " +
      "\"DiffusionThreshold\" for diffusion to occur with THAT neighbor.";


  /**  Constructor for the AnisotropicDiffusion object */
  public AnisotropicDiffusion() {
    inputCast = new vtk.vtkImageCast();
    inputCast.SetOutputScalarTypeToFloat();
    filter = new vtkImageAnisotropicDiffusion3D();
    filter.SetInput(inputCast.GetOutput());
    progressObserver = new VtkProgressObserver(filter);
  }


  /**
   *  Sets the numberOfIterations attribute of the AnisotropicDiffusion object
   *
   * @param  i  The new numberOfIterations value
   */
  public void setNumberOfIterations(int i) {
    filter.SetNumberOfIterations(i);
  }


  /**
   *  Sets the diffusionFactor attribute of the AnisotropicDiffusion object
   *
   * @param  d  The new diffusionFactor value
   */
  public void setDiffusionFactor(double d) {
    filter.SetDiffusionFactor(d);
  }


  /**
   *  Sets the gradientMagnitudeThreshold attribute of the
   *  AnisotropicDiffusion object
   *
   * @param  enabled  The new gradientMagnitudeThreshold value
   */
  public void setGradientMagnitudeThreshold(boolean enabled) {
    if (enabled) {
      filter.GradientMagnitudeThresholdOn();
    } else {
      filter.GradientMagnitudeThresholdOff();
    }
  }


  /**
   *  Sets the diffusionThreshold attribute of the AnisotropicDiffusion object
   *
   * @param  t  The new diffusionThreshold value
   */
  public void setDiffusionThreshold(double t) {
    filter.SetDiffusionThreshold(t);
  }


  /**  Description of the Method */
  public void update() {
    try {
      // Push input to VTK pipeline
      vtkImageData inputImageData = VtkImageDataFactory.create(inputImage);

      // update VTK pipeline
      inputCast.SetInput(inputImageData);
      filter.Update();

      // Pull output from VTK pipeleine
      vtkImageData outputImageData = filter.GetOutput();
      outputImage = VtkUtil.createImagePlus(outputImageData);
    } catch (Exception ex) {
      ex.printStackTrace();

    }
  }

  public String getHelpString() {
    return HELP_STRING;
  }


  /**
   *  Gets the diffusionFactor attribute of the AnisotropicDiffusion object
   *
   * @return    The diffusionFactor value
   */
  public double getDiffusionFactor() {
    return filter.GetDiffusionFactor();
  }


  /**
   *  Gets the diffusionThreshold attribute of the AnisotropicDiffusion object
   *
   * @return    The diffusionThreshold value
   */
  public double getDiffusionThreshold() {
    return filter.GetDiffusionThreshold();
  }


  /**
   *  Gets the gradientMagnitudeThreshold attribute of the
   *  AnisotropicDiffusion object
   *
   * @return    The gradientMagnitudeThreshold value
   */
  public boolean isGradientMagnitudeThreshold() {
    return filter.GetGradientMagnitudeThreshold() != 0;
  }


  /**
   *  Gets the numberOfIterations attribute of the AnisotropicDiffusion object
   *
   * @return    The numberOfIterations value
   */
  public int getNumberOfIterations() {
    return filter.GetNumberOfIterations();
  }
}