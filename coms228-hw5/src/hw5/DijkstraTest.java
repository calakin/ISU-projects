package hw5;

import java.io.File;

public class DijkstraTest {
	public static void main(String[] args){

	    try {
	        Graph g = MazeSolver.build_graph(args[0]);
	        System.out.println("A graph representing a maze");
	        System.out.println(g);

	        Graph.Vertex source = MazeSolver.get_endVertex(g, true);
	        Graph.Vertex last = MazeSolver.get_endVertex(g, false);
	        String path = Dijkstra.Dijkstra(g, source, last);
	        System.out.println("A shortest path in the maze");
	        if ( path != null )
	          System.out.println( path );
	        else
	          System.out.println( "No path exists" );
	        }
	        catch (Exception e) {
	          e.printStackTrace();
	        }
	}
}
