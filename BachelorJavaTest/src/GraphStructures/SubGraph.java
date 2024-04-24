package GraphStructures;
import java.util.LinkedList;
import java.util.Random;

import VertexStructure.DemandVertex;
import VertexStructure.Edge;
import VertexStructure.SupplyVertex;
import VertexStructure.Vertex;

public class SubGraph {
	LinkedList<Vertex> subGraph;
	private SupplyVertex subgraphsSupplyVertex;
	//TODO if i dont want to create each graph new maybe.
	private LinkedList<Edge> listOfEdges = new LinkedList<>();
	boolean isComplete = false;
	
	private int subsCovDemand;
	private int subsNumOfDemVer;
	
	
	/*
	 * creates a new Subgraph containing a SupplyVertex as starting vertex
	 */
	public SubGraph(SupplyVertex supV) {
		subGraph = new LinkedList<>();
		subGraph.add(supV);
		subgraphsSupplyVertex = supV;
		subsNumOfDemVer = 0;
	}
	
	/*
	 * sets the boolean isComplete true
	 * this is used, when the supply is used up/ there are no more adjacent fitting vertices
	 */
	public void setComplete() {
		isComplete = true;
	}
	
	public boolean isComplete() {
		return isComplete;
	}
	
	public void addVertex(Vertex v) {
		subGraph.add(v);
	}
	/*
	 * returns the Subgraphs SupplyVertex
	 */
	public SupplyVertex getSubgraphsSupplyVertex() {
		return subgraphsSupplyVertex;
	}
	
	public Vertex getSubgraphsVertex(int pos) {
		return subGraph.get(pos);
	}
	
	public LinkedList<Vertex> getVertexList(){
		return subGraph;
	}
	
	public LinkedList<Edge> getListOfEdges() {
		return listOfEdges;
	}
	
	public void addEdge(Vertex pre, Vertex succ) {
		listOfEdges.add(new Edge(pre, succ));
	}
	
	
	/*
	 * returns an Array of the Subgraph
	 * with every Vertex ID, Vertex Supply/Demand and its predecessor
	 */
	public int[][] getMathematicalRepresentationOfSubgraph() {
		// [ID][Dem/Sup][Predecessor]
		int[][] vertexAsArray = new int[subGraph.size()][3];
		for(int i = 0; i <= subGraph.size() - 1; i++) {
			Vertex v = subGraph.get(i);
			vertexAsArray[i][0] = v.getID();
			if(v.getIsSupplyVertex()) {
				vertexAsArray[i][1] = ((SupplyVertex)v).getInitialSupply();
				//Predecessor is itself
				vertexAsArray[i][2] = ((SupplyVertex)v).getID();
			}
			if(!v.getIsSupplyVertex()) {
				vertexAsArray[i][1] = -((DemandVertex)v).getDemand();
				vertexAsArray[i][2] = ((DemandVertex)v).getPredecessor().getID();
			}
			
		}
		
		
		return vertexAsArray;
	}
	
