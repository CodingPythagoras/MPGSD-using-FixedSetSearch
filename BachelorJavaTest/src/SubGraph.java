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
	
	public Vertex[] getVertexToAdd() {
		//because first element in subgraph is always the supply Vertex
		
		int remainingSupply = getSubgraphsSupplyVertex().getRemainingSupply();
		//TODO null fix?
		DemandVertex bestFittingDemandVertex = null;
		Vertex predecessor = null;
		
		int maxDem = 0;
		
		//Iteration over every Vertex in Subgraph
		for (int i = 0; i <= subGraph.size() - 1; i++ ) {
			Vertex v = subGraph.get(i);
			
			//Iteration over ervery Adj Vertex of Vertex v
			for (int j = 0; j <= v.getAdjVertexList().size() - 1; j++) {
				Vertex k = v.getAdjVertexList().get(j);
				if(!k.isSupplyVertex) {
					int currentDemand = ((DemandVertex)k).getDemand();
					if(currentDemand <= remainingSupply && ((DemandVertex) k).getDemandIsCovered() == false) {
						
						//implement trait by which element should be selected
						if(maxDem < currentDemand) {
							maxDem = currentDemand;
							bestFittingDemandVertex = ((DemandVertex)k);
							predecessor = v;
						}
					}
				}
					
			}
		}
		return new Vertex[] {bestFittingDemandVertex, predecessor};
	}
}