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
	
	public int[][] getMathematicalRepresentationOfSubgraph() {
		//TODO [ID][Dem/Sup][Predecessor]
		int[][] vertexAsArray = new int[subGraph.size()][3];
		for(int i = 0; i <= subGraph.size() - 1; i++) {
			Vertex v = subGraph.get(i);
			vertexAsArray[i][0] = v.getID();
			if(v.isSupplyVertex) {
				vertexAsArray[i][1] = ((SupplyVertex)v).getInitialSupply();
				//Predecessor is itself
				vertexAsArray[i][2] = ((SupplyVertex)v).getID();
			}
			if(!v.isSupplyVertex) {
				vertexAsArray[i][1] = -((DemandVertex)v).getDemand();
				vertexAsArray[i][2] = ((DemandVertex)v).getPredecessor().getID();
			}
			
		}
		
		
		return vertexAsArray;
	}
}