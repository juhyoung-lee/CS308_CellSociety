# Cell Society Design Plan
### Team Number
### Names

#### Examples

Here is a graphical look at my design:

![This is cool, too bad you can't see it](online-shopping-uml-example.png "An initial UI")

made from [a tool that generates UML from existing code](http://staruml.io/).


Here is our amazing UI:

![This is cool, too bad you can't see it](29-sketched-ui-wireframe.jpg "An alternate design")

taken from [Brilliant Examples of Sketched UI Wireframes and Mock-Ups](https://onextrapixel.com/40-brilliant-examples-of-sketched-ui-wireframes-and-mock-ups/).

## Introduction
This section describes the problem your team is trying to solve by writing this program, the primary design goals of the project (i.e., where is it most flexible), and the primary architecture of the design (i.e., what is closed and what is open). This section should discuss the program at a high-level (i.e., without referencing specific classes, data structures, or code).

## Overview
This section serves as a map of your design for other programmers to gain a general understanding of how and why the program was divided up, and how the individual parts work together to provide the desired functionality. As such, it should describe specific components you intend to create, their purpose with regards to the program's functionality, and how they collaborate with each other. It should also include a picture of how the components are related (these pictures can be hand drawn and scanned in CRC cards, created with a standard drawing program or on a white board, or screen shots from a UML design program). To keep these classes as flexible as possible, your team must describe two different implementations (i.e., data structures, file formats, etc.) and then design your method signatures so they do not reveal the specifics of either implementation option. This section should discuss specific classes, methods, and data structures, but not individual lines of code.  

**Picture**  
yEET.jpg

**Implementation**
1. neighbors are stored as arraylist
2. neighbors are stored as linked list

both are abstracted away in checkNeighbors()

**CRC Cards**
```Java
/**
 * configuration in constructor
 * then simulation but cell does the heavy lifting
 * 
 * super class for different implementations of the game
 * contain general rule sets
 */
public class CellAutomata {
    /**
     * calls cell update
     */
    public void step();
    
    private void readXML(String file);
    
}
```

```Java
import javafx.scene.shape.Shape;

/**
 * represent a general cell
 *
 * keep track of state, color (gui stuff), neighbors
 *
 * contain update rules
 */
public abstract class Cell extends Shape {
    /**
     * switch to new state
     */
    public void updateCell();

    /**
     * calculate next state
     */
    public void prepareState();

    /**
     * check its neighbor states
     */
    private void checkNeighbors();
}
```

```Java
/**
 * grid of cells
 */
public class Grid {
    //getNeighbors(Cell currentCell); ??
}
```

```Java
/**
 * main class that connects simulation to visualization
 */
public class Game{
    /**
     * read xml and create cell automata
     * creates grid
     * visualizes
     * step method will update cell automata
     * visualize
     * repeat past two steps
     */
    public static void main(String[] args);

    /**
     * handles input
     * may be refactored later?
     */
    public void handleEvent();

    /**
     * deals with GUI
     * may be multiple methods
     * may be refactored to class
     */
    public void setUpGUI();

    /**
     * pause, speed up, stuff for gui
     * multiple methods
     * refactored with setUpGUI()
     */
    public void doGUI();
}
```

## User Interface
This section describes how the user will interact with your program (keep it very simple to start). Describe the overall appearance of program's user interface components and how users interact with these components (especially those specific to your program, i.e., means of input other than menus or toolbars). Include one or more pictures of the user interface (these pictures can be hand drawn and scanned in, created with a standard drawing program, or screen shots from a dummy program that serves as a exemplar). Also describe any erroneous situations that are reported to the user (i.e., bad input data, empty data, etc.). This section should go into as much detail as necessary to cover all your team wants to say.

## Configuration File Format
Configuration files will use the standard XML format that can be parsed automatically by Java, but the exact tag names and attributes you use within this file are up to your team. Create at least two example files is to help the team agree on the information represented (such as number of tags, using attributes vs. text, and other internal formatting decisions). Note, your examples do not need to be exhaustive, just representative of the file's format.


## Design Details
This section describes each component introduced in the Overview in detail (as well as any other sub-components that may be needed but are not significant to include in a high-level description of the program). Describe how each component handles specific features given in the assignment specification, what resources it might use, how it collaborates with other components, and how each could be extended to include additional requirements (from the assignment specification or discussed by your team). Justify the decision to create each component with respect to the design's key goals, principles, and abstractions. This section should go into as much detail as necessary to cover all your team wants to say.

## Use Cases
In addition to the Use Cases below, write at least 2 use cases per person on the team to further test your design (they should be based on features given in the assignment specification, but should include more details to make them concrete to a specific scenario). Then, include the steps needed to complete all the Use Cases to help make your design plan more concrete.


## Design Considerations
This section describes any issues which need to be addressed or resolved before attempting to devise a complete design solution. Include any design decisions that the group discussed at length and describe at least one alternative in detail (including pros and cons from all sides of the discussion). Describe any assumptions or dependencies regarding the program that impact the overall design. This section should go into as much detail as necessary to cover all your team wants to say.


## Team Responsibilities
This section describes the program components each team member plans to take primary and secondary responsibility for and a high-level plan of how the team will complete the program.

 * Team Member #1

 * Team Member #2

 * Team Member #3
