package VertexStructure;

public class Edge {
	private Vertex startingVertex;
	private Vertex targetVertex;
	private int edgeKey;
	private String edgeKeyString;

	public Edge(Vertex pre, Vertex succ) {
		startingVertex = pre;
		targetVertex = succ;
		edgeKey = Integer.parseInt(Integer.toString(pre.getID()) + Integer.toString(succ.getID()));
		edgeKeyString = Integer.toString(pre.getID()) + "_" + Integer.toString(succ.getID());
	}

	public Vertex getStartingVertex() {
		return startingVertex;
	}


	public Vertex getTargetVertex() {
		return targetVertex;
	}


	public int getEdgeKey() {
		return edgeKey;
	}

	public String getEdgeKeyString() {
		return edgeKeyString;
	}

	
}

