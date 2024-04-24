package VertexStructure;

import java.util.LinkedList;

public class DemandVertex extends Vertex{
	private int demand;
	private boolean demandIsCovered;
	
	
	
	/*
	 * Creates a DemandVertex with a specific demand
	 */
	public DemandVertex(int id, int setDemand) {
		super(id);
		demand = setDemand;
		
		isSupplyVertex = false;
		demandIsCovered = false;
	}
	
	/*
	 * Creates a DemandVertex with a random demand
	 */
	public DemandVertex(int id) {
		super(id);
		demand = (int) Math.floor(Math.random() * 10);
		
		isSupplyVertex = false;
		demandIsCovered = false;
		
	}
	
	public int getDemand() {
		return demand;
	}
	
	public void setDemandAsCovered() {
		demandIsCovered = true;
	}
	
	public boolean getDemandIsCovered() {
		return demandIsCovered;
	}
	
	public void resetDemandVertex() {
		demandIsCovered = false;
		successor = new LinkedList<>();
		//TODO null
		predecessor = null;
	}
	

}
