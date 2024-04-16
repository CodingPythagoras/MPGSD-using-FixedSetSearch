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
		SolvedGraph[] arrayOfBestGreedySolutions = new SolvedGraph[m];
		for(int i = 0; i <= t; i++) {
			SolvedGraph JSONGraphSolution = GreedyMPGSDSolver.GreedySolve2(g, 3);
			int covdem = JSONGraphSolution.getTotalCoveredDemand();
			int totaldem = JSONGraphSolution.getTotalOriginalDemand();
			double percentCovered = (double)covdem / (double)totaldem;
			if(percentCovered > 0.5) {
				for(int j = 0; j <= m - 1; j++) {
					if(arrayOfBestGreedySolutions[j].getTotalCoveredDemand() < JSONGraphSolution.getTotalCoveredDemand() || arrayOfBestGreedySolutions[j] == null) {
						arrayOfBestGreedySolutions[j] = JSONGraphSolution;
					}
				}
			}
		}
		
		
		//arrayOfBestGreedySolutions now contains the 10 best solutions
		
		//TODO analyse 10 best solutions
	}

}
