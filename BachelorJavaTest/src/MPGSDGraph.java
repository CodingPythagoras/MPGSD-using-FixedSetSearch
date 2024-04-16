import java.util.LinkedList;

public class MPGSDGraph {
	private LinkedList<SupplyVertex> listOfSupplyVertexes;
	private LinkedList<DemandVertex> listOfDemandVertexes;
	
	public MPGSDGraph(int graphNR) {
		listOfSupplyVertexes = new LinkedList<>();
		listOfDemandVertexes = new LinkedList<>();
		switch (graphNR) {
		case 1:
			createFirstMPGSDGraph();
			break;

		case 2:
			createSecondMPGSDGraph();
			break;
		default:
			
			break;
		}
		
	}
	
	public MPGSDGraph(LinkedList<SupplyVertex> supList, LinkedList<DemandVertex> demList) {
		listOfSupplyVertexes = supList;
		listOfDemandVertexes = demList;
		
	}
	
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
	
	private void createFirstMPGSDGraph() {
		SupplyVertex sup1 = new SupplyVertex(1, 10);
		SupplyVertex sup2 = new SupplyVertex(2, 5);
		SupplyVertex sup3 = new SupplyVertex(3, 7);
		SupplyVertex sup4 = new SupplyVertex(4, 8);
		listOfSupplyVertexes.add(sup1);
		listOfSupplyVertexes.add(sup2);
		listOfSupplyVertexes.add(sup3);
		listOfSupplyVertexes.add(sup4);
		
		DemandVertex dem1 = new DemandVertex(5, 3);
		DemandVertex dem2 = new DemandVertex(6, 4);
		DemandVertex dem3 = new DemandVertex(7, 2);
		DemandVertex dem4 = new DemandVertex(8, 1);
		
		DemandVertex dem5 = new DemandVertex(9, 5);
		
		DemandVertex dem6 = new DemandVertex(10, 4);
		DemandVertex dem7 = new DemandVertex(11, 3);
		DemandVertex dem8 = new DemandVertex(12, 2);
		DemandVertex dem9 = new DemandVertex(13, 1);
		
		DemandVertex dem10 = new DemandVertex(14, 3);
		DemandVertex dem11 = new DemandVertex(15, 1);
		DemandVertex dem12 = new DemandVertex(16, 2);
		DemandVertex dem13 = new DemandVertex(17, 7);
		listOfDemandVertexes.add(dem1);
		listOfDemandVertexes.add(dem2);
		listOfDemandVertexes.add(dem3);
		listOfDemandVertexes.add(dem4);
		listOfDemandVertexes.add(dem5);
		listOfDemandVertexes.add(dem6);
		listOfDemandVertexes.add(dem7);
		listOfDemandVertexes.add(dem8);
		listOfDemandVertexes.add(dem9);
		listOfDemandVertexes.add(dem10);
		listOfDemandVertexes.add(dem11);
		listOfDemandVertexes.add(dem12);
		listOfDemandVertexes.add(dem13);
		
		//Adjacency initialisation
		sup1.addAdjVertex(dem1);
		sup1.addAdjVertex(dem5);
		sup1.addAdjVertex(sup3);
		
		sup2.addAdjVertex(dem5);
		sup2.addAdjVertex(dem10);
		sup2.addAdjVertex(dem6);
		
		sup3.addAdjVertex(dem6);
		sup3.addAdjVertex(dem8);
		
		sup4.addAdjVertex(dem11);
		sup4.addAdjVertex(dem12);
		sup4.addAdjVertex(dem4);
		
		dem1.addAdjVertex(dem2);
		dem2.addAdjVertex(dem3);
		dem2.addAdjVertex(dem5);
		dem3.addAdjVertex(dem4);
		
		dem10.addAdjVertex(dem11);
		dem12.addAdjVertex(dem13);
		dem7.addAdjVertex(dem6);
		dem7.addAdjVertex(dem8);
		dem9.addAdjVertex(dem7);
		dem9.addAdjVertex(dem8);
		
		
	}
	

	private void createSecondMPGSDGraph() {
		Vertex ver1 = createVertex("supply", 1, 8);
		Vertex ver2 = createVertex("supply", 2, 16);
		Vertex ver3 = createVertex("supply", 3, 9);
		Vertex ver4 = createVertex("supply", 4, 20);
		Vertex ver5 = createVertex("supply", 5, 5);
		Vertex ver6 = createVertex("supply", 6, 2);
		
	}
	
	public LinkedList<SupplyVertex> getListOfSupplyVertexes() {
		return listOfSupplyVertexes;
	}
	
	public LinkedList<DemandVertex> getListOfDemandVertexes() {
		return listOfDemandVertexes;
	}
	
	public int getTotalMPGSDDemand() {
		int totalMPGSDDem = 0;
		for (DemandVertex k: listOfDemandVertexes) {
			totalMPGSDDem += k.getDemand();
		}
		return totalMPGSDDem;
	}
	
	public int getTotalMPGSDSupply() {
		int totalMPGSDSup = 0;
		for (SupplyVertex k: listOfSupplyVertexes) {
			totalMPGSDSup += k.getInitialSupply();
		}
		return totalMPGSDSup;
	}
	
	public int getNumberofSupplyVertexes() {
		int num = listOfSupplyVertexes.size();
		return num;
	}
	
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

	/*
	 * currently goes over both LinkedLists to find the fitting Vertex by ID
	 */
	public Vertex getVertexById(int source) {
		// TODO improve by sorting ArrayList by ID and using direct acess
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
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
}
