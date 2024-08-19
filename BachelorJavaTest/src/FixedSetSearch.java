import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import GraphStructures.MPGSDGraph;
import GraphStructures.SolvedGraph;
import GraphStructures.SubGraph;
import JSONtoGraph.GraphBuilder;
import VertexStructure.DemandVertex;
import VertexStructure.SupplyVertex;
import VertexStructure.Vertex;

public class FixedSetSearch {

	/*
	 * Input: t total number of iterations, 
	 * m number of best solutions used for randomly selecting the base solution
	 *  n and k specify the set of solutions Skn
	 *   control parameter for the stagnation in the FSS MaxStag
	 *   //--> Greedy
	 *   size of the initial population N
	 *   problem instance G = (V, E, w)
	 *   size of the RCL α
	 *   initial temperature Tinit,
	 *	 control parameter for the number of solution change attempts δ, 
	 *	 cooling factor  and the 
	 *
	 *	 minimal acceptance rate mi
	 */
	
	/**
	 * TODO shorten, like in pseudocode?
	 * @param g MPGSD Graph to be solved
	 * @param t how many greedy solutions should be performed
	 * @param m how many solutions for the FSS should be considered
	 * @param threshold what percentage in 0. of supply should be covered to be considered
	 * @return A list of with subraphs, one for each supply vertex
	 * @throws IOException
	 */
	public static List<SubGraph> getFixedSets(MPGSDGraph g, int t, int m, double threshold /*, int n, int k, int MaxStag*/) throws IOException{
		
		
		
		int amountOfSubGraphs = g.getNumberofSupplyVertexes();
		List<SubGraph> fixedSets = new ArrayList<SubGraph>();
		
		//m sets the amount of Solution out of which the fixed sets should be formed
		SolvedGraph[] arrayOfBestGreedySolutions = new SolvedGraph[m];
		
		//solves the given graph g t times
		for(int i = 0; i <= t; i++) {
			
			//TODO change back to 4 if results turn out worse
			//4 being random trait, 5 random vertex
			SolvedGraph JSONGraphSolution = GreedyMPGSDSolver.GreedySolve2(g, 4);
			
			int covSup = JSONGraphSolution.getTotalCoveredDemand();
			int totalSup = JSONGraphSolution.getTotalGivenSupply();
			double supPercentCovered = (double)covSup / (double)totalSup;
			//System.out.println(covSup + "/" + totalSup);
			//System.out.println(supPercentCovered);
			
			//if the solution graph full fills a certain percentage of covered demand, it is viewed as one of the best solutions
			if(supPercentCovered > threshold) {
				boolean placefound = false;
				for(int j = 0; j <= m - 1; j++) {
					//looks for a free space in the array
					if(arrayOfBestGreedySolutions[j] == null) {
						arrayOfBestGreedySolutions[j] = JSONGraphSolution;
						
						placefound = true;
						break;
					}
				}
				
				//otherwise checks if the current solution outperforms another and replaces it
				if(placefound == false) {
					int worstIndex = -1;
					int worstPerformance = Integer.MAX_VALUE;
					
					//scans for the worst elemnt in array
					for(int j2 = 0; j2 <= m - 1; j2++) {
						int currentPerformance = arrayOfBestGreedySolutions[j2].getTotalCoveredDemand();
						if(currentPerformance < worstPerformance) {
							worstPerformance = currentPerformance;
							worstIndex = j2;
						}
					}
					//checks if worse elment is outperfomred by our JSONGraphSolution
					if(worstIndex != -1 && worstPerformance < JSONGraphSolution.getTotalCoveredDemand()) {
						arrayOfBestGreedySolutions[worstIndex] = JSONGraphSolution;
					}
				}
				
			}
		}
		
		if(arrayOfBestGreedySolutions[0] == null) {
			JOptionPane.showMessageDialog(new JFrame(), "No best Solutions could be found: please reduce Threshold.");
			//create a solved graph with only SupplyVertices
			SolvedGraph replacementGraph = new SolvedGraph(g);
			arrayOfBestGreedySolutions[0] = replacementGraph;
		}
		
		//arrayOfBestGreedySolutions now contains the m best solutions
		
		ArrayList<ArrayList<SubGraph>> ListForEachSupply = new ArrayList<>();
		
		for(int n = 0; n <= amountOfSubGraphs - 1; n++) {
			ArrayList<SubGraph> subgraphsForOneSupply = new ArrayList<>();
			//get every 1st then 2nd etc... Subgraph of all Subgraphs
			for(int p = 0; p <= arrayOfBestGreedySolutions.length - 1; p++) {
				SolvedGraph solvOne = arrayOfBestGreedySolutions[p];
				if(solvOne == null) {
					//System.out.println("Only found " + p + " best solutions: please reduce Threshold");
					break;
				}
				subgraphsForOneSupply.add(solvOne.getSubgraph(n));   
				
		    }
			ListForEachSupply.add(subgraphsForOneSupply);
			
		}
	
		//reset Vertices now, because they get changed by creation of the subgraphs
		GreedyMPGSDSolver.resetGraphVertices(g);
		//now ListForEachSupply.get(0) should contain all subgraphs representing the Subgraphs containing SupplyVertex1 and so on...
		for(int x = 0; x <= ListForEachSupply.size() - 1; x++) {
			SubGraph fixedSet = findFixedSet(ListForEachSupply.get(x), g);
			fixedSets.add(fixedSet);
		}
		
		
		
		
		return fixedSets;
		
	}
	
