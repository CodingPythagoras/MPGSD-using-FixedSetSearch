import java.io.IOException;
import java.util.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import GraphStructures.MPGSDGraph;
import GraphStructures.SolvedGraph;
import GraphStructures.SubGraph;
import VertexStructure.DemandVertex;
import VertexStructure.SupplyVertex;
import VertexStructure.Vertex;


/**
 * contains methods for getting the fixed set, rebuilding it
 * as well as finding the best solution after generating the fixed set
 * @author Manuel
 *
 */
public class FixedSetSearch {

	
	/**
	 * TODO shorten, like in pseudo-code?
	 * 
	 * Algorithm for generating t amount of solutions and finding a corresponding fixed set out m best of these solutions
	 * @param g MPGSD Graph to be solved
	 * @param t how many greedy solutions should be performed
	 * @param m how many solutions for the FSS should be considered
	 * @param threshold what percentage in 0. of supply should be covered to be considered (0.7 = 70% of supply coverage)
	 * @return A list of with subgraphs, one for each supply vertex
	 * @throws IOException
	 */
	public static List<SubGraph> getFixedSets(MPGSDGraph g, int t, int m, double threshold) throws IOException{
		
		
		
		int amountOfSubGraphs = g.getNumberofSupplyVertexes();
		List<SubGraph> fixedSets = new ArrayList<SubGraph>();
		
		//m sets the amount of Solution out of which the fixed sets should be formed
		SolvedGraph[] arrayOfBestGreedySolutions = new SolvedGraph[m];
		
		//solves the given graph g t times
		for(int i = 0; i <= t; i++) {
			
			
			//4 being random trait, 5 random vertex
			SolvedGraph JSONGraphSolution = GreedyMPGSDSolver.greedySolve(g, 4);
			
			int covSup = JSONGraphSolution.getTotalCoveredDemand();
			int totalSup = JSONGraphSolution.getTotalGivenSupply();
			double supPercentCovered = (double)covSup / (double)totalSup;
			
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
					
					//scans for the worst element in array
					for(int j2 = 0; j2 <= m - 1; j2++) {
						int currentPerformance = arrayOfBestGreedySolutions[j2].getTotalCoveredDemand();
						if(currentPerformance < worstPerformance) {
							worstPerformance = currentPerformance;
							worstIndex = j2;
						}
					}
					//checks if worse element is outperformed by our JSONGraphSolution
					if(worstIndex != -1 && worstPerformance < JSONGraphSolution.getTotalCoveredDemand()) {
						arrayOfBestGreedySolutions[worstIndex] = JSONGraphSolution;
					}
				}
				
			}
		}
		
		if(arrayOfBestGreedySolutions[0] == null) {
			JOptionPane.showMessageDialog(new JFrame(), "No best Solutions could be found: please reduce Threshold.");
			//create a solved graph with only supply vertices
			SolvedGraph replacementGraph = new SolvedGraph(g);
			arrayOfBestGreedySolutions[0] = replacementGraph;
		}
		
		//arrayOfBestGreedySolutions now contains the m best solutions
		
		ArrayList<ArrayList<SubGraph>> listForEachSupply = new ArrayList<>();
		
		for(int n = 0; n <= amountOfSubGraphs - 1; n++) {
			ArrayList<SubGraph> subgraphsForOneSupply = new ArrayList<>();
			//get every 1st then 2nd etc... Subgraph of all Subgraphs
			for(int p = 0; p <= arrayOfBestGreedySolutions.length - 1; p++) {
				SolvedGraph solvOne = arrayOfBestGreedySolutions[p];
				if(solvOne == null) {
					break;
				}
				subgraphsForOneSupply.add(solvOne.getSubgraph(n));   
				
		    }
			listForEachSupply.add(subgraphsForOneSupply);
			
		}
	
		//reset Vertices now, because they get changed by creation of the subgraphs
		GreedyMPGSDSolver.resetGraphVertices(g);
		//now ListForEachSupply.get(0) should contain all subgraphs representing the Subgraphs containing SupplyVertex1 and so on...
		for(int x = 0; x <= listForEachSupply.size() - 1; x++) {
			SubGraph fixedSet = findFixedSet(listForEachSupply.get(x), g);
			fixedSets.add(fixedSet);
		}
		
		
		
		
		return fixedSets;
		
	}
	

	
	/**
	 * finds the fixed set for an amount of subgraphs, all containing the same supply vertex, by analyzing its edge frequency
	 * @param subgraphsForOneSupply subgraphs, all containing the same supply vertex
	 * @param g MPGSDGraph used to find the vertices by ID
	 * @return
	 */
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
		// 0.5 can be changed by the user, to a higher value, yet 0.5 will always be the minimum, otherwise vertices could occur in multiple subgraphs
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
	    	
	        SubGraph commonSet = new SubGraph(supplyVertex); // Initialize with the common supply vertex
	     
	        // Add vertices and edges to the fixed set
	        Set<Integer> addedVertices = new HashSet<>();
	        addedVertices.add(supplyVertex.getID());
	        
	        //creates the fixedSet using the common edges
	        for (String edge : commonEdges) {
	       
	            String[] parts = edge.split("_");
	            int startVertexId = Integer.parseInt(parts[0]);
	            int targetVertexId = Integer.parseInt(parts[1]);
	            //rebuilds the vertices
	            Vertex firstVertex = g.getVertexById(startVertexId);
	            Vertex targetVertex = g.getVertexById(targetVertexId);
	            
	            firstVertex.addAdjVertex(targetVertex);
	            
	            firstVertex.setSuccessor(targetVertex);
	            targetVertex.setPredecessor(firstVertex);

	            
	            //then adds them to the common set
	            if (!addedVertices.contains(startVertexId) && supplyVertex.getID() != startVertexId && supplyVertex.getRemainingSupply() >= ((DemandVertex)firstVertex).getDemand()) {
	                commonSet.addVertex(firstVertex);
	                if(!firstVertex.getIsSupplyVertex()) {
	                	((DemandVertex)firstVertex).setDemandAsCovered();
	                	supplyVertex.useSupply(((DemandVertex)firstVertex).getDemand());
	                }
	                addedVertices.add(startVertexId);
	            }
	            if (!addedVertices.contains(targetVertexId) && supplyVertex.getID() != targetVertexId && supplyVertex.getRemainingSupply() >= ((DemandVertex)targetVertex).getDemand()) {
	                commonSet.addVertex(targetVertex);
	                if(!targetVertex.getIsSupplyVertex()) {
	                	((DemandVertex)targetVertex).setDemandAsCovered();
	                	supplyVertex.useSupply(((DemandVertex)targetVertex).getDemand());
	                }
	                addedVertices.add(targetVertexId);
	                commonSet.addEdge(firstVertex, targetVertex);
	            }
	            

	        }
	
	        
	        //only resets supply, to later just add the supply of the extracted component back
	        //this way we don't reset all the vertices, which would cause them to be disconnected
	        //Resets supply vertex, to later only cover the supply of the connected components
	        commonSet.getSubgraphsSupplyVertex().onlyResetSupply();
	        
	        //Checks for connectivity and if not extracts the part connected to the supply vertex
	        SubGraph fixedSet = commonSet.extractConnectedComponent();

	        
	        //TODO could be moved into the extractConnectedComponent() method
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
				
				
				
			}
			//Demand coverage gets adjusted
			subsInSet.getSubgraphsSupplyVertex().useSupply(subsInSet.getSubsCovDemand());
		}

	}
	
	/**
	 * takes the fixed set, solves it a given amount of times using a given trait and returns the best found solution out of these
	 * @param iterations to solve the MPGSD using the fixed set
	 * @param g MPGSDGraph to be solved
	 * @param trait for vertex selection
	 * @param fixedSets fixed set used as initial solution, to build upon
	 * @return
	 */
	public static SolvedGraph getBestFSSolution(int iterations, MPGSDGraph g, int trait,  List<SubGraph> fixedSets) {
		
		//TODO if printing fixedSet here before rebuild sometimes predecessor is null
		
		int currentBest = 0;
		SolvedGraph bestGraph = null;

		for(int i = 1; i <= iterations; i++) {
		
			
			//Resets the Vertices from previous solutions and rebuilds the Subsets
			GreedyMPGSDSolver.resetGraphVertices(g);
			rebuildFixedSetVertices(fixedSets);
			
			SolvedGraph solved = GreedyMPGSDSolver.greedySolve(g, trait, fixedSets);
			
			int currentDemCov = solved.getTotalCoveredDemand();
			
			if(currentBest < currentDemCov) {
				
				currentBest = currentDemCov;
				bestGraph = solved;
			}
			
		}
		//rebuild the best solution one last time, to then be able to display the SolvedGraph
		rebuildFixedSetVertices(bestGraph.getGraphOfSubgraphs());
		return bestGraph;
	}
	


}
