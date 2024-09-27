package GraphStructures;
import java.util.ArrayList;

import VertexStructure.DemandVertex;
import VertexStructure.SupplyVertex;
import VertexStructure.Vertex;


/**
 * defines a MPGSD graph with its operations and attributes
 * @author Manuel
 *
 */
public class MPGSDGraph {
	private ArrayList<SupplyVertex> listOfSupplyVertexes;
	private ArrayList<DemandVertex> listOfDemandVertexes;
	private ArrayList<Vertex> listOfAllVertices;
	
	private int totalMPGSDSupply;
	private int totalMPGSDDemand;
	
	/**
	 * creates a MPGSD graph using a List of supply vertices and List of demand vertices
	 * @param supList List of supply vertices
	 * @param demList List of demand vertices
	 */
	public MPGSDGraph(ArrayList<SupplyVertex> supList, ArrayList<DemandVertex> demList) {
		listOfSupplyVertexes = supList;
		listOfDemandVertexes = demList;
		
		listOfAllVertices = new ArrayList<Vertex>();
		listOfAllVertices.addAll(listOfSupplyVertexes);
		listOfAllVertices.addAll(listOfDemandVertexes);
		
		for (SupplyVertex k: listOfSupplyVertexes) {
			totalMPGSDSupply += k.getInitialSupply();
		}
		
		for (DemandVertex k: listOfDemandVertexes) {
			totalMPGSDDemand += k.getDemand();
		}
		
		
		
	}
	
	
	
	/**
	 * 
	 * @return List of all supply vertices in the MPGSD graph
	 */
	public ArrayList<SupplyVertex> getListOfSupplyVertexes() {
		return listOfSupplyVertexes;
	}
	
	
	/**
	 * 
	 * @return List of all demand vertices in the MPGSD graph
	 */
	public ArrayList<DemandVertex> getListOfDemandVertexes() {
		return listOfDemandVertexes;
	}
	

	/**
	 * 
	 * @return the total demand of all demand vertices in the graph
	 */
	public int getTotalMPGSDDemand() {

		return totalMPGSDDemand;
	}
	

	/**
	 * 
	 * @return the total initial supply, the sum of all supply vertices in the graph
	 */
	public int getTotalMPGSDSupply() {
		return totalMPGSDSupply;
	}
	

	/**
	 * 
	 * @return the number of supply vertices, which the MPGSD graph contains
	 */
	public int getNumberofSupplyVertexes() {
		int num = listOfSupplyVertexes.size();
		return num;
	}
	


	/**
	 * searches and returns the vertex with the fitting ID
	 * currently goes over both ArrayLists to find the fitting Vertex by ID
	 * @param source the ID, which will be looked for
	 * @return the corresponding Vertex to the ID
	 */
	public Vertex getVertexById(int source) {
		// TODO improve by sorting ArrayList by ID and using direct access
		for(SupplyVertex supv: listOfSupplyVertexes) {
			if (supv.getID() == source) {
				return supv;
			}
		}
		for(DemandVertex demv: listOfDemandVertexes) {
			if (demv.getID() == source) {
				return demv;
			}
		}
		return null;
	}
	
	
	/**
	 * 
	 * @return a List containing all vertices in the graph
	 */
	public ArrayList<Vertex> getAllVertices(){
		return listOfAllVertices;
	}
	
	/**
	 * Is not being used, was used during testing!
	 * 
	 * can be used to create a Vertex manually
	 * with type being either demand or supply
	 * @param type either "supply" or "demand", based on what type the vertex should be
	 * @param id the id of the manually created vertex
	 * @param value the value (demand or supply based on its type) of the vertex
	 * @return
	 */
	public Vertex createVertex(String type, int id, int value) {
        if ("supply".equals(type)) {
        	SupplyVertex v = new SupplyVertex(id, value);
        	listOfSupplyVertexes.add(v);
            return v;
            
        } else if ("demand".equals(type)) {
        	DemandVertex d = new DemandVertex(id, value);
        	listOfDemandVertexes.add(d);
            return d;
        }
        throw new IllegalArgumentException("Unknown vertex type");
    }
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
}
