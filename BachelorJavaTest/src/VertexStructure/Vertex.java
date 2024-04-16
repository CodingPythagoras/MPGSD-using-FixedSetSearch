package VertexStructure;
import java.util.LinkedList;




public class Vertex {
	private LinkedList<Vertex> AdjVertexList = new LinkedList<>();
	protected boolean isSupplyVertex;
	private int ID;
	protected Vertex predecessor;
	protected LinkedList<Vertex> successor;
	
	
	public Vertex(int id) {
		ID = id;
	}
	
	/*
	 * Adds a Vertex as adjacent
	 */
	public void addAdjVertex(Vertex v) {
		//Adds Vertex v to Adjacent List
		if (!this.AdjVertexList.contains(v)) {
			this.AdjVertexList.add(v);
		}
		//Adds this vertex to Adjacent List of v
		if (!v.getAdjVertexList().contains(this)) {
			v.getAdjVertexList().add(this);
		}
		
	}
	
	/*
	 * Removes an adjacent Vertex 
	 */
	public void removeAdjVertex(Vertex v) {
		if(this.AdjVertexList.contains(v)) {
			this.AdjVertexList.remove(v);
		}
		
		if (v.getAdjVertexList().contains(this)) {
			v.getAdjVertexList().remove(v);
		}
		
	}
	
	public LinkedList<Vertex> getAdjVertexList() {
		return AdjVertexList;
	}
	
	public int getID() {
		return ID;
	}
	
	public boolean getIsSupplyVertex() {
		return isSupplyVertex;
	}
	
	public void setPredecessor(Vertex v) {
		predecessor = v;
	}
	
	public Vertex getPredecessor() {
		return predecessor;
	}
	
	public void setSuccessor(Vertex v) {
		successor.add(v);
	}
	
	public LinkedList<Vertex> getSuccessor() {
		return successor;
	}
	
	
	public LinkedList<Vertex> getListOfAdjNotCoveredFittingVertexes(int remainingSupply) {
		LinkedList<Vertex> possibleAdjDemandVertexes = new LinkedList<Vertex>();
		for (int i = 0; i <= AdjVertexList.size() - 1; i++) {
			Vertex k = AdjVertexList.get(i);
			
			if(!k.isSupplyVertex && ((DemandVertex) k).getDemandIsCovered() == false && remainingSupply >= ((DemandVertex) k).getDemand()) {
				possibleAdjDemandVertexes.add(k);
			}
		}
		return possibleAdjDemandVertexes;
	}
	
	public int getNumberofAdjNotCoveredFittingVertexes(int remainingSupply) {
		LinkedList<Vertex> possibleAdjDemandVertexes = getListOfAdjNotCoveredFittingVertexes(remainingSupply);
		return possibleAdjDemandVertexes.size();
	}
	
	
}
