Cell Society
====

This project implements a cellular automata simulator.

Names:
- Kathleen Chen
- Jessica Yang 
- Juhyoung Lee
- Kenneth Moore III

### Timeline

Start Date: 2/6

Finish Date: 2/22

Hours Spent: 160 hours (40hrs * 4)

### Primary Roles
- Kathleen: GUI and view (Main, Control, TriangleGrid, ScreenControl, RectangleGrid, GridBuilder, ButtonBuilder, all files in resources)
- Jessica: Model (Control, ScreenControl, HexagonGrid, Cell, WaTorCell, SugarScapeCell, SegregationCell, RPSCell, PercolationCell, GameOfLifeCell, ForagingAntsCell, FireCell, BylsLoopCell) 
- Juhyoung: Model (GridHelper, Grid, Cell, WaTorGrid, SugarScapeGrid, SegregationGrid, RPSGrid, PercolationGrid, GameOfLifeGrid, ForagingAntsGrid, FireGrid, BylsLoopGrid)
- Kenneth: Configuration (XMLException, Simulation, CreateFile, test XML files, Control)

### Resources Used
- https://fab.cba.mit.edu/classes/865.18/replication/Byl.pdf
- https://www2.cs.duke.edu/courses/compsci308/current/assign/02_simulation/nifty/shiflet-fire/
- https://greenteapress.com/complexity/html/thinkcomplexity013.html
- https://en.wikipedia.org/wiki/Conway's_Game_of_Life
- https://www2.cs.duke.edu/courses/compsci308/current/assign/02_simulation/PercolationCA.pdf
- https://softologyblog.wordpress.com/2018/03/23/rock-paper-scissors-cellular-automata/
- https://www2.cs.duke.edu/courses/compsci308/current/assign/02_simulation/nifty/mccown-schelling-model-segregation/
- https://www2.cs.duke.edu/courses/compsci308/current/assign/02_simulation/Sugarscape_Leicester.pdf
- https://beltoforion.de/en/wator/
- https://www.w3schools.com/colors/colors_picker.asp
- https://stackoverflow.com/questions/1519736/random-shuffling-of-an-array  
- Oracle Java documentation
- Google Translate
- https://www.w3schools.com/java/java_files_create.asp
- https://www.tutorialspoint.com/java/lang/system_currenttimemillis.htm

### Running the Program

Main class: Main.java

Data files needed: XMLs configuration file for the specific simulation to run.

Features implemented: 
- Model: 
    - Grid of cells that represent the simulation:
        - Edge cells have smaller partial neighborhoods than middle cells
        - Different arrangements of neighbors
        - Square, triangular, and hexagonal cell shapes
        - Finite, toroidal, and infinite edges
            - Infinite grid works, but can kill your computer if you expand too much
    - Simulation algorithm:
        - State information of each cell is updated each step based on rules applied to each cell's state
        - Can run indefinitely
    - Simulations implemented:
        - Game of Life
        - Percolation
        - Segregation
        - WaTor
        - Fire
        - Rock, Paper, Scissors
        - Foraging Ants
        - Byl's Loop
        - SugarScape (preset 1 and 2)
- Configuration:
    - Reads in XML that contains initial settings for the simulations (type, title, author, description, initial parameters, width and height of grid, initial cell states)
    - XMLs: __ total; 4 per simulation that produces known results; 2 per simulation with edge active cells that produces known results
    - Incorrect file data error checking: required value not given, invalid value given, invalid cell state values given, cell locations outside grid size bounds, badly formatted XMLs
        - Missing simulation parameters are provided default values instead of throwing an error
    - Save current state of simulation as an XML config
    - Initial configuration of cell states set with specific locations and cell states
    - Customizable language, grid type (shape, neighbor, edge), color themes
- Visualization:
    - Displays current states of grid and animate the simulation until the user stops it
    - Uploads a new config file which starts a new simulation
    - Buttons that stop, pause, speed up, slow down, and step through the simulation
    - Graph that shows the population of all the types of cells while the simulation runs
    - Multiple simulations can be run at the same time on different windows independently
    - Editing simulation parameters for a new simulation of the same type to run
    - Change GUI appearance while the program is running between three different options
    - Change language used for the text displayed
    
### Notes/Assumptions

Assumptions or Simplifications:
- All the simulations used rules as described in the prepareNewState javadoc of each Cell
    - ForagingAnts will have the HOME cell in the top left corner of the grid
    - Byl's Loop will only work with a specific set of initial configurations, and with square tesselations with 4 neighbors
    - Implemented SugarScape models are presets 1 and 2
    - SugarScape cell colors don't change based on the patchSugar value
    - SugarScape agent vision limited to max number of neighbors
- View Assumptions/Notes
    - Grid would pass in an Integer List of cells to the ScreenControl class
    - The add simulation button would simply create a new Control() which would create a new screen
    - All information passed to ScreenControl from the grid and xml parsing are valid (does not check for exceptions in front end stuff)
    - Screen size is hard coded and cannot be changed by the user
- XML
    - neighborhoodSize and shape need to be declared at the same time within the XML
    - User will follow our XML guidelines:
        - User manually inputs the states, and it is not autogenerated from a random seed
    

Interesting data files:
- Bylsloop
    - When we first saw this simulation, we were pretty overwhelmed, but now that it works, it's very cool and the outcome is interesting and different from the other simulations
- WaTor
    - It's interesting how much of a balance is required in nature for fish or sharks not to over populate the ocean

Known Bugs:
- Graph only appears on next step of simulation (if simulation has started before clicking view graph, the graph will be displayed on the next step/update)
- Foraging ant simulation: occasionally an ant escapes and does not make its way back home
- When screen size expands, the title of the graph overlaps itself because it does not delete itself before redrawing
- For foraging ants (hexagon grid), if the home starts in the corner, the ants will appear on both corners
- If you do an infinite grid, you should close your window and then pull up the infinite file (doesn't run infinite grid properly if it is a later simulation)

Extra credit: N/A

### Impressions
- Interesting working in a group on a coding project. 
- For half of us (Kenny and Kathleen), this was the first collaborative coding project we had worked on.
- Navigating git was a struggle at the beginning, but this project taught us how to communicate and deal with any merge conflicts.
- Project seemed daunting when first handed out, but after planning and splitting the work/specializing, the project was not as terrible as we imagined.
- We've learned how to debug each other's code (and work with different coding styles) through pair programming
- We've learned how to work around each other's schedules and that we work very well just sitting on a Zoom together and being there for each other (even if we are just silent)

