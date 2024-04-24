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
	public static List<SubGraph> getFixedSets(MPGSDGraph g, int t, int m /*, int n, int k, int MaxStag*/){
		
		//TODO replace with arrayList?
		List<SubGraph> fixedSets = new LinkedList<SubGraph>();
		
		//m sets the amount of Solution out of which the fixed sets should be formed
		SolvedGraph[] arrayOfBestGreedySolutions = new SolvedGraph[m];
		
		//solves the given graph g i times
		for(int i = 0; i <= t; i++) {
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
		int amountOfSubGraphs = g.getNumberofSupplyVertexes();
		
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
			SubGraph fixedSet = findFixedSet(ListForEachSupply.get(x));
			fixedSets.add(fixedSet);
		}
		
		
		
		
		return fixedSets;
		
	}
	
	//private static SubGraph findFixedSet(LinkedList<SubGraph> subgraphsForOneSupply)
	
	
	
	
	private static Map<List<Integer>, Integer> createFrequencyMap(List<SubGraph> subgraphs) {
	    Map<List<Integer>, Integer> frequencyMap = new HashMap<>();
	    for (SubGraph subGraph : subgraphs) {
	        List<Integer> vertexIds = subGraph.getVertexList().stream()
	                                           .map(Vertex::getID)
	                                           .collect(Collectors.toList());
	        frequencyMap.merge(vertexIds, 1, Integer::sum);  // Count each occurrence
	    }
	    return frequencyMap;
	}
	
	private static List<SubGraph> extractFixedSets(Map<List<Integer>, Integer> frequencyMap, int threshold) {
	    List<SubGraph> fixedSets = new ArrayList<>();
	    for (Map.Entry<List<Integer>, Integer> entry : frequencyMap.entrySet()) {
	        if (entry.getValue() >= threshold) {
	            // Convert list of IDs back into a subgraph if frequency is above threshold
	            List<Integer> commonVertices = entry.getKey();
	            SubGraph fixedSet = new SubGraph(new SupplyVertex(commonVertices.get(0), 0));  // Example
	            fixedSets.add(fixedSet);
	        }
	    }
	    return fixedSets;
	}
	
	 private static SubGraph findFixedSet(LinkedList<SubGraph> subgraphsForOneSupply) {
	        Map<Integer, Integer> vertexFrequency = new HashMap<>();
	        Map<String, Integer> edgeFrequency = new HashMap<>();

	        // Count the frequency of each vertex and each edge
	        for (SubGraph subgraph : subgraphsForOneSupply) {
	            Set<Integer> visitedVertices = new HashSet<>();  // To prevent counting vertices multiple times in the same subgraph
	            List<Vertex> vertices = subgraph.getVertexList();

	            for (int i = 0; i < vertices.size(); i++) {
	                Vertex v = vertices.get(i);
	                if (!visitedVertices.contains(v.getID())) {
	                    vertexFrequency.merge(v.getID(), 1, Integer::sum);
	                    visitedVertices.add(v.getID());
	                }

	                // Count edges if there is a subsequent vertex
	                if (i < vertices.size() - 1) {
	                    Vertex nextVertex = vertices.get(i + 1);
	                    String edge = v.getID() + "-" + nextVertex.getID();
	                    edgeFrequency.merge(edge, 1, Integer::sum);
	                }
	            }
	        }

	        // Filter vertices and edges that appear in at least half of the subgraphs
	        int threshold = subgraphsForOneSupply.size() / 2;
	        List<Integer> commonVertices = vertexFrequency.entrySet().stream()
	                                                      .filter(entry -> entry.getValue() > threshold)
	                                                      .map(Map.Entry::getKey)
	                                                      .collect(Collectors.toList());

	        List<String> commonEdges = edgeFrequency.entrySet().stream()
	                                                .filter(entry -> entry.getValue() > threshold)
	                                                .map(Map.Entry::getKey)
	                                                .collect(Collectors.toList());

	        // Construct the fixed set SubGraph
	        if (!commonVertices.isEmpty()) {
	            SubGraph fixedSet = new SubGraph(new SupplyVertex(commonVertices.get(0), 0)); // Example, adjust accordingly

	            // Add vertices
	            for (Integer vertexId : commonVertices) {
	                fixedSet.addVertex(new Vertex(vertexId)); // Assuming constructor exists
	            }

	            // Here, you would also need to add edges to `fixedSet` if your subgraph structure supports this
	            // This step is skipped here due to simplicity and lack of full context

	            return fixedSet;
	        }
	        
	        return null;  // No fixed set found
	    }
	

	
	
	



}
