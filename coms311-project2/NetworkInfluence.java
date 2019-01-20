// LEAVE THIS FILE IN THE DEFAULT PACKAGE
//  (i.e., DO NOT add 'package cs311.pa1;' or similar)

// DO NOT MODIFY THE EXISTING METHOD SIGNATURES
//  (you may, however, add member fields and additional methods)

// DO NOT INCLUDE LIBRARIES OUTSIDE OF THE JAVA STANDARD LIBRARY
//  (i.e., you may only include libraries of the form java.*)


import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 * 
 * @author Cody Lakin (calakin)
 * @author Logan Kinneer (lkinneer)
 *
 */
public class NetworkInfluence {
	ArrayList<Node> nodes = new ArrayList<Node>();
	ArrayList<Edge> edges = new ArrayList<Edge>();
	Hashtable<String, Node> hashtable = new Hashtable<String, Node>();
	int numberOfNodes;

	// NOTE: graphData is an absolute file path that contains graph data, NOT the
	// raw graph data itself
	public NetworkInfluence(String graphData) {
		List<String> lines = new ArrayList<String>();
		File file = new File(graphData);
		try {
			lines = Files.readAllLines(file.toPath(), Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
		}
		int track = 1;
		for (String line : lines) {
			String[] words;
			if (track == 1) {
				numberOfNodes = Integer.parseInt(line);
				track++;
			} else {
				words = line.split(" ");
				if (hashtable.get(words[0]) != null) {
					// add the node that may or may not exist
					if (hashtable.get(words[1]) != null) {
						hashtable.get(words[0]).addNode(hashtable.get(words[1]));
					} else {
						Node node = new Node(words[1]);
						nodes.add(node);
						hashtable.put(words[1], node);
						hashtable.get(words[0]).addNode(node);
					}
				} else {
					// create the new node then add the node that may or may not exist
					Node node0 = new Node(words[0]);
					nodes.add(node0);
					hashtable.put(words[0], node0);
					if (hashtable.get(words[1]) != null) {
						node0.addNode(hashtable.get(words[1]));
					} else {
						Node node1 = new Node(words[1]);
						nodes.add(node1);
						hashtable.put(words[1], node1);
						node0.addNode(node1);
					}

				}
				// nodes have been added at this point now:
				// edges must be created
				// nodes must have the edges added to them
				Edge edge = new Edge(hashtable.get(words[0]), hashtable.get(words[1]));
				hashtable.get(words[0]).addEdge(edge);
				edges.add(edge);
			}
		}
		/*
		 * System.out.println(numberOfNodes); for(Node hi : nodes) {
		 * System.out.println(hi.getValue()); hi.printEdges(); }
		 */
		/*
		 * for(Edge hi: edges) { hi.printEdge(); }
		 */
	}

	public int outDegree(String v) {
		return hashtable.get(v).getOutDegree();
	}

	public ArrayList<String> shortestPath(String u, String v) {

		ArrayList<String> stringPath = null;

		if (u == v) {
			stringPath = new ArrayList<String>();
			stringPath.add(u);
			return stringPath;
		}

		Stack<Edge> stack = new Stack<Edge>();
		LinkedList<Edge> Q = new LinkedList<Edge>();

		Node source = hashtable.get(u);
		Node destination = hashtable.get(v);

		unmarkAllEdges();
		unmarkAllNodes();

		// get the first set of edges
		// set their depth to one
		// put them in the Q
		// put them in the stack
		boolean destinationFound = false;
		for (Edge edge : source.getEdges()) {
			edge.getSource().mark();
			edge.getDestination().mark();
			edge.setDepth(1);
			Q.add(edge);
			stack.push(edge);
			if (edge.getDestination() == destination) {
				destinationFound = true;
				break;
			}
		}

		// while the Q isn't empty
		// take an edge out of the Q
		// check if these edges are pointing at the destination
		// if not put the edges of their destination in the Q if those edges
		// destinations are not marked
		// put the edges of their destination in the stack if they were put in the Q
		// set the edges of their children depth to 1 + the current edges depth value
		// nodes must be marked if they are not marked
		Edge current;

		while (!Q.isEmpty() && !destinationFound) {
			current = Q.remove();
			for (Edge edge : current.getDestination().getEdges()) {
				if (!edge.getDestination().isMarked()) {
					// add the edge to the Q and stack
					edge.getDestination().mark();
					Q.add(edge);
					stack.push(edge);
					edge.setDepth(current.getDepth() + 1);
					if (edge.getDestination() == destination) {
						destinationFound = true;
						break;
					}
				}
			}
		}
		// System.out.println("the destination was found:" + destinationFound);
		/*
		 * for (Edge edge : stack) { System.out.println( edge.getSource().getValue() +
		 * " " + edge.getDestination().getValue() + " " + edge.getDepth()); }
		 */

		stringPath = new ArrayList<String>();
		stringPath.add(v);
		current = stack.pop();
		// add currents source
		stringPath.add(current.getSource().getValue());
		Edge temp;
		while (current.getSource() != source) {
			temp = stack.pop();
			if (current.getSource() == temp.getDestination()) {
				current = temp;
				stringPath.add(current.getSource().getValue());
			}
		}

		LinkedList<String> tempList = new LinkedList<String>();
		for (String string : stringPath) {
			tempList.add(string);
		}
		ArrayList<String> returnList = new ArrayList<String>();

		while (!tempList.isEmpty()) {
			returnList.add(tempList.remove(tempList.size() - 1));
		}
		if (destinationFound) {
			return returnList;
		} else {
			return new ArrayList<String>();
		}

	}

	public int distance(String u, String v) {
		if (u == v) {
			return 0;
		}
		return shortestPath(u, v).size() - 1;
	}

	/*
	 * returns the must check to see if the source and destination strings are the
	 * same before calling this function.
	 */
	public int distance(ArrayList<String> s, String v) {
		// implementation
		LinkedList<Integer> dists = new LinkedList<Integer>();
		for (String string : s) {
			if (shortestPath(string, v).size() != 0) {
				dists.add(distance(string, v));
			}
		}
		int min = 0;
		if (!dists.isEmpty()) {
			min = dists.remove(dists.size() - 1);
			for (Integer i : dists) {
				if (i.intValue() < min) {
					min = i;
				}
			}
		}
		if (!dists.isEmpty()) {
			return min;
		} else {
			return 0;
		}

	}

	public float  (String u) {
		// implementation
		unmarkAllNodes();
		clearNodeInfluence();
		Node start;
		start = hashtable.get(u);
		if ( start == null ) {
			return 0;
		} 
		LinkedList<Node> Q = new LinkedList<Node>();
		Node current;
		Q.add(start);
		start.setInfluence(1);
		start.mark();
		while(!Q.isEmpty()) {
			current = Q.remove();
			for(Node node : current.getNodes()) {
				if(!node.isMarked()){
					Q.add(node);
					node.mark();
					node.setInfluence(current.getInfluence()/2);
					
				}
			}
		}
		//add up the influence of all nodes and return
		float totalInfluence = 0;
		for(Node node : nodes) {
			totalInfluence += node.getInfluence(); 
		}
		// replace this:
		return totalInfluence;
	}

	//Given a set of nodes, s, calculate their influence on the rest of the graph.
	//For each node of the graph not in s, calculate its influence from the node in
	//s that influences it the most. Sum the influence of all nodes not in s. 
	public float influence(ArrayList<String> s) {
		clearNodeInfluence();
		
		//initialize influence of nodes in s to 1
		int i, length = s.size();
		for(i = 0 ; i < length ; i++) {
			Node node = hashtable.get(s.get(i));
			if ( node != null ) {
				hashtable.get(s.get(i)).setInfluence(1);
			} 
		}
		
		//perform BFS for each node in s, updating influence of the rest of the graph
		//as needed
		for(i = 0 ; i < length ; i++) {
			Node start = hashtable.get(s.get(i));
			if ( start == null ) continue;
			Queue<Node> Q = new LinkedList<Node>();
			
			Q.add(start);
			
			while(!Q.isEmpty()) {
				Node current = Q.remove();
				
				for(Node node : current.getNodes()) {
					
					float newInfluence = current.getInfluence() / 2;
					
					//if a node already has a greater influence from previous nodes in s, don't readd
					//it to the queue because nodes beneath it already have a greater influence too.
					if ( !(node.getInfluence() >= newInfluence) ) {
						Q.add(node);
						node.setInfluence(newInfluence);
					}
				}
			}
		}
		
		//add up the influence of all nodes and return
		float totalInfluence = 0;
		for(Node node : nodes) {
			totalInfluence += node.getInfluence(); 
		}
		return totalInfluence;
	}

	public ArrayList<String> mostInfluentialDegree(int k) {
		ArrayList<String> result = new ArrayList<String>();
		
		if(nodes.size() == 0) return result;
		
		NodeOutDegreeComparator c = new NodeOutDegreeComparator();
		nodes.sort(c);
		
		int numNodes = k;
		if( k > nodes.size() ) {
			k = nodes.size();
		}
		
		int i, length = nodes.size();
		for(i = 0; i < numNodes; i++) {
			result.add(nodes.get(length - i - 1).getValue());
		}
		
		return result;
	}

	public ArrayList<String> mostInfluentialModular(int k) {
		ArrayList<String> result = new ArrayList<String>();
		
		if(nodes.size() == 0) return result;
		
		int i, length = nodes.size();
		for( i = 0 ; i < length ; i++ ) {
			nodes.get(i).setInfluenceOn( influence(nodes.get(i).getValue()) );
		}
		
		NodeInfluenceComparator c = new NodeInfluenceComparator();
		nodes.sort(c);
		
		int numNodes = k;
		if( k > nodes.size() ) {
			k = nodes.size();
		}

		for(i = 0; i < numNodes; i++) {
			result.add(nodes.get(length - i - 1).getValue());
		}
		
		return result;
	}

	public ArrayList<String> mostInfluentialSubModular(int k) {
		// implementation
		ArrayList<String> strings = new ArrayList<String>();
		
		if(nodes.size() == 0) return strings;
		
		Node maxNode = null;
		float maxFloat = 0;
		int maxIndex = 0;
		LinkedList<Node> notS = new LinkedList<Node>();
		for (Node node : nodes) {
			notS.add(node);
		}

		while (strings.size() < k) {
			maxFloat = 0;
			for (int i = 0; i< notS.size(); i++) {
				strings.add(notS.get(i).getValue());
				float temp = influence(strings);
				if (temp > maxFloat) {
					maxNode = notS.get(i);
					maxFloat = temp;
					maxIndex = i;
				}
				strings.remove(strings.size() - 1);
			}
			strings.add(maxNode.getValue());
			notS.remove(maxIndex);
		}
		// replace this:
		return strings;
	}
	
	private void unmarkAllNodes() {
		for(Node node : nodes) {
			node.unmark();
		}
	}
	private void unmarkAllEdges() {
		for (Edge edge: edges) {
			edge.unmark();
		}
	}
	private void clearNodeInfluence() {
		for (Node node : nodes) {
			node.setInfluence(0);
		}
	}
}

class NodeOutDegreeComparator implements Comparator<Node> {

