package hw5;

import java.util.ArrayList;

/**
 * @author
 */

import java.util.HashMap;
import java.util.HashSet;

import hw5.Graph.Vertex;

public class Dijkstra {

    /**
     * First, computes a shortest path from a source vertex to
     * a destination vertex in a graph by using Dijkstra's algorithm.
     * Second, visits and saves (in a stack) each vertex in the path,
     * in reverse order starting from the destination vertex,
     * by using the map object pred.
     * Third, uses a StringBuilder object to generate the return String
     * object by popping up the vertices from the stack;
     * the vertices in the String object are in the right order.
     * Note that the get_index() method is called from a Graph.Vertex object
     * to get its oringinal integer name.
     *
     * @param G
     *          - The graph in which a shortest path is to be computed
     * @param source
     *          - The first vertex of the shortest path
     * @param dest
     *          - The last vertex of the shortest path
     * @return a String object with three lines (separated by a newline character)
     *         such that line 1 shows the length of the shortest path,
     *         line 2 shows the cost of the path,
     *         and line 3 gives a list of the vertices (in the path)
     *         with a space between adjacent vertices.
     *
     *         The contents of an example String object:
     *         Path Length: 5
     *         Path Cost: 4
     *         Path: 0 4 2 5 7 9
     *
     * @throws NullPointerException
     *           - If any argument is null
     *
     * @throws RuntimeException
     *           - If the given source or dest vertex is not in the graph
     *
     */
    public static String Dijkstra(Graph G, Graph.Vertex source, Graph.Vertex dest)
    {	
    	ArrayList<Graph.Vertex> vertexList = (ArrayList<Vertex>) G.get_vertices();
    	Graph.Vertex start = source;
    	Graph.Vertex end = dest;
    	
    	//create collection of distances from start for each vertex, initially setting all but the start to infinity/max_value
    	HashMap<Graph.Vertex, Integer> distances = new HashMap<Graph.Vertex, Integer>();
    	for(int i = 0; i < vertexList.size(); i++){
    		if(vertexList.get(i) != null){
    			distances.put(vertexList.get(i), Integer.MAX_VALUE);
    		}
    	}
    	distances.put(start, 0);
    	
    	//Stores vertices visited and their predecessors, initially containing only the start
    	HashMap<Graph.Vertex, Graph.Vertex> visited = new HashMap();
    	visited.put(start, null);    	

    	//create a priority queue for holding the possible paths (represented by Vpairs)
    	Heap<VpairWithPredecessor> edges = new Heap();
    	
    	//calculate distance to neighbors of current vertex
    	Graph.Vertex currentVertex = start;
    	Graph.Vertex previousVertex;
    	Iterable<Graph.Edge> currentVertexEdges = start.get_edges();
    	while(currentVertex != end){
    		//updates head with currentVertex edges
    		for(Graph.Edge edge: currentVertexEdges){
    			if((edge.get_weight()+distances.get(edge.from)) < distances.get(edge.to)){
    				Vpair<Graph.Vertex, Integer> vpair= new Vpair(edge.to,edge.get_weight()+distances.get(currentVertex));
    				edges.add(new VpairWithPredecessor(vpair, edge.from));
    				distances.put(edge.to, edge.get_weight()+distances.get(edge.from));
    			}
    		}
    		
    		if(!edges.isEmpty()){
    			//travels lowest edge to the next vertex (if it has not been visited)
    			VpairWithPredecessor lowestCost;
    			while(visited.containsKey(edges.getMin().getVpair().getVertex())){
    				edges.removeMin();
    			}
    			lowestCost = edges.getMin();
    			//updates previous vertex
    			previousVertex = lowestCost.getPredecessor();							
    			//updates current vertex
    			currentVertex = (Graph.Vertex)lowestCost.getVpair().getVertex();
    			//updates visited to include current vertex, with previous vertex as predecessor/value
    			visited.put(currentVertex, previousVertex);
    			//updates distances to the currentVertex
    			distances.put(currentVertex, (Integer)lowestCost.getVpair().getCost());
    			//updates edges of the current Vertex
    			currentVertexEdges = currentVertex.get_edges();
    			//removes used edge from heap
    			edges.removeMin();
    			//System.out.println(currentVertex.get_index());
    		} else {
    			break;
    		}
    	}

    	//add path of vertices from end to start
    	LinkedStack stack = new LinkedStack();
		stack.push(end);
		Graph.Vertex predecessor = visited.get(end);
		int length = 0;
    	while(predecessor != null){
    		length++;
    		stack.push(predecessor);
    		predecessor = visited.get(predecessor);
    	}

    	//use StringBuilder to generate string from the stack
    	StringBuilder builder = new StringBuilder();
    	while(!stack.isEmpty()){
    		Graph.Vertex vertex = (Vertex)stack.pop();
    		if(!(stack.size()==0)){
    			builder.append(vertex.get_index());
    			builder.append(" ");
    		} else{
    			builder.append(vertex.get_index());
    		}
    	}
    	
    	//Put the result in the right format
    	int cost = distances.get(end);
    	String path = builder.toString();

    	String result = "Path Length: " + length + "\n" + 
    					"Path Cost: " + cost + "\n" +
    					"Path: " + path;
    	//return string
    	if(length == 0) return "No path exists"; 	return result;
    }

/** 
 * A class containing a Vpair, along with the vertex from which the Vpair travels.
 */    
private static class VpairWithPredecessor implements Comparable<VpairWithPredecessor>{
	private Vpair vpair;
	private Graph.Vertex predecessor;
	
	VpairWithPredecessor(Vpair vpair, Graph.Vertex predecessor){
		this.vpair = vpair;
		this.predecessor = predecessor;
	}
	
	public Vpair getVpair() {return this.vpair;}
	public Graph.Vertex getPredecessor() {return this.predecessor;}
	
    public int compareTo( VpairWithPredecessor other )
    {
      return vpair.getCost().compareTo(other.getVpair().getCost() );
    }
    
    public String toString()
    {
      return "<" +  vpair.getVertex().toString() + ", " + vpair.getVertex().toString() + ">";
    }

    public int hashCode()
    {
      return vpair.getVertex().hashCode();
    }

    public boolean equals(Object obj)
    {
      if(this == obj) return true;
      if((obj == null) || (obj.getClass() != this.getClass()))
       return false;
      // object must be Vpair at this point
      Vpair<?, ?> test = (Vpair<?, ?>)obj;
      return
        (vpair.getVertex() == test.node || (vpair.getVertex() != null && vpair.getVertex().equals(test.node)));
    }
}

/**
 * A pair class with two components of types V and C, where
 * V is a vertex type and C is a cost type.
 */
private static class Vpair<V, C extends Comparable<? super C> > implements Comparable<Vpair<V, C>>
{
     private V  node;
     private C  cost;

     Vpair(V n, C c)
     {
       node = n;
       cost = c;
     }

     public V getVertex() { return node;}
     public C getCost() { return cost;}
     public int compareTo( Vpair<V, C> other )
     {
       return cost.compareTo(other.getCost() );
     }

     public String toString()
     {
       return "<" +  node.toString() + ", " + cost.toString() + ">";
     }

     public int hashCode()
     {
       return node.hashCode();
     }

     public boolean equals(Object obj)
     {
       if(this == obj) return true;
       if((obj == null) || (obj.getClass() != this.getClass()))
        return false;
       // object must be Vpair at this point
       Vpair<?, ?> test = (Vpair<?, ?>)obj;
       return
         (node == test.node || (node != null && node.equals(test.node)));
     }
}

}
