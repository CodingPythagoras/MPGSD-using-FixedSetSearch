

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
				
				Vertex[] demandPair = selctedSubGraph.getVertexToAdd();
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
			//can be changed to random Vertex
			Vertex[] demandPair = selctedSubGraph.getRandomVertex();
			//Vertex[] demandPair = getHighestListAdjVertex(selctedSubGraph);
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
	
	
	
	
	
	public static String getDemandCoverage(SolvedGraph solvedMPGSDGraphofSubgraphs) {
		int totalCovDemand = solvedMPGSDGraphofSubgraphs.getTotalCoveredDemand();
		int totalDemand = solvedMPGSDGraphofSubgraphs.getTotalOriginalDemand();
		int supplyUsed = solvedMPGSDGraphofSubgraphs.getTotalUsedSupply();
		int totalSupply = solvedMPGSDGraphofSubgraphs.getTotalGivenSupply();
		return "The total coverage amounts to a total of " + totalCovDemand + "/" + totalDemand + 
				" With a total supply use of " + supplyUsed + "/" + totalSupply;
	}
	

}
