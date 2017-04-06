import java.awt.Point;
import java.security.Policy;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class MtStHelensExp_912309925_999230758 implements AIModule
{
    public class Tile{
        public TerrainMap map;
        public Point point;
        public double g; // cost so far, from START Tile
        public double h; // cost to reach the END Tile
        public double f; // total cost f = g + h
        public Point parent;

        public Tile(TerrainMap map, Point p, double costSoFar, Point parent){
            this.map = map;
            this.point = p;
            this.g = costSoFar;
            this.h = getHeuristic(map, map.getEndPoint());//EuclideanDistance(map, map.getEndPoint());
            this.f = this.g + this.h;
            this.parent = parent;
        }

        public double EuclideanDistance(TerrainMap map, Point p){
            /*return Math.sqrt(Math.pow(2,Math.abs(this.point.getX()-p.getX()))+Math.pow(2,Math.abs(this.point.getY()-p.getY()))+
                    Math.pow(2,Math.abs(map.getTile(this.point)-map.getTile(p))));*/
            double dx;
            dx = Math.abs(this.point.getX() - p.getX());
            double dy = Math.abs(this.point.getY() - p.getY());
            return map.getCost(this.point, p) * (dx + dy);
        }
        private double getHeuristic(final TerrainMap map, Point p){
            return Math.max(Math.abs(this.point.getX()-p.getX()),Math.abs(this.point.getY()-p.getY()))*0.5;
        }
        /*private double getHeuristic(final TerrainMap map, Point p) {
            double dx = Math.abs(this.point.getX() - p.getX());
            double dy = Math.abs(this.point.getY() - p.getY());
            double big = 10;
            double small = Math.sqrt(14);

	    if(this.point.getY() > p.getY()){
		    dy = p.getY();
	    }

	    if (dx > dy) {
               return big*dy + small*(dx-dy);
            }
	    else {
               return big*dx + small*(dy-dx);
            }
        }*/

        public boolean isEqual(Point p){
            return this.point.equals(p);
        }

        public double MidPoint(TerrainMap map, Point p){
            double dx;
            dx = Math.abs(this.point.getX() - p.getX());
            double dy = Math.abs(this.point.getY() - p.getY());
            return Math.sqrt(dx * dx + dy * dy) / 2;
        }
    }


    public List<Point> createPath(final TerrainMap map) {
        ArrayList<Point> path = new ArrayList<Point>();
        Stack<Point> stackPath = new Stack<Point>();
        final Point StartPoint = map.getStartPoint();
        HashMap<Point, Tile> openList = new HashMap<Point, Tile>(); // open list
        HashMap<Point, Tile> isVisited = new HashMap<Point, Tile>(); // closed list

        Queue<Tile> fringe = new PriorityQueue<Tile>(
                new Comparator<Tile>() {
                    @Override
                    public int compare(Tile t1, Tile t2) {
                        if(t1.f > t2.f)
                            return 1;
                        else if (t1.f < t2.f)
                            return -1;
                        else
                            return 0;
                    }
                }
        ); // open list
        Tile startTile = new Tile(map, StartPoint, 0.0, null);

        fringe.add(startTile); // put in the frontier
        openList.put(StartPoint, startTile);
        Tile currentTile = startTile;

        while(!currentTile.isEqual(map.getEndPoint())) {
            while(true) {
                currentTile = fringe.poll(); // grab current tile worked on
                if (!isVisited.containsKey(currentTile.point)) // check if it is visited
                    break;
            }
            openList.remove(currentTile.point);  // neighbors not yet visited
            isVisited.put(currentTile.point, currentTile); // tiles visited

            //
            for(Point neighbors : map.getNeighbors(currentTile.point)) {
                Tile neighbor = new Tile(map, neighbors, currentTile.g+map.getCost(currentTile.point, neighbors)
                        ,currentTile.point);
                if (isVisited.containsKey(neighbor.point))
                    continue;
                else if(openList.containsKey(neighbor.point)){
                    Tile temp = openList.get(neighbor.point);
                    if(temp.g > neighbor.g){
                        temp = neighbor;
                        fringe.add(neighbor);
                    }
                }
                // neighbor not visited and not in priority queue
                else if( (!(openList.containsKey(neighbor.point))) && (!(isVisited.containsKey(neighbor.point)))){
                    fringe.add(neighbor);
                    openList.put(neighbor.point, neighbor);
                }
            }
        }

        while(currentTile.parent != null) {
            stackPath.add(currentTile.point);
            currentTile.point = currentTile.parent;
            currentTile.parent = isVisited.get(currentTile.point).parent;
        }

        stackPath.add(map.getStartPoint());

        path = new ArrayList<Point>(stackPath);
        List<?> shallowCopy = path.subList(0,path.size());
        Collections.reverse(shallowCopy);

        return path;
    }
}
