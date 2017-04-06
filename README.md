# AStar
Implements an AStar algorithm to the find the shortest path between 2 points on a 3D grid of Mount Helens
To run: 
java Main AStarDiv_912309925_999230758

```
public double getCost(final Point p1, final Point p2)
{
  return Math.pow(2.0, (getTile(p2) - getTile(p1)));
  }
```
java Main AStarExp_912309925_999230758

```
 // Bizzaro world cost function
 public double getCost(final Point p1, final Point p2)
 {
      return 1.0*(getTile(p1) / (getTile(p2) + 1));
 }
```
To run on the Mount Helens grid:

java Main <AIModule> -load MTAFT.XYZ 



