package GraphStructures;
import java.util.LinkedList;

import VertexStructure.DemandVertex;
import VertexStructure.SupplyVertex;
import VertexStructure.Vertex;

public class MPGSDGraph {
	private LinkedList<SupplyVertex> listOfSupplyVertexes;
	private LinkedList<DemandVertex> listOfDemandVertexes;
	
	/*
	 * creates a MPGSD graph using a List of supply vertices and List of demand vertices
	 */
	public MPGSDGraph(LinkedList<SupplyVertex> supList, LinkedList<DemandVertex> demList) {
		listOfSupplyVertexes = supList;
		listOfDemandVertexes = demList;
		
	}
	
	/*
	 * returns the highest supply vertex in the MPGSD graph
	 * TODO dont know if used
	 */
	public SupplyVertex getHighestSupplyVertex() {
		int currentMaxSupply = 0;
		SupplyVertex currentMaxSupplyVertex = null;
		for(int i = 0; i <= listOfSupplyVertexes.size() - 1; i++) {
			SupplyVertex k = listOfSupplyVertexes.get(i);
			int currentSupply = k.getRemainingSupply();
			if (currentSupply > currentMaxSupply) {
				currentMaxSupply = currentSupply;
				currentMaxSupplyVertex = k;
			}
			
		}
		if (currentMaxSupply == 0) {
			//TODO FixNull?
			return null;
		}
		else {
			return currentMaxSupplyVertex;
		}
	}
	
	
	public LinkedList<SupplyVertex> getListOfSupplyVertexes() {
		return listOfSupplyVertexes;
	}
	
	public LinkedList<DemandVertex> getListOfDemandVertexes() {
		return listOfDemandVertexes;
	}
	
	/*
	 * returns the total demand of all demand vertices in the graph
	 */
	public int getTotalMPGSDDemand() {
		int totalMPGSDDem = 0;
		for (DemandVertex k: listOfDemandVertexes) {
			totalMPGSDDem += k.getDemand();
		}
		return totalMPGSDDem;
	}
	
	/*
	 * returns the total supply of all supply vertices in the graph
	 */
	public int getTotalMPGSDSupply() {
		int totalMPGSDSup = 0;
		for (SupplyVertex k: listOfSupplyVertexes) {
			totalMPGSDSup += k.getInitialSupply();
		}
		return totalMPGSDSup;
	}
	
	/*
	 * returns the number of supply vertices, which the MPGSD graph contains
	 */
	public int getNumberofSupplyVertexes() {
		int num = listOfSupplyVertexes.size();
		return num;
	}
	

	/*
	 * searches and returns the vertex with the fitting ID
	 * currently goes over both LinkedLists to find the fitting Vertex by ID
	 */
	public Vertex getVertexById(int source) {
		// TODO improve by sorting ArrayList by ID and using direct access
		for(SupplyVertex supv: listOfSupplyVertexes) {
			if (supv.getID() == source) {
				return supv;
			}
		}
		for(DemandVertex supv: listOfDemandVertexes) {
			if (supv.getID() == source) {
				return supv;
			}
		}
		return null;
	}
	
	/*
	 * creates a Vertex manually
	 * with type being either demand or supply
	 * TODO is obsolete?
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