	//private static SubGraph findFixedSet(ArrayList<SubGraph> subgraphsForOneSupply)
	
	

	private static SubGraph findFixedSet(ArrayList<SubGraph> subgraphsForOneSupply, MPGSDGraph g) {
		Map<String, Integer> edgeFrequency = new HashMap<>();
		
		for(int i = 0; i <= subgraphsForOneSupply.size() - 1;i++) {
			
			String[] edges = subgraphsForOneSupply.get(i).getSubgraphsEdgesStringArray();
			for(int j = 0; j<= edges.length - 1; j++) {
				edgeFrequency.merge(edges[j], 1, Integer::sum);
			}
			//System.out.println("Edges: " + i + subgraphsForOneSupply.get(i).getArrayOfEdgesAsString()); 
		}
		//System.out.println(edgeFrequency); 
		
		// Determine the threshold for an edge to be considered common, e.g., appears in more than half of the subgraphs
		// TODO at least 2 otherwise vertices could be occurring in more than one subgraph, which is to be forbidden
		//TODO make 0.7 to user variable, where threshhold can be manually adjusted
	    double threshold = Math.max((double) subgraphsForOneSupply.size() / 2, subgraphsForOneSupply.size() * 0.5);		
	    //System.out.println(threshold); 
	    // Collect all edges that meet the frequency threshold
	    Set<String> commonEdges = new HashSet<>();
	    for (Map.Entry<String, Integer> entry : edgeFrequency.entrySet()) {
	        if (entry.getValue() > threshold) {
	            commonEdges.add(entry.getKey());
	        }
	    }
	    
	    
	    if (!commonEdges.isEmpty()) {
	        // Create a new subgraph using the common edges
	    	
	    	// can take any random subgraph, because they all share the same supply Vertex on position 0
	    	SupplyVertex supplyVertex = subgraphsForOneSupply.get(0).getSubgraphsSupplyVertex();
	    	
	        SubGraph fixedSet = new SubGraph(supplyVertex); // Initialize with the common supply vertex
	     
	        // Add vertices and edges to the fixed set
	        Set<Integer> addedVertices = new HashSet<>();
	        addedVertices.add(supplyVertex.getID());
	        
	        for (String edge : commonEdges) {
	       
	            String[] parts = edge.split("_");
	            int startVertexId = Integer.parseInt(parts[0]);
	            int targetVertexId = Integer.parseInt(parts[1]);

	            Vertex firstVertex = g.getVertexById(startVertexId);
	            Vertex targetVertex = g.getVertexById(targetVertexId);
	            
	            firstVertex.addAdjVertex(targetVertex);
	            
	            firstVertex.setSuccessor(targetVertex);
	            targetVertex.setPredecessor(firstVertex);

	            
	            
	            if (!addedVertices.contains(startVertexId) && supplyVertex.getID() != startVertexId && supplyVertex.getRemainingSupply() >= ((DemandVertex)firstVertex).getDemand()) {
	                fixedSet.addVertex(firstVertex);
	                if(!firstVertex.getIsSupplyVertex()) {
	                	((DemandVertex)firstVertex).setDemandAsCovered();
	                	supplyVertex.useSupply(((DemandVertex)firstVertex).getDemand());
	                }
	                addedVertices.add(startVertexId);
	            }
	            if (!addedVertices.contains(targetVertexId) && supplyVertex.getID() != targetVertexId && supplyVertex.getRemainingSupply() >= ((DemandVertex)targetVertex).getDemand()) {
	                fixedSet.addVertex(targetVertex);
	                if(!targetVertex.getIsSupplyVertex()) {
	                	((DemandVertex)targetVertex).setDemandAsCovered();
	                	supplyVertex.useSupply(((DemandVertex)targetVertex).getDemand());
	                }
	                addedVertices.add(targetVertexId);
	                fixedSet.addEdge(firstVertex, targetVertex);
	            }
	            

	        }
	
	        //Set new after DFS (connectivity check)
	        fixedSet.getSubgraphsSupplyVertex().onlyResetSupply();
	        
	        //Checks for connectivity and if not extracts the part connected to the supplyvertex

	        
	        SubGraph connectedComponent = fixedSet.extractConnectedComponent();

	        
	        //TODO vorziehen, wenn Vertex geadded wird
	        //iterates over the new subgraph to determine its used demand and number of demand vertices
	        for(int k = 0; k <= connectedComponent.getVertexList().size() - 1; k++) {
            	if(!connectedComponent.getVertexList().get(k).getIsSupplyVertex()) {
            		DemandVertex demV = (DemandVertex)connectedComponent.getVertexList().get(k);
            		connectedComponent.addOneNumDemVer();
            		connectedComponent.updateSubsCovDemand(demV.getDemand());
            		connectedComponent.getSubgraphsSupplyVertex().useSupply(demV.getDemand());
            	}
            }
	        return connectedComponent;
	    }
	    
	    //if no common edges found, just returns a subgraph with a supply Vertex as Start
		return new SubGraph(subgraphsForOneSupply.get(0).getSubgraphsSupplyVertex());
	}


