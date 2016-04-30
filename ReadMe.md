IJ Plugins Toolkit
==================

[![Build Status](https://travis-ci.org/ij-plugins/ijp-toolkit.svg?branch=develop)](https://travis-ci.org/ij-plugins/ijp-toolkit)

[IJ Plugins Toolkit](http://ij-plugins.sourceforge.net/plugins/toolkit.html) is a set of plugins for [ImageJ]. The plugins are grouped into:

* **3D IO** - import and export of data in 3D formats.

* **3D Toolkit** - operations on stacks interpreted as 3D images, including
  morphological operations.

* **Color** - color space conversion, color edge detection (color and
  multi-band images).

* **Filters** - fast median filters and various anisotropic diffusion filters.

* **Graphics** - Texture Synthesis - A plugin to perform texture synthesis
  using the image quilting algorithm of Efros and Freeman.

* **Segmentation** - image segmentation through clustering, thresholding, and
  region growing.


Running from source
-------------------

You can build and run the use the plugins within ImageJ using SBT task `ijRun`

```
sbt ijRun
```

It will build the code, setup plugins directory, and the start ImageJ. `ijRun` si provided by SBT plugin [sbt-imagej].

ImageJ Plugins Installation
---------------------------

1. [Download](https://sourceforge.net/projects/ij-plugins/files/ij-plugins_toolkit/)
   latest binaries for ij-plugins Toolkit. Look for version with the highest number.
   Plugin binaries will be in file named: `ij-plugins_toolkit_bin_*.zip`.

2. Uncompress content of `ij-plugins_toolkit_bin_*.zip` to ImageJ's `plugins` directory.
   You can find location of ImageJ plugins directory by selecting in ImageJ
   "Plugins"/"Utilities"/"ImageJ Properties", look for value of tag "plugins dir"
   near the bottom of the displayed Properties' window.

3. Restart ImageJ to load newly installed plugins.

[ImageJ]: http://rsbweb.nih.gov/ij/
[sbt-imagej]: https://github.com/jpsacha/sbt-imagej