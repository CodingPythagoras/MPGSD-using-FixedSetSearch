import java.util.List;
import GraphStructures.MPGSDGraph;
import GraphStructures.SolvedGraph;
import GraphStructures.SubGraph;
import VertexStructure.DemandVertex;
import VertexStructure.SupplyVertex;
import VertexStructure.Vertex;


/**
 * contains static methods for solving the MPGSD problem using different approaches
 * @author Manuel
 *
 */
public class GreedyMPGSDSolver {
	
	
	
	

	/**
	 * Solves the MPGSD Graph by cycling between the highest remaining supply Subgraphs
	 * using a certain trait
	 * 1: trait, which selects based on the max demand, which can be fulfilled
	 * 2: trait, which selects based on the number of Adj. Vertices
	 * 3: trait, which uses the Ratio between Demand / number of Adj. Vertices
	 * 4: by using random traits of the first three
	 * @param g MPGSDGraph to be solved
	 * @param trait for vertex selection
	 * @return a solved graph of the MPGSD problem
	 */
	public static SolvedGraph greedySolve(MPGSDGraph g, int trait) {
		//TODO careful predecessor, successors etc at Vertex deleted as well if needed/ need to create a new Graph every time with own vertices
		resetGraphVertices(g);
		SolvedGraph graphOfSubGraphs = new SolvedGraph(g);
		
		graphOfSubGraphs.setTotalGivenSupply(g.getTotalMPGSDSupply());
		graphOfSubGraphs.setTotalOriginalDemand(g.getTotalMPGSDDemand());
		
		
		
			
		while(true) {
			//always takes the subgraph with the highest remaining supply
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
				//updateVertex predecessor, successor, add edge
				predecessorV.addAdjVertex(selctedAdjDemV);
				selctedAdjDemV.setPredecessor(predecessorV);
				predecessorV.setSuccessor(selctedAdjDemV);
				selctedSubGraph.addEdge(predecessorV, selctedAdjDemV);
				
				//addVertex
				selctedSubGraph.addVertex(selctedAdjDemV);
				selctedSubGraph.getSubgraphsSupplyVertex().useSupply(selctedAdjDemV.getDemand());
				selctedAdjDemV.setDemandAsCovered();
						
				//update supply and demand
				graphOfSubGraphs.addCoveredDemand(selctedAdjDemV.getDemand());
				graphOfSubGraphs.addUsedSupply(selctedAdjDemV.getDemand());
				graphOfSubGraphs.addNumberOfDemandVertexes(1);
			}
			
		}
		
