package VertexStructure;
import java.util.ArrayList;



/**
 * the parent class to define the general concept of a vertex
 * @author Manuel
 *
 */
public class Vertex {
	private ArrayList<Vertex> AdjVertexList = new ArrayList<>();
	protected boolean isSupplyVertex;
	private int ID;
	protected Vertex predecessor;
	protected ArrayList<Vertex> successor = new ArrayList<>();
	
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
	
	/**
	 * 
	 * @return List of adjacent vertices
	 */
	public ArrayList<Vertex> getAdjVertexList() {
		return AdjVertexList;
	}
	
	/**
	 * 
	 * @return ID of the vertex
	 */
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
	
	/**
	 * 
	 * @return the vertex, which was crucial for adding the vertex
	 */
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
	 * vertices, which are successors to this vertex
	 * @return LikedList of vertices
	 */
	public ArrayList<Vertex> getSuccessor() {
		return successor;
	}
	
	/**
	 * 
	 * retrieves a list of adjacent demand vertices that are not covered and can potentially
	 * be covered by the remaining supply of the graph.
	 * 
	 * @param remainingSupply takes the remaining supply of a graph
	 * @return all the possible vertices which could potentially be coverd by the demand
	 */
	public ArrayList<Vertex> getListOfAdjNotCoveredFittingVertices(int remainingSupply) {
		
		//goes over every adjacent vertex and checks, if it would be a possible to cover its demand with the given supply
		ArrayList<Vertex> possibleAdjDemandVertices = new ArrayList<Vertex>();
		for (int i = 0; i <= AdjVertexList.size() - 1; i++) {
			Vertex k = AdjVertexList.get(i);
			
			if(!k.isSupplyVertex && ((DemandVertex) k).getDemandIsCovered() == false && remainingSupply >= ((DemandVertex) k).getDemand()) {
				possibleAdjDemandVertices.add(k);
			}
		}
		return possibleAdjDemandVertices;
	}
	

	/**
	 * 
	 * @param remainingSupply takes the remaining supply of a graph
	 * @return an int, as the number of vertices that could potentially be covered by the demand
	 */
	public int getNumberofAdjNotCoveredFittingVertexes(int remainingSupply) {
		ArrayList<Vertex> possibleAdjDemandVertices = getListOfAdjNotCoveredFittingVertices(remainingSupply);
		return possibleAdjDemandVertices.size();
	}
	
	
}
