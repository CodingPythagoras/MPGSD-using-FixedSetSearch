import java.io.IOException;
import java.util.List;

import GraphStructures.MPGSDGraph;
import GraphStructures.SolvedGraph;
import GraphStructures.SubGraph;
import JSONtoGraph.GraphBuilder;

public class Main {

	public static void main(String[] args) throws IOException {
	
		System.out.println("Building MPGSD graphs");
			
		MPGSDGraph testInstance_100_300 = GraphBuilder.buildGraphFromJson("src\\JSONforGraph\\snake_updated_version_19072024_100x300.json");
		
		MPGSDGraph testInstance2_100_300 = GraphBuilder.buildGraphFromJson("src\\JSONforGraph\\updated_snake_high_23072024_test1_100x300.json");
		System.out.println("Finished");
		
		//solveGraphUsingFixedSetsSearch(testInstance_100_300, 100, 10, 20, 1);
		solveGraphUsingTraits(testInstance_100_300);
		
		int optimalSolutionsForReffernce = 3346; // As reference, to later determine the relative error
		int testResultsForAvg = 100; // How many times our problem sould be solved to collect our data
		
		int iterationsToFindFixedSet = 100; // Number of GreedySolutions to determine our fixed set
		int mBestSolutionsToBeConsidered = 10; // Number of best solutions out of these greedy iterations to consider for our fixed set search
		int solvingTraitForGreedyWithFixedSet = 1; // Trait which is used to generate a final solution for the Problem, which given fixed set
		
		

		giveTestResultsToGraph(testInstance_100_300, iterationsToFindFixedSet, mBestSolutionsToBeConsidered, solvingTraitForGreedyWithFixedSet, optimalSolutionsForReffernce, testResultsForAvg);
		

		
		
		
	}
	
	private static void createAndSolveGraph(MPGSDGraph JSONPath) throws IOException {
		
		SolvedGraph JSONGraphSolution = GreedyMPGSDSolver.GreedySolve2(JSONPath, 1);
		
		String coverageJSON = GreedyMPGSDSolver.getDemandCoverage(JSONGraphSolution);
		//System.out.println(JSONGraphSolution.getSolvedGraphMathematical());
		System.out.println(coverageJSON);
		
		
	}
	
