#482 Project by Michael Nowicki
#GRAPHER README
---
#System Requirements

- To run Grapher the Java Development Kit (JDK) version 7 or higher must be installed
- Supports Windows 7 or higher, or Ubuntu 14.04 or higher
- One of: Eclipse IDE, or Ant installed on the system

#Installation

There are two way in which Grapher can be installed. The first uses
Ant built into Eclipse, or Ant can be run from the command line. First extract
the zip file to a location of your choice.

##Installing with Eclipse

1. Open Eclipse
2. Drag the build.xml file into the editor
3. Click Run As
4. Select the first Ant Build File option

##Installing using the Command Line/Shell

1. Open the Command Line or Shell and navigate to where the project was extracted
2. Type `ant` and hit enter to install

After using either version to install the application a new folder will be created in the same
directory where the zip file was extracted into.

---

##Running Grapher

Double click the jar that was created in the Grapher folder after installation
Type the Java code you wish to run, for example:

###Simple Java Example

You can write Java commands in the editor and have it be compiled and run. To quickly illustrate this try something like:

    System.out.println("Hello World");

And hit the Build Script button. You should see `Hello World` printed in the log below.

Within the editing environment it is possible to declare field variables, write methods and write classes.

___

###Importing Java Packages

To use classes from a Java package, or the JUNG2 package or library provided, simply click **Build Tools** and click **Set Package Imports**. Look through the lists and check off the packages you wish to include and it can be used in your code.

For example, if the `java.util` package is imported any class within the `util` package can be used, so the following code will compile and run:

    ArrayList<Integer> list = new ArrayList<>();

    list.add(1);
    list.add(2);

    for (Integer i : list) {
        System.out.print(i);
    }

And the build log will display: `12`. Any imports added to the script will be saved along with the script so you do not have to re-select the packages when you load your work.

___

###Command Line Arguments

Command line arguments can be set to adjust the JVM run-time environment or as other parameters for the script being created. Simply go to **Build Tools** and choose **Set Command Line Arguments**, and set the arguments as needed.

---

# Loading Graphs

To load a graph stored in the *trivial graph format* use the following syntax format:
```java
// Create the graph loader
TGFLoader tgfl = new TGFLoader();

// Call the method to load the graph, returns the graph type as specified by the integer argument
// and the boolean tells the parser if the graph should be directed or not.
Graph<Integer, Edge> graph = tgfl.loadTrivialGraphFile(0, false);

// Use the graph as desired
System.out.println(graph.getVertexCount());
```
To load a graph using the *GraphML* format use the following format:

```java
// Create the GraphML parser, and have it return the graph, type specified by the integer argument 
GraphMLReader graphBuilder = new GraphMLReader();
Graph<Vertex, Edge> graph = graphBuilder.loadGraphFile(0);
```
**Important**

Using either loader, the JUNG2 Graph class, and Edge class are members of external packages that must be imported into the script. Use the Build Tools to select the packages: 

`core.components` for the Edge class and `algorithms.graphloader` from the provided library

`edu.uci.ics.jung.graph` from JUNG2 packages for the Graph class

---

#Searching Graphs
The packaged library include some common graph search algorithms. 

### Example Breadth First Search

These searches are implementations of an uniformed search. There are two methods that can be used to run BFS/DFS, *search(graph, root)* or *visualizeSearch(graph, root)*. Below is a simple example to illustrate how you can apply BFS to a graph.

```java
GraphMLReader gml = new GraphMLReader();
Graph<Vertex, Edge> graph = gml.loadGraphFile(0); // Load as default graph type, sparse graph.

BreadthFirstSearch bfs = new BreadthFirstSearch();
Vertex root = graph.getVertices().iterator().next(); // Pick first vertex iterator points to as root
List<Vertex> results = bfs.search(graph, root);
```