	public static SolvedGraph getBestFSSolution(int iterations, MPGSDGraph g, int trait,  List<SubGraph> fixedSets) {
		
		//TODO if printing fixedSet here before rebuild sometimes predecessor is null
		
		int currentBest = 0;
		SolvedGraph bestGraph = null;

		for(int i = 1; i <= iterations; i++) {
		
			
			//Resets the Vertices from previous solutions and rebuilds the Subsets
			GreedyMPGSDSolver.resetGraphVertices(g);
			rebuildFixedSetVertices(fixedSets);
			
			SolvedGraph solved = GreedyMPGSDSolver.GreedySolve2(g, trait, fixedSets);
			String x = solved.getSolvedGraphMathematical();
			
			int currentDemCov = solved.getTotalCoveredDemand();
			
			if(currentBest < currentDemCov) {
				
				currentBest = currentDemCov;
				bestGraph = solved;
			}
			
		}
		//TODO dont know if needed!
		//rebuildFixedSetVertices(bestGraph.getGraphOfSubgraphs());
		return bestGraph;
	}
	
	/**
	 * rebuilds the Subgraphs after Vertices have been reseted using its edges
	 * and updated the supply usage of the corresponding supply vertex
	 * @param fixedSets a list of Subgraphs
	 */
	public static void rebuildFixedSetVertices(List<SubGraph> fixedSets) {
		
		for(SubGraph subsInSet: fixedSets) {
			
			for(int i = 0; i <= subsInSet.getListOfEdges().size() - 1; i++) {
				Vertex pre = subsInSet.getListOfEdges().get(i).getStartingVertex();
				Vertex target = subsInSet.getListOfEdges().get(i).getTargetVertex();
				//rebuilds all the adjacent vertices
				pre.addAdjVertex(target);
				pre.setSuccessor(target);
				target.setPredecessor(pre);
				
                if(!pre.getIsSupplyVertex()) {
                	((DemandVertex)pre).setDemandAsCovered();
                }
                if(!target.getIsSupplyVertex()) {
                	((DemandVertex)target).setDemandAsCovered();
                }
				
				//Demand coverage needs to be adjusted
				
			}
			subsInSet.getSubgraphsSupplyVertex().useSupply(subsInSet.getSubsCovDemand());
			//System.out.println("cov:_" + subsInSet.getSubsCovDemand());
		}

	}
	


}
