package VertexStructure;
import java.util.LinkedList;




public class Vertex {
	private LinkedList<Vertex> AdjVertexList = new LinkedList<>();
	protected boolean isSupplyVertex;
	private int ID;
	protected Vertex predecessor;
	protected LinkedList<Vertex> successor = new LinkedList<>();
	
	/*
	 * creates a Vertex using a specified ID
	 */
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
	
	/*
	 * checks if the Object is a supplyVertex
	 * returns true if Vertex is SupplyVertex
	 */
	public boolean getIsSupplyVertex() {
		return isSupplyVertex;
	}
	
	/*
	 * sets a Vertex as a predecessor
	 */
	public void setPredecessor(Vertex v) {
		predecessor = v;
	}
	
	public Vertex getPredecessor() {
		return predecessor;
	}
	
	/*
	 * sets a Vertex as a successor of this Vertex
	 */
	public void setSuccessor(Vertex v) {
		successor.add(v);
	}
	
	public LinkedList<Vertex> getSuccessor() {
		return successor;
	}
	
	/*
	 * takes the remaining supply of a graph
	 * returns all the possible vertices which could be coverd by the demand
	 */
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
	
	/*
	 * takes the remaining supply of a graph
	 * returns an int, as the number of vertices that could be covered by the demand
	 */
	public int getNumberofAdjNotCoveredFittingVertexes(int remainingSupply) {
		LinkedList<Vertex> possibleAdjDemandVertexes = getListOfAdjNotCoveredFittingVertexes(remainingSupply);
		return possibleAdjDemandVertexes.size();
	}
	
	
}
