import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("Hello World");
		
		compareJSONGraphAndManuell();
		
		System.out.println("Finished");
		
		
	
	}
	
	private static void compareJSONGraphAndManuell() throws IOException {
		MPGSDGraph manuellGraph = new MPGSDGraph(1);
		MPGSDGraph JSONGraph = GraphBuilder.buildGraphFromJson("src/graph-config-2.json");
		
		SolvedGraph manuellGraphSolution = GreedyMPGSDSolver.GreedySolve2(manuellGraph);
		SolvedGraph JSONGraphSolution = GreedyMPGSDSolver.GreedySolve2(JSONGraph);
		
		String coverageManuell = GreedyMPGSDSolver.getDemandCoverage(manuellGraphSolution);
		String coverageJSON = GreedyMPGSDSolver.getDemandCoverage(JSONGraphSolution);
		
		System.out.println(coverageManuell);
		System.out.println(coverageJSON);
		
		System.out.println(manuellGraphSolution.getSolvedGraphMathematical());
		System.out.println(JSONGraphSolution.getSolvedGraphMathematical());
	}

}
