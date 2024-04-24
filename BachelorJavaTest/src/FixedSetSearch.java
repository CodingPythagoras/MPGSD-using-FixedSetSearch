import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import GraphStructures.MPGSDGraph;
import GraphStructures.SolvedGraph;
import GraphStructures.SubGraph;
import JSONtoGraph.GraphBuilder;
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
			
			
			//3 being random trait
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
						System.out.println("Hello null: " + i + " " +  j);
						placefound = true;
						break;
					}
				}
				//otherwise checks if the current solution outperforms another and replaces it
				for(int j2 = 0; j2 <= m - 1; j2++) {
					if(arrayOfBestGreedySolutions[j2] != null && placefound == false) {
						if(arrayOfBestGreedySolutions[j2].getTotalCoveredDemand() < JSONGraphSolution.getTotalCoveredDemand()) {
							arrayOfBestGreedySolutions[j2] = JSONGraphSolution;
							System.out.println("Helloj2: " + i + " " +  j2 + JSONGraphSolution.getSolvedGraphMathematical());
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

	            Vertex startVertex = g.getVertexById(startVertexId);
	            Vertex targetVertex = g.getVertexById(targetVertexId);
	            
	            startVertex.addAdjVertex(startVertex);

	            if (!addedVertices.contains(startVertexId) && supplyVertex.getID() != startVertexId) {
	                fixedSet.addVertex(startVertex);
	                addedVertices.add(startVertexId);
	            }
	            if (!addedVertices.contains(targetVertexId) && supplyVertex.getID() != targetVertexId) {
	                fixedSet.addVertex(targetVertex);
	                addedVertices.add(targetVertexId);
	            }
	            fixedSet.addEdge(startVertex, targetVertex);
	        }
	        return fixedSet;
	    }
	    
	    //if no common edges found, just returns a subgraph with a supply Vertex as Start
		return new SubGraph(subgraphsForOneSupply.get(0).getSubgraphsSupplyVertex());
	}



	
	
	



}
