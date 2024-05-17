package VertexStructure;
import java.util.LinkedList;




public class Vertex {
	private LinkedList<Vertex> AdjVertexList = new LinkedList<>();
	protected boolean isSupplyVertex;
	private int ID;
	protected Vertex predecessor;
	protected LinkedList<Vertex> successor = new LinkedList<>();
	
	/**
	 * creates a vertex using a specified ID
	 * @param id vertex with specific ID
	 */
	public Vertex(int id) {
		ID = id;
	}
	
	/**
	 * adds an adjacent vertex to the current vertex
	 * @param v the adjacent vertex
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
	
	/**
	 * removes an adjacent vertex 
	 * @param v the adjacent vertex, that should be removed out of the list
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
	
	/**
	 * checks if the object is a supplyVertex
	 * @return true if vertex is SupplyVertex
	 */
	public boolean getIsSupplyVertex() {
		return isSupplyVertex;
	}
	
	/**
	 * sets a vertex as a predecessor
	 * @param v the vertex
	 */
	public void setPredecessor(Vertex v) {
		predecessor = v;
	}
	
	public Vertex getPredecessor() {
		return predecessor;
	}
	
	/**
	 * sets vertex as a successor(target) of this vertex(this is start)
	 * @param v the successor of this vertex
	 */
	public void setSuccessor(Vertex v) {
		successor.add(v);
	}
	
	/**
	 * vertices, which are succesors to this vertex
	 * @return LikedList of vertices
	 */
	public LinkedList<Vertex> getSuccessor() {
		return successor;
	}
	
	/**
	 * 
	 * @param remainingSupply takes the remaining supply of a graph
	 * @return all the possible vertices which could potentially be coverd by the demand
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
	

	/**
	 * 
	 * @param remainingSupply takes the remaining supply of a graph
	 * @return an int, as the number of vertices that could potentially be covered by the demand
	 */
	public int getNumberofAdjNotCoveredFittingVertexes(int remainingSupply) {
		LinkedList<Vertex> possibleAdjDemandVertexes = getListOfAdjNotCoveredFittingVertexes(remainingSupply);
		return possibleAdjDemandVertexes.size();
	}
	
	
}
