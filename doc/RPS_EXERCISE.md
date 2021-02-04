# Simulation Lab Discussion

## Rock Paper Scissors

## Names and NetIDs
Jessica Yang (jqy2)
Kenny Moore (km460)
Juhyoung Lee (jl840)
Kathleen Chen (kc387)

### High Level Design Ideas
- Player class that interacts with classes for the possible moves 
- Judge class that decides who wins a round of the game
- Weapon class for possible moves: will be inherited from for specific move objects

### CRC Card Classes

This class's purpose is to actively play the game:
```java
 public class Player {
  public Move play();
  public void won();
  public void resetScore();
 }
```

This class's purpose is to run the game:
```java
public class Judge {
  public void playRound(Collection<Player>);
  public Player currentWinner(Collection<Player>);
  public void resetGame(Collection<Player>);
}
```

This class's purpose or value is to be useful:
```java
 public class Weapon {
  public String decideWeapon();
  public String validateWeapon(String choice);
  public boolean decideWinner(Weapon weapon);
 }
```

### Use Cases

 * A new game is started with five players, their scores are reset to 0.
 ```java
  Judge myJudge = new Judge();
  myJudge.resetGame(allPlayers);
 ```

 * A player chooses his RPS "weapon" with which he wants to play for this round.
 ```java
  Weapon myWeapon = new Weapon();
  myWeapon.validateWeapon("input")
 ```

 * Given three players' choices, one player wins the round, and their scores are updated.
 ```java
  Judge myJudge = new Judge();
  Collection allPlayers.addAll(player1, player2, player3);
  myJudge.playRound(allPlayers);
 ```

 * A new choice is added to an existing game and its relationship to all the other choices is 
   updated.
 ```java
  Something thing = new Something();
  Value v = thing.getValue();
  v.update(13);
 ```

 * A new game is added to the system, with its own relationships for its all its "weapons".
 ```java
 Something thing = new Something();
 Value v = thing.getValue();
 v.update(13);
 ```