	private static void solveGraphUsingTraits(MPGSDGraph JSONPath) {
		
		SolvedGraph JSONGraphSolution1 = GreedyMPGSDSolver.GreedySolve2(JSONPath, 1);
		String coverageJSONTrait1 = GreedyMPGSDSolver.getDemandCoverage(JSONGraphSolution1);
		System.out.println(coverageJSONTrait1);
		
		SolvedGraph JSONGraphSolution2 = GreedyMPGSDSolver.GreedySolve2(JSONPath, 2);
		String coverageJSONTrait2 = GreedyMPGSDSolver.getDemandCoverage(JSONGraphSolution2);
		System.out.println(coverageJSONTrait2);
		
		SolvedGraph JSONGraphSolution3 = GreedyMPGSDSolver.GreedySolve2(JSONPath, 3);
		String coverageJSONTrait3 = GreedyMPGSDSolver.getDemandCoverage(JSONGraphSolution3);
		System.out.println(coverageJSONTrait3);
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
	private static void solveGraphUsingFixedSetsSearch(MPGSDGraph g, int greedyIterations, int consideredSolutions, int iterationsWithFS, int solvingTrait, int optimalSolutionsForReffernce) throws IOException
	{
		System.out.println("Initialize solution with FSS");
		long startTime = System.currentTimeMillis();
		List<SubGraph> fixedsetsfound = FixedSetSearch.getFixedSets(g, greedyIterations, consideredSolutions, 0.7);
		
		//System.out.println("Sup ID: " + fixedsetsfound.get(5).getSubgraphsSupplyVertex().getID());
		//SolvedGraph FSSJSONGraphSolution = GreedyMPGSDSolver.GreedySolve2(g1, 1, fixedsetsfound);
		
		SolvedGraph FSSSolutionOfIterations = FixedSetSearch.getBestFSSolution(iterationsWithFS, g, solvingTrait, fixedsetsfound);
		long estimatedTime = System.currentTimeMillis() - startTime;
		
		//just visual
		SolvedGraph fixedSet = new SolvedGraph(g, fixedsetsfound);
		System.out.println("FixedSet" + fixedSet.getSolvedGraphMathematical());
		String coverageFSSIt = GreedyMPGSDSolver.getDemandCoverage(FSSSolutionOfIterations);
		
		//System.out.println(FSSSolutionOfIterations.getSolvedGraphMathematical());
		System.out.println(coverageFSSIt);
		
		
		System.out.println("Problem solved in: " + estimatedTime + " milliseconds");
		
	}
	
	private static void giveTestResultsToGraph(MPGSDGraph g, int greedyIterations, int consideredSolutions, int FSSgreedySolvingTrait, int optimalSolutionsForReffernce, int testResultsForAvg) throws IOException {
		int testResults = testResultsForAvg;//Assuming we want 10 solutions to get our median: testResults = 10
		long solutionArray[][] = new long[2][testResults]; 
		double avgTime;
		double avgDem;
		double totalDem = 0;
		double totalTime = 0;
		
		for (int i = 0; i < testResults; i++) {
			
			long startTime = System.currentTimeMillis();
			
			List<SubGraph> fixedsetsfound = FixedSetSearch.getFixedSets(g, greedyIterations, consideredSolutions, 0.7);
			SolvedGraph FSSSolution = FixedSetSearch.getBestFSSolution(1, g, FSSgreedySolvingTrait, fixedsetsfound);
			
			
			long estimatedTime = System.currentTimeMillis() - startTime;
			solutionArray[0][i] = (long) FSSSolution.getTotalCoveredDemand();
			solutionArray[1][i] = estimatedTime;
			System.out.println(i + ":= " + solutionArray[0][i] + " covered demand, " + solutionArray[1][i] + " ms");
			totalTime = totalTime + solutionArray[1][i];
			totalDem = totalDem + solutionArray[0][i];
			
		}
		avgDem = totalDem/testResults;
		avgTime = totalTime/testResults;
		
		double optimalSolution = (double) optimalSolutionsForReffernce;
		double relativeError;
		double avgRelativeError = (1 - ((double) avgDem / optimalSolution)) * 100;;
		double maxRelativeError = 0;
		double standardDeviation;
		double x = 0;
		
		for (int j = 0; j < testResults; j++) {
			relativeError = (1 - ((double) solutionArray[0][j] / optimalSolution)) * 100;
			x += ((relativeError - avgRelativeError) * (relativeError - avgRelativeError)); // standard deviation without square root
			
			if(relativeError > maxRelativeError) {
				maxRelativeError = relativeError;
				
			}
		}
		x = x / (testResults);
		standardDeviation = Math.sqrt(x);
		
		System.out.println("Average demand covered" + ": " + avgDem + " Average time: " + avgTime + " ms");
		System.out.println("Average relative error" + ": " + avgRelativeError + "%," + " Max relative error: " + maxRelativeError + "%," + " Standard deviation: " + standardDeviation + "%");
	}
	
	private static SolvedGraph getFSSSolution (MPGSDGraph g, int greedyIterations, int consideredSolutions, int iterationsWithFS, int FSSgreedySolvingTrait) throws IOException {
		List<SubGraph> fixedsetsfound = FixedSetSearch.getFixedSets(g, greedyIterations, consideredSolutions, 0.7);
		SolvedGraph FSSSolution = FixedSetSearch.getBestFSSolution(iterationsWithFS, g, FSSgreedySolvingTrait, fixedsetsfound);
	
		return FSSSolution;
	
	}
	
	//TODO Theshhold can be removed also in text/ iterations with FS can be set to 1 if we only se trait 1-3
	
	
	
}

