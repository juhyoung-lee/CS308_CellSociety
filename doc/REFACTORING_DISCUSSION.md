## Refactoring Lab Discussion
### Team 11
### Names
Juhyoung Lee, Jessica Yang, Kenneth Moore III, Kathleen Chen


### Issues in Current Code
Declarations should use Java collection interfaces  
Useless imports should be removed  
Classes should not be empty  
Methods should not have too many parameters  
Boolean expression should not be gratuitous
Inter-file Duplication

#### Method or Class
* Configure, Simulation and XMLParser might be combined to make active classes

### Refactoring Plan

* What are the code's biggest issues?  
    * Not declaring using interfaces.
  
* Which issues are easy to fix and which are hard?  
    * Switching declarations to interfaces was easy.
    * It's harder to fix no empty classes because we haven't implemented them yet.
    * Inter-file duplication was also harder.

* What are good ways to implement the changes "in place"?
    * Extracting methods is a good strategy.


### Refactoring Work

* Issue chosen: Fix and Alternatives
    * We redeclared all HashMaps and ArrayLists as Maps and Lists

* Issue chosen: Fix and Alternatives
    * Missing javadocs will be added later