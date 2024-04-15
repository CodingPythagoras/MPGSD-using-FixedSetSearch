import java.util.LinkedList;

public class SubGraph {
	LinkedList<Vertex> subGraph; 
	boolean isComplete = false;
	
	public SubGraph(SupplyVertex supV) {
		subGraph = new LinkedList<>();
		subGraph.add(supV);
	}
	
	public void setComplete() {
		isComplete = true;
	}
	
	public boolean isComplete() {
		return isComplete;
	}
	
	public void addVertex(Vertex v) {
		subGraph.add(v);
	}
	/*
	 * returns the Subgraphs SupplyVertex
	 */
	public SupplyVertex getSubgraphsSupplyVertex() {
		return (SupplyVertex)subGraph.get(0);
	}
	
	public Vertex getSubgraphsVertex(int pos) {
		return subGraph.get(pos);
	}
	
	public LinkedList<Vertex> getVertexList(){
		return subGraph;
	}
	
	public String getMathematicalRepresentationOfSubgraph() {
		//TODO [ID][Dem/Sup][Predecessor]
		//int[][][] vertexAsArray = new int[1][1][1];
		//vertexAsArray[] arrayOfVertexes = new vertexAsArray[7];
		
		return "test";
	}
}