	/*
	 * finds a fitting Vertex to add to the Subgraph
	 * @param traitNumber selects by which criteria the Vertex should be selected
	 * 1: trait, which selects based on the max demand, which can be fullfilled
	 * 2: trait, which selects based on the number of Adj. Vertices
	 * 3: trait, which uses the Ratio between Demand / number of Adj Vertices
	 * 4: by using random traits of the first three
	 */
	public Vertex[] getVertexToAdd(int traitNumber) {
		//because first element in subgraph is always the supply Vertex
		
		int remainingSupply = getSubgraphsSupplyVertex().getRemainingSupply();
		//TODO null fix?
		DemandVertex bestFittingDemandVertex = null;
		Vertex predecessor = null;
		
		int maxTrait = 0;
		
		//Iteration over every Vertex in Subgraph
		for (int i = 0; i <= subGraph.size() - 1; i++ ) {
			Vertex v = subGraph.get(i);
			
			//Iteration over ervery Adj Vertex of Vertex v
			for (int j = 0; j <= v.getAdjVertexList().size() - 1; j++) {
				Vertex k = v.getAdjVertexList().get(j);
				if(!k.getIsSupplyVertex()) {
					int currentDemand = ((DemandVertex)k).getDemand();
					if(currentDemand <= remainingSupply && ((DemandVertex) k).getDemandIsCovered() == false) {
						
						int[] trait = new int[2];
						
						
						//TODO (IMPORTANT) can be changed to random trait
						//implement trait by which element should be selected
						//trait = traitMaxDemand(maxTrait, k);
						trait = selectTrait(traitNumber, maxTrait, k);
						
						
						
						
						//trait[0] == 1 when true trait[1] == new max value
						if(trait[0] == 1) {
							
							maxTrait = trait[1];
							bestFittingDemandVertex = ((DemandVertex)k);
							predecessor = v;
						}
					}
				}
					
			}
		}
		return new Vertex[] {bestFittingDemandVertex, predecessor};
	}
	
	
	/*
	 * selctets the trait based on the int numberOfTrait
	 */
	public int[] selectTrait(int numberOfTrait, int remainingSupply, Vertex k) {
		switch (numberOfTrait) {
		case 1:
			return traitMaxDemand(remainingSupply, k);
			
		case 2:
			return traitAdjacentVertex(remainingSupply, k);
			
		case 3:
			return traitDemandAdjVertexRatio(remainingSupply, k);
		
		case 4:
			return traitRandomTrait(remainingSupply, k);
			
		default:
			return traitAdjacentVertex(remainingSupply, k);
		}
	}
	
	/*
	 * trait 1
	 * returns an Array with [0] being the best fitting Vertex and [1] being its predecessor
	 * selects the best fitting vertex by selecting the adjacent vertex, which has the most uncovered demand
	 */
	private int[] traitMaxDemand(int maxTrait, Vertex k) {
		int[] trait = new int[2];
		if(maxTrait < ((DemandVertex)k).getDemand()) {
			trait[0] = 1;
			trait[1] = ((DemandVertex)k).getDemand();
			return trait;
		}else {
			trait[0] = 0;
			trait[1] = 0;
			return trait;
		}
	}
	
	//TODO add that it only checks for Adjacent which demand isnt covered
	/*
	 * trait 2
	 * returns an Array with [0] being the best fitting Vertex and [1] being its predecessor
	 * selects the best fitting vertex by selecting the one with the most adjacent vertices
	 */
	private int[] traitAdjacentVertex(int maxTrait, Vertex k) {
		int[] trait = new int[2];
		if(k.getAdjVertexList().size() > maxTrait) {
			trait[0] = 1;
			trait[1] = k.getAdjVertexList().size();
			return trait;
		}else {
			trait[0] = 0;
			trait[1] = 0;
			return trait;
		}	
	}
	
	/*
	 * trait 3
	 * returns an Array with [0] being the best fitting Vertex and [1] being its predecessor
	 * selects the best fitting vertex by using a ratio of Demand/Number of adjacent vertices
	 */
	private int[] traitDemandAdjVertexRatio(int maxTrait, Vertex k) {
		int[] trait = new int[2];
		LinkedList<Vertex> listOfAdjVDemNotCovered = k.getListOfAdjNotCoveredFittingVertexes(getSubgraphsSupplyVertex().getRemainingSupply());
		int numberOfAdjVDemNotCovered = listOfAdjVDemNotCovered.size();
		
		int demToCov = ((DemandVertex) k).getDemand();
		double x = (double)demToCov / ((double)(numberOfAdjVDemNotCovered + 1));
		double doubleRatio = 1 / x;
		double upscaledRatio = doubleRatio * 100;
		int currentRatio = (int) Math.floor(upscaledRatio);
		if(currentRatio > maxTrait) {
			trait[0] = 1;
			trait[1] = currentRatio;
			return trait;
		}
		else {
			trait[0] = 0;
			trait[1] = 0;
			return trait;
		}
	}
	
