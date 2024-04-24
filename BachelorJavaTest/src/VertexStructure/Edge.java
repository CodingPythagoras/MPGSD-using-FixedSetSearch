package VertexStructure;

public class Edge {
	private Vertex startingVertex;
	private Vertex targetVertex;
	private String edgeKeyString;

	public Edge(Vertex pre, Vertex succ) {
		startingVertex = pre;
		targetVertex = succ;
		edgeKeyString = Integer.toString(pre.getID()) + "_" + Integer.toString(succ.getID());
	}

	public Vertex getStartingVertex() {
		return startingVertex;
	}


	public Vertex getTargetVertex() {
		return targetVertex;
	}

	public String getEdgeKeyString() {
		return edgeKeyString;
	}

	
}

