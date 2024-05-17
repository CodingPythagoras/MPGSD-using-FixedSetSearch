package VertexStructure;

public class Edge {
	private Vertex startingVertex;
	private Vertex targetVertex;
	private String edgeKeyString;

	/**
	 * creates an edge object between two vertices
	 * is later used to reconstruct the graph, after restetting the vertices itself
	 * @param pre the predecessor vertex
	 * @param succ the taget vertex
	 */
	public Edge(Vertex pre, Vertex succ) {
		startingVertex = pre;
		targetVertex = succ;
		edgeKeyString = Integer.toString(pre.getID()) + "_" + Integer.toString(succ.getID());
	}

	/**
	 * 
	 * @return the starting vertex of the edge
	 */
	public Vertex getStartingVertex() {
		return startingVertex;
	}


	/**
	 * 
	 * @return the target vertex of the edge
	 */
	public Vertex getTargetVertex() {
		return targetVertex;
	}

	/**
	 * is used to get each edge as string and extract start and target vertex to create a frequency map
	 * @return the edge as a String with "startID_targetID"
	 */
	public String getEdgeKeyString() {
		return edgeKeyString;
	}

	
}

