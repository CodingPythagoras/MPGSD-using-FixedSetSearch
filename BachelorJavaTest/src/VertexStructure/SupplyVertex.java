package VertexStructure;

public class SupplyVertex extends Vertex{
	private int supply;
	private int usedSupply;
	private boolean isInSubgraph;
	
	
	/*
	 * Creates a SupplyVertex with a specific supply
	 */
	public SupplyVertex(int id, int setSupply) {
		super(id);
		supply = setSupply;
		isSupplyVertex = true;
		usedSupply = 0;
		isInSubgraph = false;
		predecessor = this;
	}
	
	/*
	 * Creates a SupplyVertex with a random supply
	 */
	public SupplyVertex(int id) {
		super(id);
		supply = (int) Math.floor(Math.random() * 10);
		isSupplyVertex = true;
		usedSupply = 0;
		isInSubgraph = false;
		predecessor = this;
	}
	
	public int getRemainingSupply() {
		return supply - usedSupply;
	}
	
	public void useSupply(int demandCovered) {
		usedSupply += demandCovered;
	}
	
	public int getInitialSupply() {
		return supply;
	}
	
	
	/*
	 * TODO not used yet/ maybe not needed
	 */
	public void setInSubGraphTrue() {
		isInSubgraph = true;
	}
	
	public boolean getIsInSubGraph() {
		return isInSubgraph;
	}
}
