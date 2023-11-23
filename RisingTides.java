package tides;

import java.util.*;

/**
 * This class contains methods that provide information about select terrains 
 * using 2D arrays. Uses floodfill to flood given maps and uses that 
 * information to understand the potential impacts. 
 * Instance Variables:
 *  - a double array for all the heights for each cell
 *  - a GridLocation array for the sources of water on empty terrain 
 * 
 * @author Original Creator Keith Scharz (NIFTY STANFORD) 
 * @author Vian Miranda (Rutgers University)
 */
public class RisingTides {

    // Instance variables
    private double[][] terrain;     // an array for all the heights for each cell
    private GridLocation[] sources; // an array for the sources of water on empty terrain 

    /**
     * DO NOT EDIT!
     * Constructor for RisingTides.
     * @param terrain passes in the selected terrain 
     */
    public RisingTides(Terrain terrain) {
        this.terrain = terrain.heights;
        this.sources = terrain.sources;
    }

    /**
     * Find the lowest and highest point of the terrain and output it.
     * 
     * @return double[][], with index 0 and index 1 being the lowest and 
     * highest points of the terrain, respectively
     */
    public double[] elevationExtrema() {
    /* WRITE YOUR CODE BELOW */
    double[] elevationExtrema = new double[2];
    double highest=terrain[0][0];
    double lowest=terrain[0][0];
    for (int i=0;i<terrain.length;i++){
    for (int f=0;f<terrain[0].length;f++){
        if (terrain[i][f]>highest) {
           highest=terrain[i][f];
        }
    }
}
 for (int s = 0; s < terrain.length; s++){
    for (int n = 0; n < terrain[0].length; n++ ){
   
       
        if (terrain[s][n]<lowest) {
            lowest = terrain [s][n];
        }
    }
}


     elevationExtrema[0] = lowest;
     elevationExtrema[1] = highest;
   
     return elevationExtrema;
}


    
    /**
     * Implement the floodfill algorithm using the provided terrain and sources.
     * 
     * All water originates from the source GridLocation. If the height of the 
     * water is greater than that of the neighboring terrain, flood the cells. 
     * Repeat iteratively till the neighboring terrain is higher than the water 
     * height.
     * 
     * 
     * @param height of the water
     * @return boolean[][], where flooded cells are true, otherwise false
     */
    public boolean[][] floodedRegionsIn(double height) {
        int rows = terrain.length;
        int cols = terrain[0].length;
        boolean[][] floodedRegionsIn = new boolean[rows][cols];
        
        // Initialize with false
        for (int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                floodedRegionsIn[i][j] = false;
            }
        }
    
        ArrayList<GridLocation> locationsToProcess = new ArrayList<GridLocation>();
        for(GridLocation source : sources) {
        floodedRegionsIn[source.row][source.col] = true; 
        locationsToProcess.add(source);
        }
    
