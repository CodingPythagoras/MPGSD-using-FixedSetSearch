package GraphStructures;
import java.util.ArrayList;
import java.util.List;

import VertexStructure.SupplyVertex;
import VertexStructure.Vertex;


/**
 * defines a Solved Graph graph with its operations and attributes
 * @author Manuel
 *
 */
public class SolvedGraph {
	private ArrayList<SubGraph> graphOfSubgraphs = new ArrayList<>();
	private int numberOfSupplyVertexes;
	private int numberOfDemandVertexes;
	private int totalGivenSupply;
	private int totalUsedSupply;
	private int totalOriginalDemand;
	private int totalCoveredDemand;
	

	
	/**
	 * takes a MPGSD graph g, and creates a unique subgraph for each supply vertex in g
	 * @param g MPGSD graph
	 */
	public SolvedGraph(MPGSDGraph g) {
		numberOfSupplyVertexes = g.getListOfSupplyVertexes().size();
		
		//extract the supply vertices and create one new subgraph for each one
		for (int i = 0; i <= numberOfSupplyVertexes - 1; i++) {
			SubGraph sub = new SubGraph(g.getListOfSupplyVertexes().get(i));
			graphOfSubgraphs.add(sub);
		}
	}
	
	/**
	 * only with fixedSets!
	 * initializes a new solved graph, using the found fixed sets
	 * and corrects the used Supply and Covered Demand!
	 * @param g MPGSD graph
	 * @param fixedsetsfound List<SubGraph> the fixed sets
	 */
	public SolvedGraph(MPGSDGraph g, List<SubGraph> fixedsetsfound) {
		numberOfSupplyVertexes = g.getListOfSupplyVertexes().size();
		
		for (int i = 0; i <= fixedsetsfound.size() - 1; i++) {
			SubGraph clonedFixedSet = new SubGraph(fixedsetsfound.get(i));
			graphOfSubgraphs.add(clonedFixedSet);
			
			//update supply and demand of the new subgraph with pre selected elements
			this.addUsedSupply(clonedFixedSet.getSubsCovDemand());
			this.addCoveredDemand(clonedFixedSet.getSubsCovDemand());
		}
	}
	
	
	/**
	 * returns the subgraph with the highest remaining supply
	 * 
	 * @return SubGraph with highest remaining Supply
	 */
	public SubGraph getSubgraphWithHigestSupply() {
		int maxSupply = 0;
		SubGraph currentMaxSupplySubgraph = null;
		
		
		//goes over every subgraphs supply vertex
		for(SubGraph subG: graphOfSubgraphs) {
			SupplyVertex supV = subG.getSubgraphsSupplyVertex();
			int currentSup = supV.getRemainingSupply();
			
			if(maxSupply < currentSup && !subG.isComplete()) {
				maxSupply = currentSup;
				currentMaxSupplySubgraph = subG;
			}
		}
		
		
		return currentMaxSupplySubgraph;
	}
	
	/**
	 * returns the subgraph with the lowest remaining supply
	 * 
	 * @return SubGraph with lowest remaining Supply
	 */
	public SubGraph getSubgraphWithLowestSupply() {
		int minSupply = Integer.MAX_VALUE;
		SubGraph currentMinSupplySubgraph = null;
		
		for(SubGraph subG: graphOfSubgraphs) {
			SupplyVertex supV = subG.getSubgraphsSupplyVertex();
			int currentSup = supV.getRemainingSupply();
			
			if(minSupply > currentSup && !subG.isComplete()) {
				minSupply = currentSup;
				currentMinSupplySubgraph = subG;
			}
		}
		
		
		return currentMinSupplySubgraph;
	}
	

	
	/**
	 * returns an String/ Array if changed representation of the solved Graph
	 * @return String representation of the solved Graph
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
	
	
	
	/**
	 * searches for a vertex in the solved graph with the requested ID
	 * @param source ID to search for
	 * @return Vertex corresponding to the ID
	 */
	public Vertex findVertexById(int source) {
		
		for(SubGraph subs: graphOfSubgraphs) {
			for(Vertex vert: subs.getVertexList()) {
				if (vert.getID() == source) {
					return vert;
				}
			}
		}
			
		
		return null;
	}
	
	/**
	 * used to get the subgraph to a specific supply vertex
	 * @param supplyVertex the vertex of the subgraphs which should be returned
	 * @return subGraph with corresponding supply vertex
	 */
	public SubGraph getSubgraphWithSupplyVertex(SupplyVertex supplyVertex) {
		for(int i = 0; i < graphOfSubgraphs.size() - 1; i++) {
			if(graphOfSubgraphs.get(i).getSubgraphsSupplyVertex().getID() == supplyVertex.getID()) {
				return graphOfSubgraphs.get(i);
			}
		}
		return null;
	}
	
	
	//getters and setters
	
	public SubGraph getSubgraph(int pos) {
		return graphOfSubgraphs.get(pos);
	}
	
	public void addCoveredDemand(int dem) {
		totalCoveredDemand += dem;
	}
	
	public void addSupplyAndDemand(int dem) {
		totalCoveredDemand += dem;
		totalUsedSupply += dem;
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

	public ArrayList<SubGraph> getGraphOfSubgraphs() {
		return graphOfSubgraphs;
	}
	

}
