import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import GraphStructures.MPGSDGraph;
import GraphStructures.SolvedGraph;
import GraphStructures.SubGraph;
import JSONtoGraph.GraphBuilder;

public class Main {

	public static void main(String[] args) throws IOException {
	
		System.out.println("Building MPGSD graphs");
		//MPGSDGraph test = GraphBuilder.buildGraphFromJson("src\\JSONforGraph\\snake_updated_version_19072024_100x300.json");
		MPGSDGraph graph = GraphBuilder.getGraphSupXDem(5, 100, false);
		
		System.out.println("Finished");
		
		solveGraphUsingFixedSetsSearch(graph, 50, 3, 20, 1);
		//solveGraphUsingTraitsTXT();
		//solveGraphUsingTraits(graph);
		
		
		int testResultsForAvg = 500; // How many times our problem should be solved to collect our data
		
		int iterationsToFindFixedSet = 50; // Number of GreedySolutions to determine our fixed set
		int mBestSolutionsToBeConsidered = 3; // Number of best solutions out of these greedy iterations to consider for our fixed set search
		int solvingTraitForGreedyWithFixedSet = 1; // Trait which is used to generate a final solution for the Problem, which given fixed set
		
		
		//writeDownFSSTestResultsToGraph(iterationsToFindFixedSet, mBestSolutionsToBeConsidered, solvingTraitForGreedyWithFixedSet, testResultsForAvg);
		//writeDownFSSTestResultsToGraph(100, 5, solvingTraitForGreedyWithFixedSet, testResultsForAvg);
		//writeDownFSSTestResultsToGraph(1000, 50, solvingTraitForGreedyWithFixedSet, testResultsForAvg);
		//writeDownFSSTestResultsToGraph(4000, 200, solvingTraitForGreedyWithFixedSet, testResultsForAvg);
		//giveTestResultsToGraph(graph, iterationsToFindFixedSet, mBestSolutionsToBeConsidered, solvingTraitForGreedyWithFixedSet, testResultsForAvg);
		

		
		
		
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
		System.out.println("Trait 1: " + coverageJSONTrait1 );
		
		double relativeError1 =(double) (1-(double)((double)JSONGraphSolution1.getTotalCoveredDemand() / (double)JSONPath.getTotalMPGSDSupply())) * 100;
		System.out.println("relative error trait 1: " + relativeError1);
		
		
		
		SolvedGraph JSONGraphSolution2 = GreedyMPGSDSolver.GreedySolve2(JSONPath, 2);
		String coverageJSONTrait2 = GreedyMPGSDSolver.getDemandCoverage(JSONGraphSolution2);
		System.out.println("Trait 2: " + coverageJSONTrait2);
		
		double relativeError2 =(double) (1-(double)((double)JSONGraphSolution2.getTotalCoveredDemand() / (double)JSONPath.getTotalMPGSDSupply())) * 100;
		System.out.println("relative error trait 2: " + relativeError2);
		
		
		
		SolvedGraph JSONGraphSolution3 = GreedyMPGSDSolver.GreedySolve2(JSONPath, 3);
		String coverageJSONTrait3 = GreedyMPGSDSolver.getDemandCoverage(JSONGraphSolution3);
		System.out.println("Trait 3: " + coverageJSONTrait3);
		
		double relativeError3 =(double) (1-(double)((double)JSONGraphSolution3.getTotalCoveredDemand() / (double)JSONPath.getTotalMPGSDSupply())) * 100;
		System.out.println("relative error trait 3: " + relativeError3);
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
		
		SolvedGraph FSSSolutionOfIterations = FixedSetSearch.getBestFSSolution(iterationsWithFS, g, solvingTrait, fixedsetsfound);
		long estimatedTime = System.currentTimeMillis() - startTime;
		
		//just visual
		SolvedGraph fixedSet = new SolvedGraph(g, fixedsetsfound);
		System.out.println("FixedSetSolution" + fixedSet.getSolvedGraphMathematical());
		String coverageFSSIt = GreedyMPGSDSolver.getDemandCoverage(FSSSolutionOfIterations);
		
		//System.out.println(FSSSolutionOfIterations.getSolvedGraphMathematical());
		System.out.println(coverageFSSIt);
		
		
		System.out.println("Problem solved in: " + estimatedTime + " milliseconds");
		
	}
	
	private static void giveTestResultsToGraph(MPGSDGraph g, int greedyIterations, int consideredSolutions, int FSSgreedySolvingTrait, int testResultsForAvg) throws IOException {
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
	
	private static SolvedGraph getFSSSolution (MPGSDGraph g, int greedyIterations, int consideredSolutions, int iterationsWithFS, int FSSgreedySolvingTrait) throws IOException {
		List<SubGraph> fixedsetsfound = FixedSetSearch.getFixedSets(g, greedyIterations, consideredSolutions, 0.7);
		SolvedGraph FSSSolution = FixedSetSearch.getBestFSSolution(iterationsWithFS, g, FSSgreedySolvingTrait, fixedsetsfound);
	
		return FSSSolution;
	
	}
	
	//TODO Theshhold can be removed also in text/ iterations with FS can be set to 1 if we only se trait 1-3
	
	
	
	private static void solveGraphUsingTraitsTXT() {
	    try (BufferedWriter writer = new BufferedWriter(new FileWriter("resultsNormalTraits.txt"))) {
	    	Boolean highConnectivity = true;
	    	
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
	    
	    
		        SolvedGraph JSONGraphSolution1 = GreedyMPGSDSolver.GreedySolve2(graphArray[i], 3);
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private static void writeDownFSSTestResultsToGraph(int greedyIterations, int consideredSolutions, int FSSgreedySolvingTrait, int testResultsForAvg) throws IOException {
		
    	Boolean highConnectivity = false;
    	int testResults = testResultsForAvg;//Assuming we want 10 solutions to get our median: testResults = 10
    	String txtName = "Resul_" + testResultsForAvg + "Iter_" + greedyIterations + "mbest_" + consideredSolutions + "Trait_" + FSSgreedySolvingTrait;
    	
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
    			
    			List<SubGraph> fixedsetsfound = FixedSetSearch.getFixedSets(currentGraph, greedyIterations, consideredSolutions, 0.7);
    			SolvedGraph FSSSolution = FixedSetSearch.getBestFSSolution(1, currentGraph, FSSgreedySolvingTrait, fixedsetsfound);
    			
    			
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