	/*
	 * trait 4
	 * uses a random trait of 3 as criteria
	 * returns an Array with [0] being the best fitting Vertex and [1] being its predecessor
	 */
	public int[] traitRandomTrait(int remainingSupply, Vertex k) {
		int[] trait = new int[2];
		//Random number between 1 and 3
		Random randomNumber = new Random();
		int ranOneTwoThree = randomNumber.nextInt(3) + 1;
		switch (ranOneTwoThree) {
		case 1:
			trait = traitAdjacentVertex(remainingSupply, k);
			break;
		case 2:
			trait = traitMaxDemand(remainingSupply, k);
			break;
		case 3:
			trait = traitDemandAdjVertexRatio(remainingSupply, k);
			break;
		default:
			break;
		}
		
		return trait;
	}
	
	/*
	 * returns an Array with [0] being the best fitting Vertex and [1] being its predecessor
	 * with the vertex being a random adjacent vertex
	 * is not a trait, but a function, which can replace the main function
	 */
	public Vertex[] getRandomVertex() {
		int remainingSupply = getSubgraphsSupplyVertex().getRemainingSupply();
		//TODO null fix?
		Vertex predecessor = null;
		LinkedList<DemandVertex> listOfPossibleAdj = new LinkedList<>();
		//TODO fix predecessor
		LinkedList<Vertex> predecessorList = new LinkedList<>();
		//Iteration over every Vertex in Subgraph
		for (int i = 0; i <= subGraph.size() - 1; i++ ) {
			Vertex v = subGraph.get(i);
			
			//Iteration over every adjacent Vertex of Vertex v
			for (int j = 0; j <= v.getAdjVertexList().size() - 1; j++) {
				Vertex k = v.getAdjVertexList().get(j);
				if(!k.getIsSupplyVertex()) {
					int currentDemand = ((DemandVertex)k).getDemand();
					if(currentDemand <= remainingSupply && ((DemandVertex) k).getDemandIsCovered() == false) {
						listOfPossibleAdj.add((DemandVertex) k);
						predecessorList.add(v);
					}
				}
					
			}
		}
		//now all possible Vertexes are added to listOfPossibleAdj
		Vertex ranAdjVer = null;
		
		int sizeList = listOfPossibleAdj.size();
		if(sizeList > 0) {
			Random randomNumber = new Random();
			int ranAdj = randomNumber.nextInt(sizeList);
			ranAdjVer = listOfPossibleAdj.get(ranAdj);
			predecessor = predecessorList.get(ranAdj);
		}
		return new Vertex[] {ranAdjVer, predecessor};
	}
	
	
	public String[] getSubgraphsEdgesStringArray() {
		String[] arrayOfEdgesID = new String[listOfEdges.size()];
		for(int i = 0; i <= arrayOfEdgesID.length - 1; i++) {
			arrayOfEdgesID[i] = listOfEdges.get(i).getEdgeKeyString();
		}
		return arrayOfEdgesID;
	}
	
	public String getArrayOfEdgesAsString() {
		String solutionString = "Edges: ";
		String[] arrayOfStrings = getSubgraphsEdgesStringArray();
		for(int i = 0; i <= arrayOfStrings.length - 1; i++) {
			solutionString = solutionString.concat(arrayOfStrings[i] + " ");
		}
		return solutionString;
	}

	public int getSubsCovDemand() {
		return subsCovDemand;
	}
	
	public int getSubsNumOfDemVer() {
		return subsNumOfDemVer;
	}

	
	public void updateSubsCovDemand(int covDemand) {
		subsCovDemand += covDemand;
	}

	public void addOneNumDemVer() {
		subsNumOfDemVer += 1;
	}


	
	


	
	
	
	
	
	
	
}