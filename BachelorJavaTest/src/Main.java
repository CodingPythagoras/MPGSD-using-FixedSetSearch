import java.io.IOException;
import java.util.List;

import GraphStructures.MPGSDGraph;
import GraphStructures.SolvedGraph;
import GraphStructures.SubGraph;
import JSONtoGraph.GraphBuilder;

public class Main {

	public static void main(String[] args) throws IOException {
	
		System.out.println("Building MPGSD graphs");
		MPGSDGraph failureTest = GraphBuilder.buildGraphFromJson("src\\JSONforGraph\\failureTest_01_connectivityFS.json");
		MPGSDGraph ansatzTwo = GraphBuilder.buildGraphFromJson("src\\JSONforGraph\\large_graph_ansatzTwo_100x1000.json");
		
		MPGSDGraph ansatzTwoPt2 = GraphBuilder.buildGraphFromJson("src\\JSONforGraph\\large_graph_LowConnectivityAnsatzTwo_100x1000.json");
		MPGSDGraph ansatzTwoPt3 = GraphBuilder.buildGraphFromJson("src\\JSONforGraph\\large_graph_HighConnectivityAnsatzTwo_100x1000.json");
		
		MPGSDGraph ansatzThree = GraphBuilder.buildGraphFromJson("src\\JSONforGraph\\large_graph_SnakeAnsatzTwo_100x1000.json");
		MPGSDGraph ansatzFour = GraphBuilder.buildGraphFromJson("src\\JSONforGraph\\snake_graph_100x300.json");
		
		MPGSDGraph g1 = GraphBuilder.buildGraphFromJson("src\\JSONforGraph\\graph-config.json");
		MPGSDGraph g2 = GraphBuilder.buildGraphFromJson("src\\JSONforGraph\\graph-config-2.json");
		MPGSDGraph g3 = GraphBuilder.buildGraphFromJson("src\\JSONforGraph\\graph-config-3.json");
		MPGSDGraph g4 = GraphBuilder.buildGraphFromJson("src\\JSONforGraph\\graph_100x300.json");
		MPGSDGraph g5 = GraphBuilder.buildGraphFromJson("src\\JSONforGraph\\graph_v2_100x300.json");
		MPGSDGraph g400_8000 = GraphBuilder.buildGraphFromJson("src\\JSONforGraph\\graph_400x8000.json");
		MPGSDGraph g400_4000 = GraphBuilder.buildGraphFromJson("src\\JSONforGraph\\large_graph_400x4000.json");
		System.out.println("Finished");
		
		solveGraphUsingFixedSetsSearch(ansatzFour, 2000, 10, 1000, 4);
		createAndSolveGraph(ansatzFour);
		
		//random vertex
		//SolvedGraph greedyX = GreedyMPGSDSolver.GreedySolveXTimes(5000, g400_4000);
		
	
		
		//SolvedGraph optimal = OptimalTest.findOptimalSolution(g4);
		
		//System.out.println(optimal.getTotalUsedSupply());
		
		
		
	}
	
	private static void createAndSolveGraph(MPGSDGraph JSONPath) throws IOException {
		
		SolvedGraph JSONGraphSolution = GreedyMPGSDSolver.GreedySolve2(JSONPath, 1);
		
		String coverageJSON = GreedyMPGSDSolver.getDemandCoverage(JSONGraphSolution);
		//System.out.println(JSONGraphSolution.getSolvedGraphMathematical());
		System.out.println(coverageJSON);
		
		
	}
	
	//TODO Greedy trait could be added
	/**
	 * 
	 * @param g MPGSDGraph which should be solved
	 * @param greedyIterations iterations used to identify the fixed sets
	 * @param consideredSolutions what is the maximum number of "best" solutions that should be considered
	 * @param iterationsWithFS how many random iterations with the fixedSet should be performed
	 * @param solvingTrait select the trait trough which the FSS should be solved, if more than 1 iteration random trait int = "4" is recommended
	 * @throws IOException
	 */
	private static void solveGraphUsingFixedSetsSearch(MPGSDGraph g, int greedyIterations, int consideredSolutions, int iterationsWithFS, int solvingTrait) throws IOException
	{
		System.out.println("Initialize solution with FSS");
		long startTime = System.currentTimeMillis();
		List<SubGraph> fixedsetsfound = FixedSetSearch.getFixedSets(g, greedyIterations, consideredSolutions, 0.7);
		
		//System.out.println("Sup ID: " + fixedsetsfound.get(5).getSubgraphsSupplyVertex().getID());
		//SolvedGraph FSSJSONGraphSolution = GreedyMPGSDSolver.GreedySolve2(g1, 1, fixedsetsfound);
		SolvedGraph fixedSet = new SolvedGraph(g, fixedsetsfound);
		System.out.println("FixedSet" + fixedSet.getSolvedGraphMathematical());
		
		SolvedGraph FSSSolutionOfIterations = FixedSetSearch.getBestFSSolution(iterationsWithFS, g, solvingTrait, fixedsetsfound);
		
		String coverageFSSIt = GreedyMPGSDSolver.getDemandCoverage(FSSSolutionOfIterations);
		
		//System.out.println(FSSSolutionOfIterations.getSolvedGraphMathematical());
		System.out.println(coverageFSSIt);
		
		long estimatedTime = System.currentTimeMillis() - startTime;
		System.out.println("Problem solved in: " + estimatedTime + " milliseconds");
		
	}
}
