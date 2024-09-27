import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import GraphStructures.MPGSDGraph;
import GraphStructures.SolvedGraph;
import GraphStructures.SubGraph;
import JSONtoGraph.GraphBuilder;


/**
 * main class, to launch all the different operations
 * containing some methods to solve multiple graphs in a row and saving its results a .txt file
 * for then extracting the results
 * @author Manuel
 *
 */
public class Main {

	public static void main(String[] args) throws IOException {
	
		System.out.println("Building MPGSD graphs");
		
		//For available graph Supply x Demand combination look into the thesis or in the JSONforGraph folder
		//This is an example of how to create a graph used in the tests (400x1200 with high connectivity/ false for low connectivity)
		MPGSDGraph graph = GraphBuilder.getGraphSupXDem(400, 1200, true);
		
	
		//This is an example of how to create a graph used for literature comparison by Jovanovic et al. (2015) 
		//(5x15 with true = general graph/ false for trees and a number from 0 to 39, cause there are 40 graphs for each combination)
		MPGSDGraph graphLiteratur = GraphBuilder.getLitGraphSupXDem(5, 15, true, 17);
		
		System.out.println("Finished building graphs");

		
		
		//Example methods explained below
		createAndSolveGraph(graph, 1);
		
		solveGraphUsingTraits(graphLiteratur);
		
		
		
		
		
		
		int testResultsForAvg = 5; // How many times our problem should be solved to collect our data
		
		int iterationsToFindFixedSet = 1000; // Number of GreedySolutions to determine our fixed set
		int mBestSolutionsToBeConsidered = 25; // Number of best solutions out of these greedy iterations to consider for our fixed set search
		int solvingTraitForGreedyWithFixedSet = 4; // Trait which is used to generate a final solution for the Problem, which given fixed set
		int solvesUsingFixedSet = 1000; // How many solves should be performed with the found fixed set to solve the problem
		double threshold = 0.7; // Percentage that must be achieved by a solution to be considered a good enough solution to be considered
		
		
		
		//Example methods in usage, please look up documentation
		solveGraphUsingFixedSetsSearch(graphLiteratur, iterationsToFindFixedSet, mBestSolutionsToBeConsidered, solvesUsingFixedSet, solvingTraitForGreedyWithFixedSet, threshold);
		
		giveTestResultsToGraph(graphLiteratur, iterationsToFindFixedSet, mBestSolutionsToBeConsidered, solvesUsingFixedSet, solvingTraitForGreedyWithFixedSet, threshold, testResultsForAvg);
		
		solveGraphUsingTraitsTXT(1);
		
		writeDownFSSTestResultsToGraph(iterationsToFindFixedSet, mBestSolutionsToBeConsidered, solvesUsingFixedSet, solvingTraitForGreedyWithFixedSet, threshold, testResultsForAvg, false);
		
		FSSResults2015LitGraphs(iterationsToFindFixedSet, mBestSolutionsToBeConsidered, solvesUsingFixedSet, solvingTraitForGreedyWithFixedSet, threshold, testResultsForAvg, false, 5, 15);
		
	
		
		
	}
	
	
	/**
	 * Solves a graph with just a plain greedy approach and a given trait
	 * and prints the result into the console
	 * @param g the MPGSD graph to be solved
	 * @param trait the trait used for solving
	 * @throws IOException
	 */
	private static void createAndSolveGraph(MPGSDGraph g, int trait) throws IOException {
		
		SolvedGraph JSONGraphSolution = GreedyMPGSDSolver.greedySolve(g, trait);
		
		String coverageJSON = GreedyMPGSDSolver.getDemandCoverage(JSONGraphSolution);
		
		System.out.println(coverageJSON);
		
		
	}
	
	
	/**
	 * Solves a graph with trait 1, 2 and 3 and prints the result in the console
	 * @param g MPGSD graph to be solved
	 */
	private static void solveGraphUsingTraits(MPGSDGraph g) {
		
		SolvedGraph JSONGraphSolution1 = GreedyMPGSDSolver.greedySolve(g, 1);
		String coverageJSONTrait1 = GreedyMPGSDSolver.getDemandCoverage(JSONGraphSolution1);
		System.out.println("Trait 1: " + coverageJSONTrait1 );
		

		SolvedGraph JSONGraphSolution2 = GreedyMPGSDSolver.greedySolve(g, 2);
		String coverageJSONTrait2 = GreedyMPGSDSolver.getDemandCoverage(JSONGraphSolution2);
		System.out.println("Trait 2: " + coverageJSONTrait2);
		

		SolvedGraph JSONGraphSolution3 = GreedyMPGSDSolver.greedySolve(g, 3);
		String coverageJSONTrait3 = GreedyMPGSDSolver.getDemandCoverage(JSONGraphSolution3);
		System.out.println("Trait 3: " + coverageJSONTrait3);
		
	
	}
	
	
	/**
	 * solves a specific graph using the fixed set, printing the results into the console
	 * @param g MPGSDGraph which should be solved
	 * @param greedyIterations iterations used to identify the fixed sets
	 * @param consideredSolutions what is the maximum number of "best" solutions that should be considered
	 * @param iterationsWithFS how many random iterations with the fixedSet should be performed
	 * @param solvingTrait select the trait trough which the FSS should be solved, if more than 1 iteration random trait int = "4" is recommended
	 * @param threshold percent of supply coverage, which must be achieved to be considered a good enough solution (0.7 being 70%)
	 * @throws IOException
	 */
	private static void solveGraphUsingFixedSetsSearch(MPGSDGraph g, int greedyIterations, int consideredSolutions, int iterationsWithFS, int solvingTrait, double threshold) throws IOException
	{
		System.out.println("Initialize solution with FSS");
		long startTime = System.currentTimeMillis();
		List<SubGraph> fixedsetsfound = FixedSetSearch.getFixedSets(g, greedyIterations, consideredSolutions, threshold);
		
		SolvedGraph FSSSolutionOfIterations = FixedSetSearch.getBestFSSolution(iterationsWithFS, g, solvingTrait, fixedsetsfound);
		long estimatedTime = System.currentTimeMillis() - startTime;
		
		//just visual
		SolvedGraph fixedSet = new SolvedGraph(g, fixedsetsfound);
		System.out.println("FixedSetSolution:" + "\n" + fixedSet.getSolvedGraphMathematical());
		String coverageFSSIt = GreedyMPGSDSolver.getDemandCoverage(FSSSolutionOfIterations);
		
		System.out.println(coverageFSSIt);
		
		
		System.out.println("Problem solved in: " + estimatedTime + " milliseconds");
		
	}
	
	
	/**
	 * gives !average FSS results to multiple results! of the same graph and prints the average into the console
	 * @param g MPGSDGraph which should be solved
	 * @param greedyIterations iterations used to identify the fixed sets
	 * @param consideredSolutions what is the maximum number of "best" solutions that should be considered
	 * @param iterationsWithFS how many random iterations with the fixedSet should be performed
	 * @param solvingTrait select the trait trough which the problem should be solved after generating the FSS, if more than 1 iteration random trait int = "4" is recommended
	 * @param threshold percent of supply coverage, which must be achieved to be considered a good enough solution (0.7 being 70%)
	 * @param testResultsForAvg the amount of tests that should be performed with this configuration
	 * @throws IOException
	 */
	private static void giveTestResultsToGraph(MPGSDGraph g, int greedyIterations, int consideredSolutions, int solvesUsingFixedSet, int FSSgreedySolvingTrait, double threshold, int testResultsForAvg) throws IOException {
		int testResults = testResultsForAvg;//Assuming we want 10 solutions to get our median: testResults = 10
		long solutionArray[][] = new long[2][testResults]; 
		double avgTime;
		double avgDem;
		double totalDem = 0;
		double totalTime = 0;
		
		for (int i = 0; i < testResults; i++) {
			
			long startTime = System.currentTimeMillis();
			
			List<SubGraph> fixedsetsfound = FixedSetSearch.getFixedSets(g, greedyIterations, consideredSolutions, 0.7);
			SolvedGraph FSSSolution = FixedSetSearch.getBestFSSolution(solvesUsingFixedSet, g, FSSgreedySolvingTrait, fixedsetsfound);
			
			System.out.println(FSSSolution.getSolvedGraphMathematical());
			
			long estimatedTime = System.currentTimeMillis() - startTime;
			solutionArray[0][i] = (long) FSSSolution.getTotalCoveredDemand();
			solutionArray[1][i] = estimatedTime;
			System.out.println(i + ":= " + solutionArray[0][i] + " covered demand, " + solutionArray[1][i] + " ms");
			totalTime = totalTime + solutionArray[1][i];
			totalDem = totalDem + solutionArray[0][i];
			
		}
		avgDem = totalDem/testResults;
		avgTime = totalTime/testResults;
		
		double optimalSolution = (double) g.getTotalMPGSDSupply();
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * solves the MPGSD problem one for all graphs, without using the FSS, just plain greedy approach
	 * @param trait the trait for vertex selection
	 */
	private static void solveGraphUsingTraitsTXT(int trait) {
	    try (BufferedWriter writer = new BufferedWriter(new FileWriter("resultsRelativeErrorNormalTraits.txt"))) {
	    	
	    	
	    	
	    	
	    	Boolean highConnectivity = false;
	    	
	    	MPGSDGraph graph1 = GraphBuilder.getGraphSupXDem(2, 6, highConnectivity);
	    	MPGSDGraph graph2 = GraphBuilder.getGraphSupXDem(2, 10, highConnectivity);
	    	MPGSDGraph graph3 = GraphBuilder.getGraphSupXDem(2, 20, highConnectivity);
	    	MPGSDGraph graph4 = GraphBuilder.getGraphSupXDem(2, 40, highConnectivity);
	    	MPGSDGraph graph5 = GraphBuilder.getGraphSupXDem(5, 15, highConnectivity);
	    	MPGSDGraph graph6 = GraphBuilder.getGraphSupXDem(5, 25, highConnectivity);
	    	MPGSDGraph graph7 = GraphBuilder.getGraphSupXDem(5, 50, highConnectivity);
	    	MPGSDGraph graph8 = GraphBuilder.getGraphSupXDem(5, 100, highConnectivity);
	    	MPGSDGraph graph9 = GraphBuilder.getGraphSupXDem(10, 30, highConnectivity);
	    	MPGSDGraph graph10 = GraphBuilder.getGraphSupXDem(10, 50, highConnectivity);
	    	MPGSDGraph graph11 = GraphBuilder.getGraphSupXDem(10, 100, highConnectivity);
	    	MPGSDGraph graph12 = GraphBuilder.getGraphSupXDem(10, 200, highConnectivity);
	    	MPGSDGraph graph13 = GraphBuilder.getGraphSupXDem(25, 75, highConnectivity);
	    	MPGSDGraph graph14 = GraphBuilder.getGraphSupXDem(25, 125, highConnectivity);
	    	MPGSDGraph graph15 = GraphBuilder.getGraphSupXDem(25, 250, highConnectivity);
	    	MPGSDGraph graph16 = GraphBuilder.getGraphSupXDem(25, 500, highConnectivity);
	    	MPGSDGraph graph17 = GraphBuilder.getGraphSupXDem(50, 150, highConnectivity);
	    	MPGSDGraph graph18 = GraphBuilder.getGraphSupXDem(50, 250, highConnectivity);
	    	MPGSDGraph graph19 = GraphBuilder.getGraphSupXDem(50, 500, highConnectivity);
	    	MPGSDGraph graph20 = GraphBuilder.getGraphSupXDem(50, 1000, highConnectivity);
	    	MPGSDGraph graph21 = GraphBuilder.getGraphSupXDem(100, 300, highConnectivity);
	    	MPGSDGraph graph22 = GraphBuilder.getGraphSupXDem(100, 500, highConnectivity);
	    	MPGSDGraph graph23 = GraphBuilder.getGraphSupXDem(100, 1000, highConnectivity);
	    	MPGSDGraph graph24 = GraphBuilder.getGraphSupXDem(100, 2000, highConnectivity);
	    	MPGSDGraph graph25 = GraphBuilder.getGraphSupXDem(200, 600, highConnectivity);
	    	MPGSDGraph graph26 = GraphBuilder.getGraphSupXDem(200, 1000, highConnectivity);
	    	MPGSDGraph graph27 = GraphBuilder.getGraphSupXDem(200, 2000, highConnectivity);
	    	MPGSDGraph graph28 = GraphBuilder.getGraphSupXDem(200, 4000, highConnectivity);
	    	MPGSDGraph graph29 = GraphBuilder.getGraphSupXDem(400, 1200, highConnectivity);
	    	MPGSDGraph graph30 = GraphBuilder.getGraphSupXDem(400, 2000, highConnectivity);
	    	MPGSDGraph graph31 = GraphBuilder.getGraphSupXDem(400, 4000, highConnectivity);
	    	MPGSDGraph graph32 = GraphBuilder.getGraphSupXDem(400, 8000, highConnectivity);
	    	
	    	MPGSDGraph[] graphArray = {graph1, graph2, graph3, graph4, graph5, graph6, graph7, graph8, graph9, graph10, 
	    			graph11, graph12, graph13, graph14, graph15, graph16, graph17, graph18, graph19, graph20, graph21, 
	    			graph22, graph23, graph24, graph25, graph26, graph27, graph28, graph29, graph30, graph31, graph32};
	    	
	    	
	    	for(int i = 0; i < graphArray.length; i++) {
	    
	    
		        SolvedGraph JSONGraphSolution1 = GreedyMPGSDSolver.greedySolve(graphArray[i], trait);
		        //Important add * 100 if want in %
		        double relativeError1 = (1 - ((double)JSONGraphSolution1.getTotalCoveredDemand() / graphArray[i].getTotalMPGSDSupply()));
		        writer.write(relativeError1 + "");
		        writer.newLine();
	    	}
	    	
	        
	        
	        
	        System.out.println("Working Directory = " + System.getProperty("user.dir"));
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * writes down the test results to all 32 graphs (highly connected or low connected) with a chosen configuration and amount of tests that should be considered to calculate the average
	 * and saves them in a .txt
	 * @param greedyIterations iterations used to identify the fixed sets
	 * @param consideredSolutions what is the maximum number of "best" solutions that should be considered
	 * @param solvesUsingFixedSet how many random iterations with the fixedSet should be performed
	 * @param FSSgreedySolvingTrait select the trait trough which the problem should be solved after generating the FSS, if more than 1 iteration random trait int = "4" is recommended
	 * @param threshold percent of supply coverage, which must be achieved to be considered a good enough solution (0.7 being 70%)
	 * @param testResultsForAvg the amount of tests that should be performed with this configuration
	 * @param connectivityHigh sets the connectivity high (true) or low (false)
	 * @throws IOException
	 */
	private static void writeDownFSSTestResultsToGraph(int greedyIterations, int consideredSolutions, int solvesUsingFixedSet, int FSSgreedySolvingTrait, double threshold, int testResultsForAvg, boolean connectivityHigh) throws IOException {
		
    	Boolean highConnectivity = connectivityHigh;
    	String connect = "";
    	if(highConnectivity == true) {
    		connect = "High";
    	}
    	if(highConnectivity == false) {
    		connect = "Low";
    	}
    	int testResults = testResultsForAvg;//Assuming we want 10 solutions to get our median: testResults = 10
    	String txtName = "Resul_" + testResultsForAvg + "Iter_" + greedyIterations + "mbest_" + consideredSolutions + "Trait_" + FSSgreedySolvingTrait + "FSSolves_" + solvesUsingFixedSet + "_" + connect;
    	
		BufferedWriter writerAvg = new BufferedWriter(new FileWriter("resultsFSSAvgRelativeError"+ txtName +".txt"));
		BufferedWriter writerMax = new BufferedWriter(new FileWriter("resultsFSSMaxRelativeError"+ txtName +".txt"));
		BufferedWriter writerSD = new BufferedWriter(new FileWriter("resultsFSSStandardDeviation"+ txtName +".txt"));
		BufferedWriter writerTime = new BufferedWriter(new FileWriter("resultsFSSTime"+ txtName +".txt"));
    	
		writerAvg.write("Please * 100 for getting the percentage!");
		writerMax.write("Please * 100 for getting the percentage!");
		writerSD.write("Numbers are already in %!");
		writerTime.write("Time is alredy in milliseconds!");
		
		writerAvg.newLine();
		writerMax.newLine();
		writerSD.newLine();
		writerTime.newLine();
		
		
    	MPGSDGraph graph1 = GraphBuilder.getGraphSupXDem(2, 6, highConnectivity);
    	MPGSDGraph graph2 = GraphBuilder.getGraphSupXDem(2, 10, highConnectivity);
    	MPGSDGraph graph3 = GraphBuilder.getGraphSupXDem(2, 20, highConnectivity);
    	MPGSDGraph graph4 = GraphBuilder.getGraphSupXDem(2, 40, highConnectivity);
    	MPGSDGraph graph5 = GraphBuilder.getGraphSupXDem(5, 15, highConnectivity);
    	MPGSDGraph graph6 = GraphBuilder.getGraphSupXDem(5, 25, highConnectivity);
    	MPGSDGraph graph7 = GraphBuilder.getGraphSupXDem(5, 50, highConnectivity);
    	MPGSDGraph graph8 = GraphBuilder.getGraphSupXDem(5, 100, highConnectivity);
    	MPGSDGraph graph9 = GraphBuilder.getGraphSupXDem(10, 30, highConnectivity);
    	MPGSDGraph graph10 = GraphBuilder.getGraphSupXDem(10, 50, highConnectivity);
    	MPGSDGraph graph11 = GraphBuilder.getGraphSupXDem(10, 100, highConnectivity);
    	MPGSDGraph graph12 = GraphBuilder.getGraphSupXDem(10, 200, highConnectivity);
    	MPGSDGraph graph13 = GraphBuilder.getGraphSupXDem(25, 75, highConnectivity);
    	MPGSDGraph graph14 = GraphBuilder.getGraphSupXDem(25, 125, highConnectivity);
    	MPGSDGraph graph15 = GraphBuilder.getGraphSupXDem(25, 250, highConnectivity);
    	MPGSDGraph graph16 = GraphBuilder.getGraphSupXDem(25, 500, highConnectivity);
    	MPGSDGraph graph17 = GraphBuilder.getGraphSupXDem(50, 150, highConnectivity);
    	MPGSDGraph graph18 = GraphBuilder.getGraphSupXDem(50, 250, highConnectivity);
    	MPGSDGraph graph19 = GraphBuilder.getGraphSupXDem(50, 500, highConnectivity);
    	MPGSDGraph graph20 = GraphBuilder.getGraphSupXDem(50, 1000, highConnectivity);
    	MPGSDGraph graph21 = GraphBuilder.getGraphSupXDem(100, 300, highConnectivity);
    	MPGSDGraph graph22 = GraphBuilder.getGraphSupXDem(100, 500, highConnectivity);
    	MPGSDGraph graph23 = GraphBuilder.getGraphSupXDem(100, 1000, highConnectivity);
    	MPGSDGraph graph24 = GraphBuilder.getGraphSupXDem(100, 2000, highConnectivity);
    	MPGSDGraph graph25 = GraphBuilder.getGraphSupXDem(200, 600, highConnectivity);
    	MPGSDGraph graph26 = GraphBuilder.getGraphSupXDem(200, 1000, highConnectivity);
    	MPGSDGraph graph27 = GraphBuilder.getGraphSupXDem(200, 2000, highConnectivity);
    	MPGSDGraph graph28 = GraphBuilder.getGraphSupXDem(200, 4000, highConnectivity);
    	MPGSDGraph graph29 = GraphBuilder.getGraphSupXDem(400, 1200, highConnectivity);
    	MPGSDGraph graph30 = GraphBuilder.getGraphSupXDem(400, 2000, highConnectivity);
    	MPGSDGraph graph31 = GraphBuilder.getGraphSupXDem(400, 4000, highConnectivity);
    	MPGSDGraph graph32 = GraphBuilder.getGraphSupXDem(400, 8000, highConnectivity);
    	
    	MPGSDGraph[] graphArray = {graph1, graph2, graph3, graph4, graph5, graph6, graph7, graph8, graph9, graph10, 
    			graph11, graph12, graph13, graph14, graph15, graph16, graph17, graph18, graph19, graph20, graph21, 
    			graph22, graph23, graph24, graph25, graph26, graph27, graph28, graph29, graph30, graph31, graph32};
    	
    	
		
    	for(int k = 0; k < graphArray.length; k++) {
    		System.out.println("Starting with graph " + (k + 1) );
    		
    		long solutionArray[][] = new long[2][testResults]; 
    		double avgTime;
    		double avgDem;
    		double totalDem = 0;
    		double totalTime = 0;
    		MPGSDGraph currentGraph = graphArray[k];
    		for (int i = 0; i < testResults; i++) {
    			
    			long startTime = System.currentTimeMillis();
    			
    			List<SubGraph> fixedsetsfound = FixedSetSearch.getFixedSets(currentGraph, greedyIterations, consideredSolutions, threshold);
    			SolvedGraph FSSSolution = FixedSetSearch.getBestFSSolution(solvesUsingFixedSet, currentGraph, FSSgreedySolvingTrait, fixedsetsfound);
    			
    			
    			long estimatedTime = System.currentTimeMillis() - startTime;
    			solutionArray[0][i] = (long) FSSSolution.getTotalCoveredDemand();
    			solutionArray[1][i] = estimatedTime;

    			totalTime = totalTime + solutionArray[1][i];
    			totalDem = totalDem + solutionArray[0][i];
    			
    		}
    		avgDem = totalDem/testResults;
    		avgTime = totalTime/testResults;
    		
    		double optimalSolution = (double) currentGraph.getTotalMPGSDSupply();
    		double relativeError = - 1;
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
    		
    		
    		double relativeErrorWithoutPercent = relativeError / 100;
    		writerAvg.write(relativeErrorWithoutPercent + "");
    		writerAvg.newLine();
    		
    		double maxErrorWithoutPercent = maxRelativeError / 100;
    		writerMax.write(maxErrorWithoutPercent + "");
    		writerMax.newLine();
    		
    		writerSD.write(standardDeviation + "");
    		writerSD.newLine();
    		
    		writerTime.write(avgTime + "ms");
    		writerTime.newLine();
    	}
    	writerAvg.close();
    	writerMax.close();
    	writerSD.close();
    	writerTime.close();
		
		
		
    	System.out.println("Working Directory = " + System.getProperty("user.dir"));

		
	}
	

	
	
	
	
	/**
	 * writes down the results to a specific SupXDem graph from the literature, but solves all the 40 variations of that combination.
	 * Can be performed for either general graphs or trees with a specific configuration.
	 * Saves the results in a .txt
	 * @param greedyIterations iterations used to identify the fixed sets
	 * @param consideredSolutions what is the maximum number of "best" solutions that should be considered
	 * @param solvesUsingFixedSet how many random iterations with the fixedSet should be performed
	 * @param FSSgreedySolvingTrait select the trait trough which the problem should be solved after generating the FSS, if more than 1 iteration random trait int = "4" is recommended
	 * @param threshold percent of supply coverage, which must be achieved to be considered a good enough solution (0.7 being 70%)
	 * @param testResultsForAvg the amount of tests that should be performed with this configuration
	 * @param connectivityHigh sets the graphs to general graphs (true) or trees (false)
	 * @param supplyV supply vertices of the graph to be solved
	 * @param demandV demand vertices of the graph to be solved
	 * @throws IOException
	 */
	private static void FSSResults2015LitGraphs(int greedyIterations, int consideredSolutions, int solvesUsingFixedSet, int FSSgreedySolvingTrait, double threshold, int testResultsForAvg, boolean connectivityHigh, int supplyV, int demandV) throws IOException {
		
		int supAmount = supplyV;
		int demAmount = demandV;
	
    	Boolean highConnectivity = connectivityHigh;
    	String connect = "";
    	if(highConnectivity == true) {
    		connect = "General";
    	}
    	if(highConnectivity == false) {
    		connect = "Tree";
    	}
    	int testResults = testResultsForAvg;//Assuming we want 10 solutions to get our median: testResults = 10
    	String txtName = "Literature_" + supAmount + "x" + demAmount + "_Resul_" + testResultsForAvg + "Iter_" + greedyIterations + "mbest_" + consideredSolutions + "Trait_" + FSSgreedySolvingTrait + "FSSolves_" + solvesUsingFixedSet + "_" + connect;
    	
		BufferedWriter writerAvg = new BufferedWriter(new FileWriter("resultsFSSAvgRelativeError"+ txtName +".txt"));
		BufferedWriter writerMax = new BufferedWriter(new FileWriter("resultsFSSMaxRelativeError"+ txtName +".txt"));
		BufferedWriter writerSD = new BufferedWriter(new FileWriter("resultsFSSStandardDeviation"+ txtName +".txt"));
		BufferedWriter writerTime = new BufferedWriter(new FileWriter("resultsFSSTime"+ txtName +".txt"));
    	
		writerAvg.write("Please * 100 for getting the percentage!");
		writerMax.write("Please * 100 for getting the percentage!");
		writerSD.write("Numbers are already in %!");
		writerTime.write("Time is alredy in milliseconds!");
		
		writerAvg.newLine();
		writerMax.newLine();
		writerSD.newLine();
		writerTime.newLine();
		
		
    	MPGSDGraph graph1 = GraphBuilder.getLitGraphSupXDem(supAmount, demAmount, highConnectivity, 0);
    	MPGSDGraph graph2 = GraphBuilder.getLitGraphSupXDem(supAmount, demAmount, highConnectivity, 1);
    	MPGSDGraph graph3 = GraphBuilder.getLitGraphSupXDem(supAmount, demAmount, highConnectivity, 2);
    	MPGSDGraph graph4 = GraphBuilder.getLitGraphSupXDem(supAmount, demAmount, highConnectivity, 3);
    	MPGSDGraph graph5 = GraphBuilder.getLitGraphSupXDem(supAmount, demAmount, highConnectivity, 4);
    	MPGSDGraph graph6 = GraphBuilder.getLitGraphSupXDem(supAmount, demAmount, highConnectivity, 5);
    	MPGSDGraph graph7 = GraphBuilder.getLitGraphSupXDem(supAmount, demAmount, highConnectivity, 6);
    	MPGSDGraph graph8 = GraphBuilder.getLitGraphSupXDem(supAmount, demAmount, highConnectivity, 7);
    	MPGSDGraph graph9 = GraphBuilder.getLitGraphSupXDem(supAmount, demAmount, highConnectivity, 8);
    	MPGSDGraph graph10 = GraphBuilder.getLitGraphSupXDem(supAmount, demAmount, highConnectivity, 9);
    	MPGSDGraph graph11 = GraphBuilder.getLitGraphSupXDem(supAmount, demAmount, highConnectivity, 10);
    	MPGSDGraph graph12 = GraphBuilder.getLitGraphSupXDem(supAmount, demAmount, highConnectivity, 11);
    	MPGSDGraph graph13 = GraphBuilder.getLitGraphSupXDem(supAmount, demAmount, highConnectivity, 12);
    	MPGSDGraph graph14 = GraphBuilder.getLitGraphSupXDem(supAmount, demAmount, highConnectivity, 13);
    	MPGSDGraph graph15 = GraphBuilder.getLitGraphSupXDem(supAmount, demAmount, highConnectivity, 14);
    	MPGSDGraph graph16 = GraphBuilder.getLitGraphSupXDem(supAmount, demAmount, highConnectivity, 15);
    	MPGSDGraph graph17 = GraphBuilder.getLitGraphSupXDem(supAmount, demAmount, highConnectivity, 16);
    	MPGSDGraph graph18 = GraphBuilder.getLitGraphSupXDem(supAmount, demAmount, highConnectivity, 17);
    	MPGSDGraph graph19 = GraphBuilder.getLitGraphSupXDem(supAmount, demAmount, highConnectivity, 18);
    	MPGSDGraph graph20 = GraphBuilder.getLitGraphSupXDem(supAmount, demAmount, highConnectivity, 19);
    	MPGSDGraph graph21 = GraphBuilder.getLitGraphSupXDem(supAmount, demAmount, highConnectivity, 20);
    	MPGSDGraph graph22 = GraphBuilder.getLitGraphSupXDem(supAmount, demAmount, highConnectivity, 21);
    	MPGSDGraph graph23 = GraphBuilder.getLitGraphSupXDem(supAmount, demAmount, highConnectivity, 22);
    	MPGSDGraph graph24 = GraphBuilder.getLitGraphSupXDem(supAmount, demAmount, highConnectivity, 23);
    	MPGSDGraph graph25 = GraphBuilder.getLitGraphSupXDem(supAmount, demAmount, highConnectivity, 24);
    	MPGSDGraph graph26 = GraphBuilder.getLitGraphSupXDem(supAmount, demAmount, highConnectivity, 25);
    	MPGSDGraph graph27 = GraphBuilder.getLitGraphSupXDem(supAmount, demAmount, highConnectivity, 26);
    	MPGSDGraph graph28 = GraphBuilder.getLitGraphSupXDem(supAmount, demAmount, highConnectivity, 27);
    	MPGSDGraph graph29 = GraphBuilder.getLitGraphSupXDem(supAmount, demAmount, highConnectivity, 28);
    	MPGSDGraph graph30 = GraphBuilder.getLitGraphSupXDem(supAmount, demAmount, highConnectivity, 29);
    	MPGSDGraph graph31 = GraphBuilder.getLitGraphSupXDem(supAmount, demAmount, highConnectivity, 30);
    	MPGSDGraph graph32 = GraphBuilder.getLitGraphSupXDem(supAmount, demAmount, highConnectivity, 31);
    	MPGSDGraph graph33 = GraphBuilder.getLitGraphSupXDem(supAmount, demAmount, highConnectivity, 32);
    	MPGSDGraph graph34 = GraphBuilder.getLitGraphSupXDem(supAmount, demAmount, highConnectivity, 33);
    	MPGSDGraph graph35 = GraphBuilder.getLitGraphSupXDem(supAmount, demAmount, highConnectivity, 34);
    	MPGSDGraph graph36 = GraphBuilder.getLitGraphSupXDem(supAmount, demAmount, highConnectivity, 35);
    	MPGSDGraph graph37 = GraphBuilder.getLitGraphSupXDem(supAmount, demAmount, highConnectivity, 36);
     	MPGSDGraph graph38 = GraphBuilder.getLitGraphSupXDem(supAmount, demAmount, highConnectivity, 37);
     	MPGSDGraph graph39 = GraphBuilder.getLitGraphSupXDem(supAmount, demAmount, highConnectivity, 38);
     	MPGSDGraph graph40 = GraphBuilder.getLitGraphSupXDem(supAmount, demAmount, highConnectivity, 39);

    	
    	MPGSDGraph[] graphArray = {graph1, graph2, graph3, graph4, graph5, graph6, graph7, graph8, graph9, graph10, 
    			graph11, graph12, graph13, graph14, graph15, graph16, graph17, graph18, graph19, graph20, graph21, 
    			graph22, graph23, graph24, graph25, graph26, graph27, graph28, graph29, graph30, graph31, graph32, 
    			graph33, graph34, graph35, graph36, graph37, graph38, graph39, graph40};
    	
    	
		
    	for(int k = 0; k < graphArray.length; k++) {
    		System.out.println("Starting with graph " + (k + 1) );
    		
    		long solutionArray[][] = new long[2][testResults]; 
    		double avgTime;
    		double avgDem;
    		double totalDem = 0;
    		double totalTime = 0;
    		MPGSDGraph currentGraph = graphArray[k];
    		for (int i = 0; i < testResults; i++) {
    			
    			long startTime = System.currentTimeMillis();
    			
    			List<SubGraph> fixedsetsfound = FixedSetSearch.getFixedSets(currentGraph, greedyIterations, consideredSolutions, threshold);
    			SolvedGraph FSSSolution = FixedSetSearch.getBestFSSolution(solvesUsingFixedSet, currentGraph, FSSgreedySolvingTrait, fixedsetsfound);
    			
    			
    			long estimatedTime = System.currentTimeMillis() - startTime;
    			solutionArray[0][i] = (long) FSSSolution.getTotalCoveredDemand();
    			solutionArray[1][i] = estimatedTime;

    			totalTime = totalTime + solutionArray[1][i];
    			totalDem = totalDem + solutionArray[0][i];
    			
    		}
    		avgDem = totalDem/testResults;
    		avgTime = totalTime/testResults;
    		
    		double optimalSolution = (double) currentGraph.getTotalMPGSDSupply();
    		double relativeError = - 1;
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
    		
    		
    		double relativeErrorWithoutPercent = relativeError / 100;
    		writerAvg.write(relativeErrorWithoutPercent + "");
    		writerAvg.newLine();
    		
    		double maxErrorWithoutPercent = maxRelativeError / 100;
    		writerMax.write(maxErrorWithoutPercent + "");
    		writerMax.newLine();
    		
    		writerSD.write(standardDeviation + "");
    		writerSD.newLine();
    		
    		writerTime.write(avgTime + "ms");
    		writerTime.newLine();
    	}
    	writerAvg.close();
    	writerMax.close();
    	writerSD.close();
    	writerTime.close();
		
		
		
    	System.out.println("Working Directory = " + System.getProperty("user.dir"));

		
	}



	/**
	 * solves a given graph once using the FSS with the given configuration, but doesn't print results to the console, but returns a Solved Graph
	 * not used, but was used for testing to better control the results of the solution
	 * @param g
	 * @param greedyIterations
	 * @param consideredSolutions
	 * @param iterationsWithFS
	 * @param FSSgreedySolvingTrait
	 * @return
	 * @throws IOException
	 */
	private static SolvedGraph getFSSSolution (MPGSDGraph g, int greedyIterations, int consideredSolutions, int iterationsWithFS, int FSSgreedySolvingTrait, double threshold) throws IOException {
		List<SubGraph> fixedsetsfound = FixedSetSearch.getFixedSets(g, greedyIterations, consideredSolutions, 0.7);
		SolvedGraph FSSSolution = FixedSetSearch.getBestFSSolution(iterationsWithFS, g, FSSgreedySolvingTrait, fixedsetsfound);
	
		return FSSSolution;
	
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

