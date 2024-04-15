import java.util.LinkedList;

public class SolvedGraph {
	private LinkedList<SubGraph> graphOfSubgraphs = new LinkedList<>();
	private int numberOfSubgraphs = 0;
	private int numberOfSupplyVertexes;
	private int numberOfDemandVertexes;
	private int totalGivenSupply;
	private int totalUsedSupply;
	private int totalOriginalDemand;
	private int totalCoveredDemand;
	
	
	public SolvedGraph(int numberSupplyVertexes) {
		numberOfSupplyVertexes = numberSupplyVertexes;
		for (int i = 0; i <= numberSupplyVertexes - 1; i++) {
			LinkedList<Vertex> subGraph = new LinkedList<Vertex>();
			//graphOfSubgraphs.add(subGraph);
		}
	}
	
	public SolvedGraph(MPGSDGraph g) {
		numberOfSupplyVertexes = g.getListOfSupplyVertexes().size();
		for (int i = 0; i <= numberOfSupplyVertexes - 1; i++) {
			SubGraph sub = new SubGraph(g.getListOfSupplyVertexes().get(i));
			graphOfSubgraphs.add(sub);
			numberOfSubgraphs += 1;
		}
	}
	
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
}
