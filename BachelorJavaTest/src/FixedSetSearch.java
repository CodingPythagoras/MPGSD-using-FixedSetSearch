import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import GraphStructures.MPGSDGraph;
import GraphStructures.SolvedGraph;
import GraphStructures.SubGraph;
import JSONtoGraph.GraphBuilder;
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
	public static List<SubGraph> getFixedSets(MPGSDGraph g, int t, int m, int n, int k, int MaxStag){
		//TODO replace with arrayList?
		List<SubGraph> fixedSets = new LinkedList<SubGraph>();
		//m sets the amount of Solution out of which the fixed sets should be formed
		SolvedGraph[] arrayOfBestGreedySolutions = new SolvedGraph[m];
		
		//solves the given graph g i times
		for(int i = 0; i <= t; i++) {
			SolvedGraph JSONGraphSolution = GreedyMPGSDSolver.GreedySolve2(g, 3);
			int covdem = JSONGraphSolution.getTotalCoveredDemand();
			int totaldem = JSONGraphSolution.getTotalOriginalDemand();
			double percentCovered = (double)covdem / (double)totaldem;
			
			//if the solution graph full fills a certain percentage of covered demand, it is viewed as one of the best solutions
			if(percentCovered > 0.5) {
				for(int j = 0; j <= m - 1; j++) {
					//looks for a free space in the array
					if(arrayOfBestGreedySolutions[j] == null) {
						arrayOfBestGreedySolutions[j] = JSONGraphSolution;
						//otherwise checks if the current solution outperforms another and replaces it
					}else if(arrayOfBestGreedySolutions[j].getTotalCoveredDemand() < JSONGraphSolution.getTotalCoveredDemand()) {
						arrayOfBestGreedySolutions[j] = JSONGraphSolution;
					}
				}
			}
		}
		
		//m - 1; == arrayOfBestGreedySolutions.length - 1;
		//arrayOfBestGreedySolutions now contains the m best solutions
		for (int y = 0; y <= m - 1; y++) {
			fixedSets.add(findFixedSets(arrayOfBestGreedySolutions));
		}
		//TODO analyze m best solutions
		return fixedSets;
		
	}
	
	private static SubGraph findFixedSets(SolvedGraph[] sub) {
		//[solution1][sol2][sol3][][][][][][] usw...
		//since all were created the same way, by iterating through g.supplyList all should have the same order of subgraphs
		int counter;
		SolvedGraph[] arrayOfBestGreedySolutions = sub;
		
		
		
	}

}
