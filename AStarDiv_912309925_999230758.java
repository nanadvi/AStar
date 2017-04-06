import java.awt.Point;
import java.security.Policy;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class AStarDiv_912309925_999230758 implements AIModule
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
            this.h = getHeuristic(map, map.getEndPoint());
            this.f = this.g + this.h;
            this.parent = parent;
        }

        public double getHeuristic(TerrainMap map, Point p) {
            double result = 0;

            double dx = Math.abs(this.point.getX() - p.getX());
            double dy = Math.abs(this.point.getY() - p.getY());
            double height = map.getTile(this.point);
            double goal_height = map.getTile(p);

            double costDiag  = map.getCost(this.point, map.getEndPoint());

            double costStraight = 1;
            if (dx > dy){
                result =  costStraight * dy + Math.sqrt(costDiag/costStraight) * (dx - dy);
            }
            else{
                result = costStraight * dx + Math.sqrt(costDiag/costStraight) * (dy - dx);
            }


//            D * (dx + dy) + (D2 - 2 * D) * min(dx, dy)
//            result = (dx+dy) + (Math.sqrt(2)-2)*Math.min(dx, dy);

//            if(height > goal_height) {
//
//                result = Math.max(dx, dy)+ costDiag;
//            }
//            else if(goal_height < height){
//
//                double temp = Math.max(dx, dy);
//                double dh = height - goal_height;
//                double temp2 = dh / temp; // incrementing the slope
//                result = Math.exp(temp2) * temp;
//            }
//            else
//                result = Math.max(dx, dy);
            return result;
        }
        public boolean isEqual(Point p){
            return this.point.equals(p);
        }

    }

    public List<Point> createPath(final TerrainMap map){
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


        while(!currentTile.isEqual(map.getEndPoint())){
            while(true) {
                currentTile = fringe.poll();
                if (!isVisited.containsKey(currentTile.point))
                    break;
            }
            openList.remove(currentTile.point);
            isVisited.put(currentTile.point, currentTile);


            for(Point neighbors : map.getNeighbors(currentTile.point)){
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
                else if( (!(openList.containsKey(neighbor.point))) && (!(isVisited.containsKey(neighbor.point)))){
                    fringe.add(neighbor);
                    openList.put(neighbor.point, neighbor);
                }
            }
        }
        while(currentTile.parent != null){
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
