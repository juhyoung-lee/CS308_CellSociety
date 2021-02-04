# Simulation Lab Discussion

## Breakout with Inheritance

## Names and NetIDs
Jessica Yang (jqy2)  
Kenny Moore (km460)  
Juhyoung Lee (jl840)  
Kathleen Chen (kc387)

### Block

This superclass's purpose as an abstraction: lays out qualities every brick has
```java
 public class Block extends Rectangle{
     public int something ();
     // no implementation, just a comment about its purpose in the abstraction 
 }
```

#### Subclasses (the Open part)

This subclass's high-level behavorial differences from the superclass: moves
```java
 public class MovingBrick extends Block {
     public int something ();
     // no implementation, just a comment about what it does differently 
 }
```

#### Affect on Game/Level class (the Closed part)
Cleaner, easier to read code, allows to hide exact brick type from other code


### Power-up

This superclass's purpose as an abstraction: same qualities across all drops
```java
 public class Drop {
     public int something ();
     // no implementation, just a comment about its purpose in the abstraction 
 }
```

#### Subclasses (the Open part)

This subclass's high-level behavorial differences from the superclass:
```java
 public class Coin extends Drop {
     public int something ();
     // no implementation, just a comment about what it does differently 
 }
```

#### Affect on Game/Level class (the Closed part)
Allows for a lot of drops to be handled and displayed without specifying exact objects.   
Used arraylist of drops. Paddle had intersectDrops method.


### Level

This superclass's purpose as an abstraction: allows for different types of screens to be easily displayed
```java
 public class Screen {
     public int something ();
     // no implementation, just a comment about its purpose in the abstraction 
 }
```

#### Subclasses (the Open part)

This subclass's high-level behavorial differences from the superclass: a type of screen that displays text
```java
 public class TextScreen extends Screen {
     public int something ();
     // no implementation, just a comment about what it does differently 
 }
```

#### Affect on Game class (the Closed part)
Allows for a single arraylist of screens/levels to be displayed despite each element being different types of screens 


### Others?
One member had a sprite class that any object on the screen could extend.
