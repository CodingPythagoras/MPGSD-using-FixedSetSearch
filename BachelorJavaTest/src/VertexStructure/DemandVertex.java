package VertexStructure;

public class DemandVertex extends Vertex{
	private int demand;
	private boolean demandIsCovered;
	private Vertex predecessor;
	
	
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
	
	public void setPredecessor(Vertex v) {
		predecessor = v;
	}
	
	public Vertex getPredecessor() {
		return predecessor;
	}
}
