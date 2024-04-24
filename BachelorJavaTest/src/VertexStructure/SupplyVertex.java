package VertexStructure;

import java.util.LinkedList;

public class SupplyVertex extends Vertex{
	private int supply;
	private int usedSupply;
	private boolean isInSubgraph;
	private int remainingSupply;
	
	
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
		remainingSupply = supply;
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
		return remainingSupply;
	}
	
	public void useSupply(int demandCovered) {
		usedSupply += demandCovered;
		remainingSupply = supply - usedSupply;
	}
	
	public int getInitialSupply() {
		return supply;
	}
	
	public void resetSupplyVertex() {
		usedSupply =  0;
		isInSubgraph = false;
		remainingSupply = supply;
		successor = new LinkedList<>();
		predecessor  = this;
	}
	
}
