import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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
	public static List<SubGraph> getFixedSets(MPGSDGraph g, int t, int m /*, int n, int k, int MaxStag*/) throws IOException{
		
		
		
		int amountOfSubGraphs = g.getNumberofSupplyVertexes();
		//TODO replace with arrayList?
		List<SubGraph> fixedSets = new LinkedList<SubGraph>();
		
		//m sets the amount of Solution out of which the fixed sets should be formed
		SolvedGraph[] arrayOfBestGreedySolutions = new SolvedGraph[m];
		
		//solves the given graph g i times
		for(int i = 0; i <= t; i++) {
			//TODO currently Graph gets created everytime, beacause otherwise Vertexes would be resettet
			
			
			//4 being random trait
			SolvedGraph JSONGraphSolution = GreedyMPGSDSolver.GreedySolve2(g, 4);
			
			int covdem = JSONGraphSolution.getTotalCoveredDemand();
			int totaldem = JSONGraphSolution.getTotalOriginalDemand();
			double percentCovered = (double)covdem / (double)totaldem;
			
			//if the solution graph full fills a certain percentage of covered demand, it is viewed as one of the best solutions
			if(percentCovered > 0.5) {
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
				for(int j2 = 0; j2 <= m - 1; j2++) {
					if(arrayOfBestGreedySolutions[j2] != null && placefound == false) {
						if(arrayOfBestGreedySolutions[j2].getTotalCoveredDemand() < JSONGraphSolution.getTotalCoveredDemand()) {
							arrayOfBestGreedySolutions[j2] = JSONGraphSolution;
							
							break;
						}
						
					}
				}
			}
		}
		
		//m - 1; == arrayOfBestGreedySolutions.length - 1;
		
		//arrayOfBestGreedySolutions now contains the m best solutions
		
		//TODO fined fixed sets/ analyze m best solutions
		
		
		LinkedList<LinkedList<SubGraph>> ListForEachSupply = new LinkedList<>();
		
		for(int n = 0; n <= amountOfSubGraphs - 1; n++) {
			LinkedList<SubGraph> subgraphsForOneSupply = new LinkedList<>();
			//get every 1st then 2nd etc... Subgraph of all Subgraphs
			for(int p = 0; p <= arrayOfBestGreedySolutions.length - 1; p++) {
				
				SolvedGraph solvOne = arrayOfBestGreedySolutions[p];
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
	
	//private static SubGraph findFixedSet(LinkedList<SubGraph> subgraphsForOneSupply)
	
	

	private static SubGraph findFixedSet(LinkedList<SubGraph> subgraphsForOneSupply, MPGSDGraph g) {
		Map<String, Integer> edgeFrequency = new HashMap<>();
		
		for(int i = 0; i <= subgraphsForOneSupply.size() - 1;i++) {
			
			String[] edges = subgraphsForOneSupply.get(i).getSubgraphsEdgesStringArray();
			for(int j = 0; j<= edges.length - 1; j++) {
				edgeFrequency.merge(edges[j], 1, Integer::sum);
			}
			//System.out.println("Edges: " + i + subgraphsForOneSupply.get(i).getArrayOfEdgesAsString()); 
		}
		System.out.println(edgeFrequency); 
		
		// Determine the threshold for an edge to be considered common, e.g., appears in more than half of the subgraphs
		// TODO at least 2 otherwise vertices could be occuring in more than one subgraph, which is to be forbidden
	    int threshold = subgraphsForOneSupply.size() / 2;
	    
	    // Collect all edges that meet the frequency threshold
	    Set<String> commonEdges = new HashSet<>();
	    for (Map.Entry<String, Integer> entry : edgeFrequency.entrySet()) {
	        if (entry.getValue() > threshold) {
	            commonEdges.add(entry.getKey());
	        }
	    }
	    
	    if (!commonEdges.isEmpty()) {
	        // Create a new subgraph using the common edges
	    	// can take any random subgraph, because they all share the same supply Vertex
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

	            if (!addedVertices.contains(startVertexId) && supplyVertex.getID() != startVertexId) {
	                fixedSet.addVertex(firstVertex);
	                if(!firstVertex.getIsSupplyVertex()) {
	                	((DemandVertex)firstVertex).setDemandAsCovered();
	                }
	                addedVertices.add(startVertexId);
	            }
	            if (!addedVertices.contains(targetVertexId) && supplyVertex.getID() != targetVertexId) {
	                fixedSet.addVertex(targetVertex);
	                if(!targetVertex.getIsSupplyVertex()) {
	                	((DemandVertex)targetVertex).setDemandAsCovered();
	                }
	                addedVertices.add(targetVertexId);
	            }
	            fixedSet.addEdge(firstVertex, targetVertex);
	           
	        }
	        //iterates over the new subgraph to determine its used demand and number of demand vertices
	        for(int k = 0; k <= fixedSet.getVertexList().size() - 1; k++) {
            	if(!fixedSet.getVertexList().get(k).getIsSupplyVertex()) {
            		DemandVertex demV = (DemandVertex)fixedSet.getVertexList().get(k);
            		fixedSet.addOneNumDemVer();
            		fixedSet.updateSubsCovDemand(demV.getDemand());
            		fixedSet.getSubgraphsSupplyVertex().useSupply(demV.getDemand());
            	}
            }
	        return fixedSet;
	    }
	    
	    //if no common edges found, just returns a subgraph with a supply Vertex as Start
		return new SubGraph(subgraphsForOneSupply.get(0).getSubgraphsSupplyVertex());
	}



	public static SolvedGraph getBestFSSolution(int iterations, MPGSDGraph g, int trait,  List<SubGraph> fixedSets) {
		//TODO doesnt work beacuse its not resettet, need to outsource the whole fixed set generation process
		// from above and put that after the reset in MPGSDSolver2 with FSS
		
		
		int currentBest = 0;
		SolvedGraph bestGraph = null;
		for(int i = 1; i <= iterations; i++) {
			
			//Resets the Vertices from previous solutions and rebuilds the Subsets
			GreedyMPGSDSolver.resetGraphVertices(g);
			rebuildFixedSetVertices(fixedSets);
			
			SolvedGraph solved = GreedyMPGSDSolver.GreedySolve2(g, trait, fixedSets);
			int currentDemCov = solved.getTotalCoveredDemand();
			if(currentBest < currentDemCov) {
				currentBest = currentDemCov;
				bestGraph = solved;
			}
		}
		//TODO dont know if needed!
		rebuildFixedSetVertices(bestGraph.getGraphOfSubgraphs());
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
		}
	}
	



}
