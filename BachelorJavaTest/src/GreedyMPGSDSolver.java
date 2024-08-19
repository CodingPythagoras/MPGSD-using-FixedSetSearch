import java.util.List;

import GraphStructures.MPGSDGraph;
import GraphStructures.SolvedGraph;
import GraphStructures.SubGraph;
import VertexStructure.DemandVertex;
import VertexStructure.SupplyVertex;
import VertexStructure.Vertex;

public class GreedyMPGSDSolver {
	
	/*
	 * Solves the MPGSD Graph by completing one Subgraph and then continues
	 */
	public static SolvedGraph GreedySolve1(MPGSDGraph g,int trait) {
		resetGraphVertices(g);
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
				
				Vertex[] demandPair = selctedSubGraph.getVertexToAdd(trait);
				DemandVertex selctedAdjDemV = (DemandVertex) demandPair[0];
				
				if(selctedAdjDemV == null) {
					selctedSubGraph.setComplete();
					//selctedSubGraph is complete, when no fitting AdjVertexes could be found
					break;
				}else {
					Vertex predecessorV = demandPair[1];
					predecessorV.addAdjVertex(selctedAdjDemV);
					selctedAdjDemV.setPredecessor(predecessorV);
					predecessorV.setSuccessor(selctedAdjDemV);
					
					selctedSubGraph.addEdge(predecessorV, selctedAdjDemV);
					
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
	 * using a certain trait
	 * 1: trait, which selects based on the max demand, which can be fullfilled
	 * 2: trait, which selects based on the number of Adj. Vertices
	 * 3: trait, which uses the Ratio between Demand / number of Adj Vertices
	 * 4: by using random traits of the first three
	 */
	public static SolvedGraph GreedySolve2(MPGSDGraph g, int trait) {
		//TODO carefull predecessor, successors etc at Vertex deleted aswell if needed/ need to create a new Graph everytime with own vertices
		resetGraphVertices(g);
		SolvedGraph graphOfSubGraphs = new SolvedGraph(g);
		
		graphOfSubGraphs.setTotalGivenSupply(g.getTotalMPGSDSupply());
		graphOfSubGraphs.setTotalOriginalDemand(g.getTotalMPGSDDemand());
		
		
		
			
		while(true) {
			//always takes the Subgraph with the higest remainign Supply
			SubGraph selctedSubGraph = graphOfSubGraphs.getSubgraphWithHigestSupply();
			//selctedSubGraph is null, when all subgraphs are complete
			if(selctedSubGraph == null) {
				break;
			}
			//TODO (IMPORTANT) can be changed to random Vertex
			Vertex[] demandPair = selctedSubGraph.getVertexToAdd(trait);
			//Vertex[] demandPair = selctedSubGraph.getRandomVertex();
			
			
			DemandVertex selctedAdjDemV = (DemandVertex) demandPair[0];
			
			if(selctedAdjDemV == null) {
				selctedSubGraph.setComplete();
				
			}else {
				Vertex predecessorV = demandPair[1];
				predecessorV.addAdjVertex(selctedAdjDemV);
				selctedAdjDemV.setPredecessor(predecessorV);
				predecessorV.setSuccessor(selctedAdjDemV);
				
				selctedSubGraph.addEdge(predecessorV, selctedAdjDemV);
				
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
		
		double percent = ((double) supplyUsed / (double) totalSupply) * 100;
		return "The total coverage amounts to a total of " + totalCovDemand + "/" + totalDemand + 
				" With a total supply use of " + supplyUsed + "/" + totalSupply + " that equals to a coverage of: " + percent + "%.";
	}
	
	
	
	public static SolvedGraph GreedySolve2(MPGSDGraph g, int trait, List<SubGraph> fixedsetsfound) {
		//resetGraphVertices(g);
		SolvedGraph graphOfSubGraphs = new SolvedGraph(g, fixedsetsfound);

		graphOfSubGraphs.setTotalGivenSupply(g.getTotalMPGSDSupply());
		graphOfSubGraphs.setTotalOriginalDemand(g.getTotalMPGSDDemand());
		
			
		while(true) {
			//always takes the Subgraph with the higest remainign Supply
			SubGraph selctedSubGraph = graphOfSubGraphs.getSubgraphWithHigestSupply();
			
			//selctedSubGraph is null, when all subgraphs are complete
			if(selctedSubGraph == null) {
				break;
			}
			//TODO (IMPORTANT) can be changed to random Vertex
			Vertex[] demandPair = selctedSubGraph.getVertexToAdd(trait);
			DemandVertex selctedAdjDemV = (DemandVertex) demandPair[0];
			
			if(selctedAdjDemV == null) {
				selctedSubGraph.setComplete();
				
			}else {
				Vertex predecessorV = demandPair[1];
				predecessorV.addAdjVertex(selctedAdjDemV);
				selctedAdjDemV.setPredecessor(predecessorV);
				predecessorV.setSuccessor(selctedAdjDemV);
				
				selctedSubGraph.addEdge(predecessorV, selctedAdjDemV);
				
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
	
	
	
	
	public static void resetGraphVertices(MPGSDGraph g) {
		
	    for (Vertex vertex : g.getAllVertices()) {  // Assuming you have a method to get all vertices
	        if (vertex instanceof DemandVertex) {
	            ((DemandVertex)vertex).resetDemandVertex();
	        } else if (vertex instanceof SupplyVertex) {
	            ((SupplyVertex)vertex).resetSupplyVertex();
	        }
	    }
	}
	
	
	
	public static SolvedGraph GreedySolveXTimes(int x, MPGSDGraph g) {
		
		SolvedGraph bestSolution = GreedySolve2(g, 5); //5 beeeing random vertex, not considering any traits
		
		for(int i = 0; i < x; i++) {
			SolvedGraph s = GreedySolve2(g, 5); //5 beeeing random vertex, not considering any traits
			if(s.getTotalCoveredDemand() > bestSolution.getTotalCoveredDemand()) {
				bestSolution = s;
				System.out.println("new best on trial " + i + ":" + bestSolution.getTotalCoveredDemand());
			}
		}
		System.out.println("Best solution out of " + x + " times found.");
		return bestSolution;
		
	}
	


}