		return graphOfSubGraphs;
	}
	
	
	
	
	/**
	 * gets the demand coverage as a String
	 * @param solvedMPGSDGraphofSubgraphs graph which demand coverage should be shown
	 * @return A String which can then be printed out for example
	 */
	public static String getDemandCoverage(SolvedGraph solvedMPGSDGraphofSubgraphs) {
		int totalCovDemand = solvedMPGSDGraphofSubgraphs.getTotalCoveredDemand();
		int totalDemand = solvedMPGSDGraphofSubgraphs.getTotalOriginalDemand();
		int supplyUsed = solvedMPGSDGraphofSubgraphs.getTotalUsedSupply();
		int totalSupply = solvedMPGSDGraphofSubgraphs.getTotalGivenSupply();
		
		double relativeError =(double) (1-(double)((double)solvedMPGSDGraphofSubgraphs.getTotalUsedSupply() / (double)solvedMPGSDGraphofSubgraphs.getTotalGivenSupply())) * 100;
		
		double percent = ((double) supplyUsed / (double) totalSupply) * 100;
		return "The total coverage amounts to a total of " + totalCovDemand + "/" + totalDemand + "\n" +
				"With a total supply use of " + supplyUsed + "/" + totalSupply + " that equals to a coverage of: " + percent + "%." + "\n" + 
				"Resulting in an relative error of: " + relativeError + "%." + "\n";
		
		
	}
	
	
	/**
	 * 
	 * Solves the MPGSD Graph by cycling between the highest remaining supply Subgraphs
	 * using a certain trait
	 * Also includes an initial solution, on which is being build on, the fixed set 
	 * 1: trait, which selects based on the max demand, which can be fulfilled
	 * 2: trait, which selects based on the number of Adj. Vertices
	 * 3: trait, which uses the Ratio between Demand / number of Adj. Vertices
	 * 4: by using random traits of the first three
	 * @param g MPGSDGraph to be solved
	 * @param trait for vertex selection
	 * @param fixedsetsfound initial solution, on which is being build on
	 * @return A solved graph of the MPGSD problem
	 */
	public static SolvedGraph greedySolve(MPGSDGraph g, int trait, List<SubGraph> fixedsetsfound) {
		//resetGraphVertices(g);
		SolvedGraph graphOfSubGraphs = new SolvedGraph(g, fixedsetsfound);
		
		
		
		graphOfSubGraphs.setTotalGivenSupply(g.getTotalMPGSDSupply());
		graphOfSubGraphs.setTotalOriginalDemand(g.getTotalMPGSDDemand());
		
			
		while(true) {
			//always takes the subgraph with the highest remaining supply
			SubGraph selctedSubGraph = graphOfSubGraphs.getSubgraphWithHigestSupply();
			
			//selctedSubGraph is null, when all subgraphs are complete
			if(selctedSubGraph == null) {
				break;
			}
			//TODO (IMPORTANT) can be changed to random Vertex
			Vertex[] demandPair = selctedSubGraph.getVertexToAdd(trait);
			DemandVertex selctedAdjDemV = (DemandVertex) demandPair[0];
			
			//if no vertex could be found
			if(selctedAdjDemV == null) {
				selctedSubGraph.setComplete();
				
			}else {
				//updateVertex predecessor, successor, add edge
				Vertex predecessorV = demandPair[1];
				predecessorV.addAdjVertex(selctedAdjDemV);
				selctedAdjDemV.setPredecessor(predecessorV);
				predecessorV.setSuccessor(selctedAdjDemV);
				
				selctedSubGraph.addEdge(predecessorV, selctedAdjDemV);
				
				//add vertex
				selctedSubGraph.addVertex(selctedAdjDemV);
				selctedSubGraph.getSubgraphsSupplyVertex().useSupply(selctedAdjDemV.getDemand());
				selctedAdjDemV.setDemandAsCovered();
						
				//update overall supply and demand usage
				graphOfSubGraphs.addSupplyAndDemand(selctedAdjDemV.getDemand());
				graphOfSubGraphs.addNumberOfDemandVertexes(1);

			}
			
		}
		
		return graphOfSubGraphs;
	}
	
	
	
	/**
	 * resets the graphs vertices of the MPGSD graph
	 * (important to notice, that the edges, which are being saved by each subgraph are not reseted, due to rebuilding of the fixed set)
	 * @param g MPGSDGraph, which vertices are being reseted
	 */
	public static void resetGraphVertices(MPGSDGraph g) {
		
	    for (Vertex vertex : g.getAllVertices()) {  // Assuming you have a method to get all vertices
	        if (vertex instanceof DemandVertex) {
	            ((DemandVertex)vertex).resetDemandVertex();
	        } else if (vertex instanceof SupplyVertex) {
	            ((SupplyVertex)vertex).resetSupplyVertex();
	        }
	    }
	}
	
	

	
	/**
	 * TODO method can be removed
	 * Just greedy solves a given graph X amount of times and returns the best one
	 * implemented for test purposes of random vertex selection
	 * @param x times to solve
	 * @param g MPGSDGraph to be solved
	 * @return best SolvedGraph out of X amount of solves
	 */
	public static SolvedGraph GreedySolveXTimes(int x, MPGSDGraph g) {
		
		SolvedGraph bestSolution = greedySolve(g, 5); //5 being random vertex, not considering any traits
		
		for(int i = 0; i < x; i++) {
			SolvedGraph s = greedySolve(g, 5); //5 being random vertex, not considering any traits
			if(s.getTotalCoveredDemand() > bestSolution.getTotalCoveredDemand()) {
				bestSolution = s;
				System.out.println("new best on trial " + i + ":" + bestSolution.getTotalCoveredDemand());
			}
		}
		System.out.println("Best solution out of " + x + " times found.");
		return bestSolution;
		
	}
	
	
	/**
	 * Solves the MPGSD Graph by completing one Subgraph after another (stating with the one with the most supply)
	 * Was implemented as an first alternative, but not used due to worse results
	 * so currently not used, but can replace other greedySolves if the wanted
	 * @param g MPGSD graph to be solved
	 * @param trait trait after which vertices should be selected to add
	 * @return a SolvedGraph of the problem
	 */
	public static SolvedGraph greedySolveAlternative(MPGSDGraph g, int trait) {
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
	


}