        int[][] directions = {{0,1}, {1,0}, {0,-1}, {-1,0}}; // 4 directions (up,down,left,right)
        while (!locationsToProcess.isEmpty()) {
            GridLocation current = locationsToProcess.remove(0);
            for (int[] direction : directions) {
                int newRow = current.row+direction[0];  
                int newCol = current.col+direction[1];
                if(newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols && !floodedRegionsIn[newRow][newCol]&&terrain[newRow][newCol]<=height) {
                    floodedRegionsIn[newRow][newCol] = true;
                    locationsToProcess.add(new GridLocation(newRow, newCol));
                }
            }
        }
        return floodedRegionsIn;
    }
    
    
            
        


    

    public boolean isFlooded(double height, GridLocation cell) {
        
        /* WRITE YOUR CODE BELOW */
     boolean[][] floodedRegions = floodedRegionsIn(height);
    
      return floodedRegions[cell.row][cell.col];
  
    }

    /**
     * Given the water height and a GridLocation find the difference between 
     * the chosen cells height and the water height.
     * 
     * If the return value is negative, the Driver will display "meters below"
     * If the return value is positive, the Driver will display "meters above"
     * The value displayed will be positive.
     * 
     * @param height of the water
     * @param cell location
     * @return double, representing how high/deep a cell is above/below water
     */
   
     public double heightAboveWater(double height, GridLocation cell) {
     
        double Landsize = terrain[cell.row][cell.col];
    
     // subtract water height from land height 
        double difference = Landsize-height;
    
     
        return difference;
    }
    
    /**
     * Total land available (not underwater) given a certain water height.
     * 
     * @param height of the water
     * @return int, representing every cell above water
     */
    public int totalVisibleLand(double height) {
        
        boolean[][] flooded = floodedRegionsIn(height);
    
        int count = 0;
    
        
        for (int i = 0; i < flooded.length; i++) {
            for (int j = 0; j < flooded[i].length; j++) {
                // if the cell is not flooded increase the counter
                if (!flooded[i][j]) {
                    count++;
                }
            }
        }
    
        return count;
    }
    


    /**
     * Given 2 heights, find the difference in land available at each height. 
     * 
     * If the return value is negative, the Driver will display "Will gain"
     * If the return value is positive, the Driver will display "Will lose"
     * The value displayed will be positive.
     * 
     * @param height of the water
     * @param newHeight the future height of the water
     * @return int, representing the amount of land lost or gained
     */
    public int landLost(double height, double newHeight) {
        int initialLand = totalVisibleLand(height);
        int futureLand = totalVisibleLand(newHeight);
        return initialLand - futureLand;
        
        
    }

    /**
     * Count the total number of islands on the flooded terrain.
     * 
     * Parts of the terrain are considered "islands" if they are completely 
     * surround by water in all 8-directions. Should there be a direction (ie. 
     * left corner) where a certain piece of land is connected to another 
     * landmass, this should be considered as one island. A better example 
     * would be if there were two landmasses connected by one cell. Although 
     * seemingly two islands, after further inspection it should be realized 
     * this is one single island. Only if this connection were to be removed 
     * (height of water increased) should these two landmasses be considered 
     * two separate islands.
     * 
     * @param height of the water
     * @return int, representing the total number of islands
     */
    public int numOfIslands(double height) {
        int row = terrain.length; 
        int col = terrain[0].length;
        UnionFind pt = new UnionFind(row*col);
        boolean[][] floodPresent = floodedRegionsIn(height);
        for(int i = 0; i< row; i++){
            for(int j =0; j<col; j++){
                if(!floodPresent[i][j] ) {
                    for (int[] direction : new int[][]{{0,1}, {1,0}, {0,-1}, {-1,0}, {-1,-1}, {-1,1}, {1,-1}, {1,1}}) {
                        int newRow = i + direction[0]; 
                        int newCol = j + direction[1];
                        if (newRow>=0 && newRow < row && newCol>=0 && newCol<col && !floodPresent[newRow][newCol]) {
                            pt.union(i*col+j, newRow*col+newCol);

                        }
                    }
                }
            }
        }
        Set<Integer> islandRepresentatives = new HashSet<>();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (!floodPresent[i][j]) {
                    islandRepresentatives.add(pt.find(i *col+j));
                }
            }
        }
        return islandRepresentatives.size();
    }
    private class UnionFind {
        private int[] parent;
        private int[] size;
        public UnionFind(int n) {
        parent =new int[n];
        size =new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }
        public int find(int x) {
            while (x != parent[x]) {
                parent[x] = parent[parent[x]];  
                x = parent[x];
            }
            return x;
        }
        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX != rootY) {
                if(size[rootX] <size[rootY]) {
                    parent[rootX]=rootY;
                    size[rootY] += size[rootX];
                } else {
                    parent[rootY] =rootX;
                    size[rootX] +=size[rootY];
                }
            }
        }
    }
}
        
       
    

