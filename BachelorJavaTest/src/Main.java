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
		
		createAndSolveGraph("src\\JSONforGraph\\graph-config.json");
		
		System.out.println("Finished");
		
		
		System.out.println("Initialize solution with FSS");
		MPGSDGraph g1 = GraphBuilder.buildGraphFromJson("src\\JSONforGraph\\graph-config.json");
		System.out.println("1");
		List<SubGraph> fixedsetsfound = FixedSetSearch.getFixedSets(g1, 100, 10);
		System.out.println("Sup ID: " + fixedsetsfound.get(5).getSubgraphsSupplyVertex().getID());
		SolvedGraph FSSJSONGraphSolution = GreedyMPGSDSolver.GreedySolve2(g1, 1, fixedsetsfound);
		
		String coverageFSSSol = GreedyMPGSDSolver.getDemandCoverage(FSSJSONGraphSolution);
		System.out.println(coverageFSSSol);
	
	}
	
	private static void createAndSolveGraph(String JSONPath) throws IOException {
		
		MPGSDGraph JSONGraph = GraphBuilder.buildGraphFromJson(JSONPath);
		
		SolvedGraph JSONGraphSolution = GreedyMPGSDSolver.GreedySolve2(JSONGraph, 1);
		
		String coverageJSON = GreedyMPGSDSolver.getDemandCoverage(JSONGraphSolution);
		
		System.out.println(coverageJSON);
		
		System.out.println(JSONGraphSolution.getSolvedGraphMathematical());
	}

}
