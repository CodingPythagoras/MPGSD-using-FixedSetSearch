import java.io.IOException;
import java.util.List;

import GraphStructures.MPGSDGraph;
import GraphStructures.SolvedGraph;
import GraphStructures.SubGraph;
import JSONtoGraph.GraphBuilder;

public class Main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("Hello World");
		
		//createAndSolveGraph("src\\JSONforGraph\\graph-config.json");
		
		System.out.println("Finished");
		
		
		System.out.println("Initialize solution with FSS");
		MPGSDGraph g1 = GraphBuilder.buildGraphFromJson("src\\JSONforGraph\\graph-config.json");
		MPGSDGraph g3 = GraphBuilder.buildGraphFromJson("src\\JSONforGraph\\graph-config-3.json");

		solveGraphUsingFixedSetsSearch(g3, 100, 3, 1000, 4);
 
		
		
		
		
		
	}
	
	private static void createAndSolveGraph(String JSONPath) throws IOException {
		
		MPGSDGraph JSONGraph = GraphBuilder.buildGraphFromJson(JSONPath);
		
		SolvedGraph JSONGraphSolution = GreedyMPGSDSolver.GreedySolve2(JSONGraph, 1);
		
		String coverageJSON = GreedyMPGSDSolver.getDemandCoverage(JSONGraphSolution);
		
		System.out.println(coverageJSON);
		
		System.out.println(JSONGraphSolution.getSolvedGraphMathematical());
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
		List<SubGraph> fixedsetsfound = FixedSetSearch.getFixedSets(g, greedyIterations, consideredSolutions, 0.38);
		
		//System.out.println("Sup ID: " + fixedsetsfound.get(5).getSubgraphsSupplyVertex().getID());
		//SolvedGraph FSSJSONGraphSolution = GreedyMPGSDSolver.GreedySolve2(g1, 1, fixedsetsfound);
		
		SolvedGraph FSSSolutionOfIterations = FixedSetSearch.getBestFSSolution(100, g, solvingTrait, fixedsetsfound);

		String coverageFSSIt = GreedyMPGSDSolver.getDemandCoverage(FSSSolutionOfIterations);
		
		System.out.println(FSSSolutionOfIterations.getSolvedGraphMathematical());
		System.out.println(coverageFSSIt);
		
	}
}
