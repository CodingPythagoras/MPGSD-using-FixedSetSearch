import java.io.IOException;

import GraphStructures.MPGSDGraph;
import GraphStructures.SolvedGraph;
import JSONtoGraph.GraphBuilder;

public class Main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("Hello World");
		
		createAndSolveGraph("src\\JSONforGraph\\graph-config.json");
		
		System.out.println("Finished");
		
		
	
	}
	
	private static void createAndSolveGraph(String JSONPath) throws IOException {
		
		MPGSDGraph JSONGraph = GraphBuilder.buildGraphFromJson(JSONPath);
		
		SolvedGraph JSONGraphSolution = GreedyMPGSDSolver.GreedySolve2(JSONGraph);
		
		String coverageJSON = GreedyMPGSDSolver.getDemandCoverage(JSONGraphSolution);
		
		System.out.println(coverageJSON);
		
		System.out.println(JSONGraphSolution.getSolvedGraphMathematical());
	}

}
