import java.util.LinkedList;
import java.util.Random;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hello World");
		
		MPGSDGraph testMPGSDGraph1 = new MPGSDGraph(0, 0);
		MPGSDGraph testMPGSDGraph2 = new MPGSDGraph(0, 0);
		
		
		LinkedList<LinkedList<Vertex>> solutionGraph = GreedyMPGSDSolver.GreedySolve(testMPGSDGraph1);
		
//		for(int i = 0; i <= solutionGraph.size() - 1; i++) {
//			LinkedList<Vertex> k = solutionGraph.get(i);
//			System.out.println("Subgraph: " + i + " contains:");
//			for(Vertex v: k) {
//				System.out.println(v.getID());
//			}
//		}
		
		SolvedGraph solutionGraph2 = GreedyMPGSDSolver.GreedySolve2(testMPGSDGraph2);
		String coverage = GreedyMPGSDSolver.getDemandCoverage(solutionGraph2);
		System.out.println(coverage);
		System.out.println(((SupplyVertex)solutionGraph.get(0).get(0)).getRemainingSupply());
		System.out.println(((SupplyVertex)solutionGraph.get(1).get(0)).getRemainingSupply());
		System.out.println(((SupplyVertex)solutionGraph.get(2).get(0)).getRemainingSupply());
		System.out.println(((SupplyVertex)solutionGraph.get(3).get(0)).getRemainingSupply());
		System.out.println(solutionGraph.get(0).get(0).getAdjVertexList());
		System.out.println(solutionGraph2.getSubgraph(0).get(0).getAdjVertexList());
		
	}

}
