import java.util.LinkedList;
import java.util.Random;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hello World");
		
		MPGSDGraph testMPGSDGraph1 = new MPGSDGraph(0, 0);
		MPGSDGraph testMPGSDGraph2 = new MPGSDGraph(0, 0);
		
		
		SolvedGraph solutionGraph1 = GreedyMPGSDSolver.GreedySolve1(testMPGSDGraph1);
		SolvedGraph solutionGraph2 = GreedyMPGSDSolver.GreedySolve2(testMPGSDGraph2);
		
//		for(int i = 0; i <= solutionGraph.size() - 1; i++) {
//			LinkedList<Vertex> k = solutionGraph.get(i);
//			System.out.println("Subgraph: " + i + " contains:");
//			for(Vertex v: k) {
//				System.out.println(v.getID());
//			}
//		}
		
		
		String coverage1 = GreedyMPGSDSolver.getDemandCoverage(solutionGraph1);
		String coverage2 = GreedyMPGSDSolver.getDemandCoverage(solutionGraph2);
		
		System.out.println(coverage1);
		System.out.println(coverage2);
		
		System.out.println(solutionGraph1.getSubgraph(0).getVertexList());
		System.out.println(solutionGraph1.getSubgraph(1).getVertexList());
		System.out.println(solutionGraph1.getSubgraph(2).getVertexList());
		System.out.println(solutionGraph1.getSubgraph(3).getVertexList());
		
		System.out.println("Finished");
		
		
	
	}

}
