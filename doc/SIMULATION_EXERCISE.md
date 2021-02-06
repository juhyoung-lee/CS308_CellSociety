# Simulation Lab Discussion

## Cell Society

## Names and NetIDs
Jessica Yang (jqy2)
Kenny Moore (km460)
Juhyoung Lee (jl840)
Kathleen Chen (kc387)

### High Level Design Ideas
- Cell class that updates itself based on its value and its neighbors
- Simulation class that keeps track of rules

### CRC Card Classes

This class's purpose or value is to manage something:
```java
public class Something {
     // sums the numbers in the given data
     public int getTotal (Collection<Integer> data)
	 // creates an order from the given data
     public Order makeOrder (String structuredData)
 }
```

This class's purpose or value is to organize the data from Something:
```java
public class Order {
     // updates the information based on new data 
     public void update (int data)
 }
```


### Use Cases

* Apply the rules to a middle cell: set the next state of a cell to dead by counting its number of neighbors using the Game of Life rules for a cell in the middle (i.e., with all its neighbors)
```java
Something thing = new Something();
Value v = thing.getValue();
v.update(13);
```

* Apply the rules to an edge cell: set the next state of a cell to live by counting its number of neighbors using the Game of Life rules for a cell on the edge (i.e., with some of its neighbors missing)
```java
Something thing = new Something();
Value v = thing.getValue();
v.update(13);
```

* Move to the next generation: update all cells in a simulation from their current state to their next state and display the result graphically
```java
Something thing = new Something();
Value v = thing.getValue();
v.update(13);
```

* Set a simulation parameter: set the value of a parameter, probCatch, for a simulation, Fire, based on the value given in an XML file
```java
Something thing = new Something();
Value v = thing.getValue();
v.update(13);
```

* Switch simulations: use the GUI to change the current simulation from Game of Life to Wa-Tor
```java
Something thing = new Something();
Value v = thing.getValue();
v.update(13);
```