# 482 Project by Michael Nowicki
# GRAPHER README
---
# System Requirements

- To run Grapher the Java Development Kit (JDK) version 7 or higher must be installed
- Supports Windows 7 or higher, or Ubuntu 14.04 or higher
- One of: Eclipse IDE, or Ant installed on the system

# Installation

There are two way in which Grapher can be installed. The first uses
Ant built into Eclipse, or Ant can be run from the command line. First extract
the zip file to a location of your choice.

## Installing with Eclipse

1. Open Eclipse
2. Drag the build.xml file into the editor
3. Click Run As
4. Select the first Ant Build File option

## Installing using the Command Line/Shell

1. Open the Command Line or Shell and navigate to where the project was extracted
2. Type `ant` and hit enter to install

---

## Running Grapher

Double click the jar that was created in the Grapher folder after installation
Type the Java code you wish to run, for example:

`System.out.println("Hello World!");`

Hit the Run button, or Tools->Build Script

The output will be displayed in the bottom console, so from this example the console will print:

`Hello World!`