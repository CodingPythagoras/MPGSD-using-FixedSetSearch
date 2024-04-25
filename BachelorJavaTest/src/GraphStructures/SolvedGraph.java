package GraphStructures;
import java.util.LinkedList;
import java.util.List;

import VertexStructure.DemandVertex;
import VertexStructure.Edge;
import VertexStructure.SupplyVertex;
import VertexStructure.Vertex;

public class SolvedGraph {
	private LinkedList<SubGraph> graphOfSubgraphs = new LinkedList<>();
	private int numberOfSubgraphs = 0;
	private int numberOfSupplyVertexes;
	private int numberOfDemandVertexes;
	private int totalGivenSupply;
	private int totalUsedSupply;
	private int totalOriginalDemand;
	private int totalCoveredDemand;
	
	/*
	 * takes a MPGSD graph g, and creates a unique subgraph for each supply vertex in g
	 */
	public SolvedGraph(MPGSDGraph g) {
		numberOfSupplyVertexes = g.getListOfSupplyVertexes().size();
		for (int i = 0; i <= numberOfSupplyVertexes - 1; i++) {
			SubGraph sub = new SubGraph(g.getListOfSupplyVertexes().get(i));
			graphOfSubgraphs.add(sub);
			numberOfSubgraphs += 1;
		}
	}
	
	/**
	 * only with fixedSets!
	 * corrects the used Supply and Covered Demand!
	 * @param g
	 * @param fixedsetsfound
	 */
	public SolvedGraph(MPGSDGraph g, List<SubGraph> fixedsetsfound) {
		numberOfSupplyVertexes = g.getListOfSupplyVertexes().size();
		
		for (int i = 0; i <= fixedsetsfound.size() - 1; i++) {
			
			graphOfSubgraphs.add(fixedsetsfound.get(i));
			this.addUsedSupply(fixedsetsfound.get(i).getSubsCovDemand());
			this.addCoveredDemand(fixedsetsfound.get(i).getSubsCovDemand());
			numberOfSubgraphs += 1;
		}
	}
	
	/*
	 * returns the subgraph with the highest remaining supply
	 */
	public SubGraph getSubgraphWithHigestSupply() {
		int maxSupply = 0;
		SubGraph currentMaxSupplySubgraph = null;
		
		for(int i = 0; i <= numberOfSubgraphs - 1; i++) {
			int currentSup = graphOfSubgraphs.get(i).getSubgraphsSupplyVertex().getRemainingSupply();
			//Only returns Subgraphs when its not complete
			if(maxSupply < currentSup && !graphOfSubgraphs.get(i).isComplete) {
				maxSupply = currentSup;
				currentMaxSupplySubgraph = graphOfSubgraphs.get(i);
			}
		}
		return currentMaxSupplySubgraph;
	}
	
	/*
	 * returns an String/ Array if changed representation of the solves Subgraph
	 * possible use of StringBuilder to increase performance
	 */
	public String getSolvedGraphMathematical() {
		String solutionString = "";
		for(int i = 0; i <= graphOfSubgraphs.size() - 1; i++) {
			SubGraph sub = graphOfSubgraphs.get(i);
			int[][] subRepresentation = sub.getMathematicalRepresentationOfSubgraph();
			//1st one is always the Supply one
			String x = "[Subgraph " + (i+1) + "]\n Vertex " + 1 + ":" + "  ID: " + subRepresentation[0][0] + " Supply: " + subRepresentation[0][1] +
					" Predecessor: " + subRepresentation[0][2] + "\n ";
			
			solutionString = solutionString.concat(x);
			//start at j == 1 to start for loop with demand Vertexes
			for(int j = 1; j <= subRepresentation.length - 1; j++) {

				String z = "Vertex " + (j + 1) + ":" + "  ID: " + subRepresentation[j][0] + " Demand: " + subRepresentation[j][1] +
						" Predecessor: " + subRepresentation[j][2] + "\n ";
				solutionString = solutionString.concat(z);
			}
			String y = "\n" + "\n";
			solutionString = solutionString.concat(y);
		}
		
		return solutionString;
	}
	
	
	public Vertex findVertexById(int source) {
		// TODO improve by sorting ArrayList by ID and using direct access
		for(SubGraph subs: graphOfSubgraphs) {
			for(Vertex vert: subs.getVertexList()) {
				if (vert.getID() == source) {
					return vert;
				}
			}
		}
			
		
		return null;
	}
	
	public SubGraph getSubgraph(int pos) {
		return graphOfSubgraphs.get(pos);
	}
	
	public void addCoveredDemand(int dem) {
		totalCoveredDemand += dem;
	}
	
	public void addUsedSupply(int sup) {
		totalUsedSupply += sup;
	}
	
	public int getTotalUsedSupply() {
		return totalUsedSupply;
	}
	
	public int getTotalCoveredDemand() {
		return totalCoveredDemand;
	}
	
	public void addNumberOfDemandVertexes(int num) {
		numberOfDemandVertexes += num;
	}
	
	public int getNumberOfSupplyVertexes() {
		return numberOfSupplyVertexes;
	}
	
	public int getNumberOfDemandVertexes() {
		return numberOfDemandVertexes;
	}
	
	public void setTotalGivenSupply(int num) {
		totalGivenSupply = num;
	}
	
	public int getTotalGivenSupply() {
		return totalGivenSupply;
	}
	
	public void setTotalOriginalDemand(int num) {
		totalOriginalDemand = num;
	}
	
	public int getTotalOriginalDemand() {
		return totalOriginalDemand;
	}

	public LinkedList<SubGraph> getGraphOfSubgraphs() {
		// TODO Auto-generated method stub
		return graphOfSubgraphs;
	}
}