	@Override
	public int compare(Node n1, Node n2) {
		return n1.getOutDegree() - n2.getOutDegree();
	}	
}

class NodeInfluenceComparator implements Comparator<Node> {

	@Override
	public int compare(Node n1, Node n2) {
		float result = n1.getInfluenceOn() - n2.getInfluenceOn();
		int resultInt;
		
		if (result > 0) {
			resultInt = 1;
		} else if (result < 0) {
			resultInt = -1;
		} else {
			resultInt = 0;
		}
		
		return resultInt;
	}	
}

class Node {
	String value;
	int outDegree = 0;
	float influence = 0;
	float influenceOn = 0;
	boolean marked = false;
	ArrayList<Node> nodes = new ArrayList<Node>();
	ArrayList<Edge> edges = new ArrayList<Edge>();

	public Node(String value, Node node) {
		this.value = value;
		this.nodes.add(node);
		outDegree++;
	}

	public Node(String value) {
		this.value = value;
	}

	public void addNode(Node node) {
		this.nodes.add(node);
		outDegree++;
	}

	public void addEdge(Edge edge) {
		this.edges.add(edge);
	}

	public ArrayList<Edge> getEdges() {
		return edges;
	}

	public ArrayList<Node> getNodes() {
		return nodes;
	}

	public String getValue() {
		return value;
	}

	public void printEdges() {
		for (Node hi : nodes) {
			System.out.println("\t" + hi.getValue());
		}
	}

	public int getOutDegree() {
		return this.outDegree;
	}
	public void mark() {
		marked = true;
	}
	public void unmark() {
		marked = false;
	}
	public boolean isMarked() {
		return marked;
	}
	public float getInfluence() {
		return influence;
	}
	
	public float getInfluenceOn() {
		return influenceOn;
	}
	
	public void setInfluence(float influence) {
		this.influence = influence;
	}
	
	public void setInfluenceOn(float influenceOn) {
		this.influenceOn = influenceOn;
	}

}

class Edge {
	Node source;
	Node destination;
	boolean marked = false;
	int depth = 0;

	public Edge(Node source, Node destination) {
		this.destination = destination;
		this.source = source;
	}

	public Node getDestination() {
		return destination;
	}

	public Node getSource() {
		return source;
	}

	public void printEdge() {
		System.out.println(source.getValue() + destination.getValue());
	}

	public boolean isMarked() {
		return marked;
	}

	public void mark() {
		marked = true;
	}

	public void unmark() {
		marked = false;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public int getDepth() {
		return depth;
	}
}