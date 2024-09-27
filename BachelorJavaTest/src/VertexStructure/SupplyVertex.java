package VertexStructure;

import java.util.ArrayList;

/**
 * extends Vertex
 * defines what a supply vertex is and its specific attributes and operations
 * @author Manuel
 *
 */
public class SupplyVertex extends Vertex{
	private int supply;
	private int usedSupply;
	private int remainingSupply;
	
	
	/**
	 * creates a SupplyVertex with a specific supply
	 * @param id int which functions as identification of the vertex
	 * @param setSupply specific supply of this vertex
	 */
	public SupplyVertex(int id, int setSupply) {
		super(id);
		supply = setSupply;
		isSupplyVertex = true;
		usedSupply = 0;
		predecessor = this;
		remainingSupply = supply;
	}
	
	
	/**
	 * 
	 * @return the remaining not used supply of the supplyVertex
	 */
	public int getRemainingSupply() {
		return remainingSupply;
	}
	
	/**
	 * if demand is full filled by this supplyVertex, it gets subtracted from the remaining supply
	 * @param demandCovered the demand which gets covered by this supplyVertex
	 */
	public void useSupply(int demandCovered) {
		usedSupply += demandCovered;
		remainingSupply = supply - usedSupply;
	}
	
	/**
	 * 
	 * @return the initial supply of this vertex
	 */
	public int getInitialSupply() {
		return supply;
	}
	
	/**
	 * resets this vertex, to be used to solve the graph again
	 * sets the supply back to its initial
	 * removes all the current successors
	 * and sets this vertex as its own predecessor
	 */
	public void resetSupplyVertex() {
		usedSupply =  0;
		remainingSupply = supply;
		successor = new ArrayList<>();
		predecessor  = this;
	}
	/**
	 * only resets the supply of the vertex, but not its successor
	 * important, when later extracting the fixed set
	 */
	public void onlyResetSupply() {
		usedSupply =  0;
		remainingSupply = supply;
	}
	
}
