import java.util.LinkedList;

public class GreedyMPGSDSolver {
	
	/*
	 * Solves the MPGSD Graph by completing one Subgraph and then continues
	 */
	public static SolvedGraph GreedySolve1(MPGSDGraph g) {
		SolvedGraph graphOfSubGraphs = new SolvedGraph(g);
		graphOfSubGraphs.setTotalGivenSupply(g.getTotalMPGSDSupply());
		graphOfSubGraphs.setTotalOriginalDemand(g.getTotalMPGSDDemand());
		
		for(int i = 0; i <=  g.getNumberofSupplyVertexes() - 1 ; i++) {
			
			SubGraph selctedSubGraph = graphOfSubGraphs.getSubgraphWithHigestSupply();
			//could be added but not necessary because of for-loop
			if(selctedSubGraph == null) {
				break;
			}
			
			while(true) {
				
				Vertex[] demandPair = getHighestListAdjVertex(selctedSubGraph);
				DemandVertex selctedAdjDemV = (DemandVertex) demandPair[0];
				
				if(selctedAdjDemV == null) {
					selctedSubGraph.setComplete();
					//selctedSubGraph is complete, when no fitting AdjVertexes could be found
					break;
				}else {
					Vertex predecessorV = demandPair[1];
					predecessorV.addAdjVertex(selctedAdjDemV);
					selctedAdjDemV.setPredecessor(predecessorV);
					selctedSubGraph.addVertex(selctedAdjDemV);
					selctedSubGraph.getSubgraphsSupplyVertex().useSupply(selctedAdjDemV.getDemand());
					selctedAdjDemV.setDemandAsCovered();
							
					graphOfSubGraphs.addCoveredDemand(selctedAdjDemV.getDemand());
					graphOfSubGraphs.addUsedSupply(selctedAdjDemV.getDemand());
					graphOfSubGraphs.addNumberOfDemandVertexes(1);
				}
				
			}
			
		}
		
		return graphOfSubGraphs;
	}
	
	
	/*
	 * Solves the MPGSD Graph by cyling between the higest remaining supply Subgraphs
	 */
	public static SolvedGraph GreedySolve2(MPGSDGraph g) {
		SolvedGraph graphOfSubGraphs = new SolvedGraph(g);
		
		graphOfSubGraphs.setTotalGivenSupply(g.getTotalMPGSDSupply());
		graphOfSubGraphs.setTotalOriginalDemand(g.getTotalMPGSDDemand());
		
		
		
			
		while(true) {
			SubGraph selctedSubGraph = graphOfSubGraphs.getSubgraphWithHigestSupply();
			//selctedSubGraph is null, when all subgraphs are complete
			if(selctedSubGraph == null) {
				break;
			}
			Vertex[] demandPair = getHighestListAdjVertex(selctedSubGraph);
			DemandVertex selctedAdjDemV = (DemandVertex) demandPair[0];
			
			if(selctedAdjDemV == null) {
				selctedSubGraph.setComplete();
			}else {
				Vertex predecessorV = demandPair[1];
				predecessorV.addAdjVertex(selctedAdjDemV);
				selctedAdjDemV.setPredecessor(predecessorV);
				selctedSubGraph.addVertex(selctedAdjDemV);
				selctedSubGraph.getSubgraphsSupplyVertex().useSupply(selctedAdjDemV.getDemand());
				selctedAdjDemV.setDemandAsCovered();
						
				graphOfSubGraphs.addCoveredDemand(selctedAdjDemV.getDemand());
				graphOfSubGraphs.addUsedSupply(selctedAdjDemV.getDemand());
				graphOfSubGraphs.addNumberOfDemandVertexes(1);
			}
			
		}
		
		
			
		
		
		return graphOfSubGraphs;
	}
	
	
	
	public static Vertex[] getHighestListAdjVertex(SubGraph vListSubgraph) {
		//because first element in subgraph is always the supply Vertex
		
		int remainingSupply = vListSubgraph.getSubgraphsSupplyVertex().getRemainingSupply();
		//TODO null fix?
		DemandVertex bestFittingDemandVertex = null;
		Vertex predecessor = null;
		int maxDem = 0;
		LinkedList<Vertex> vList = vListSubgraph.getVertexList(); 
		
		for (int i = 0; i <= vList.size() - 1; i++ ) {
			Vertex k = vList.get(i);
			//getHighestFittingAdjVertex is swappable with function of choise
			DemandVertex currentAdjDemV = (DemandVertex) k.getHighestFittingAdjVertex(remainingSupply /*, vList*/);
			if(currentAdjDemV != null) {
				/*
				 * TODO currently only takes the one with the most demand of the given
				 */
				if(maxDem < currentAdjDemV.getDemand()) {
					maxDem = currentAdjDemV.getDemand();
					bestFittingDemandVertex = currentAdjDemV;
					predecessor = k;
				}
			}
			
			
		}
		return new Vertex[] {bestFittingDemandVertex, predecessor};
	}
	
	public static String getDemandCoverage(SolvedGraph solvedMPGSDGraphofSubgraphs) {
		int totalCovDemand = solvedMPGSDGraphofSubgraphs.getTotalCoveredDemand();
		int totalDemand = solvedMPGSDGraphofSubgraphs.getTotalOriginalDemand();
		int supplyUsed = solvedMPGSDGraphofSubgraphs.getTotalUsedSupply();
		int totalSupply = solvedMPGSDGraphofSubgraphs.getTotalGivenSupply();
		return "The total coverage amounts to a total of " + totalCovDemand + "/" + totalDemand + 
				" With a total supply use of " + supplyUsed + "/" + totalSupply;
	}
}
