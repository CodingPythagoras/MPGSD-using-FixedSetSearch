package VertexStructure;

import java.util.ArrayList;

/**
 * extends Vertex
 * defines what a demand vertex is and its specific attributes and operations
 * @author Manuel
 *
 */
public class DemandVertex extends Vertex{
	private int demand;
	private boolean demandIsCovered;
	
	
	
	/**
	 * creates a DemandVertex with a specific demand
	 * @param id int which functions as identification of the vertex
	 * @param setDemand int that sets the required demand of the vertex
	 */
	public DemandVertex(int id, int setDemand) {
		super(id);
		demand = setDemand;
		
		isSupplyVertex = false;
		demandIsCovered = false;
	}
	
	
	/**
	 * 
	 * @return the demand of the vertex
	 */
	public int getDemand() {
		return demand;
	}
	
	/**
	 * sets the vertex/demand as covered
	 */
	public void setDemandAsCovered() {
		demandIsCovered = true;
	}
	
	/**
	 * checks if the vertex is already being covered
	 * @return boolean, if the vertex is already being covered by a supplyVertex or not
	 */
	public boolean getDemandIsCovered() {
		return demandIsCovered;
	}
	
	/**
	 * resets the demandVertex, so it can be used to solve the same graph again
	 * sets the demandCovered as false
	 * removes all successors
	 * sets the predecessor of this vertex to null
	 */
	public void resetDemandVertex() {
		demandIsCovered = false;
		successor = new ArrayList<>();
		predecessor = null;
	}
	

}
