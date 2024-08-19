package GraphStructures;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

import VertexStructure.DemandVertex;
import VertexStructure.Edge;
import VertexStructure.SupplyVertex;
import VertexStructure.Vertex;

public class SubGraph {
	
	ArrayList<Vertex> subGraph;
	private SupplyVertex subgraphsSupplyVertex;
	//TODO if i dont want to create each graph new maybe.
	private ArrayList<Edge> listOfEdges;
	boolean isComplete = false;
	
	private int subsCovDemand;
	private int subsNumOfDemVer;
	
	
	/**
	 * creates a new Subgraph containing a SupplyVertex as starting vertex
	 * @param supV  SupplyVertex as starting
	 */
	public SubGraph(SupplyVertex supV) {
		subGraph = new ArrayList<>();
		subGraph.add(supV);
		subgraphsSupplyVertex = supV;
		subsNumOfDemVer = 0;
		listOfEdges = new ArrayList<>();
	}
	
	public SubGraph(SubGraph fixedSet) {
		//Deep Copy, to keep fixedSetUntouched
	    subGraph = new ArrayList<>();
	    for (Vertex v : fixedSet.getVertexList()) {
	        // Vertices don't need a copy constructor, because they re being reset.
	        subGraph.add(v);
	    }
		
	    //Deep copy of listOfEdges list
	    listOfEdges = new ArrayList<>();
	    for (Edge e : fixedSet.getListOfEdges()) {
	        // Assuming Edge has a copy constructor or a method to create a new instance
	        listOfEdges.add(e);
	    }
	    
	    //just integer values
		subgraphsSupplyVertex = fixedSet.getSubgraphsSupplyVertex();
		subsNumOfDemVer = fixedSet.getSubsNumOfDemVer();
		subsCovDemand = fixedSet.getSubsCovDemand();
	}
	
	/**
	 * sets the boolean isComplete true
	 * this is used, when the supply is used up/ there are no more adjacent fitting vertices
	 */
	public void setComplete() {
		isComplete = true;
	}
	
	/**
	 * checks if the subgraph is complete or not
	 * @return boolean
	 */
	public boolean isComplete() {
		return isComplete;
	}
	
	/**
	 * adds a vertex to the subgraph
	 * @param v vertex which is added
	 */
	public void addVertex(Vertex v) {
		subGraph.add(v);
	}
	
	/**
	 * get subgraphs supply vertex 
	 * @return the subgraphs SupplyVertex
	 */
	public SupplyVertex getSubgraphsSupplyVertex() {
		return subgraphsSupplyVertex;
	}
	
	public int getSubgraphsRemainingSupply() {
		return getSubgraphsSupplyVertex().getRemainingSupply();
	}
	
	/**
	 * 
	 * @param pos position in the ArrayList, 0 beeing the supplyVertex
	 * @return Vertex on that position
	 */
	public Vertex getSubgraphsVertex(int pos) {
		return subGraph.get(pos);
	}
	
	public ArrayList<Vertex> getVertexList(){
		return subGraph;
	}
	
	public ArrayList<Edge> getListOfEdges() {
		return listOfEdges;
	}
	
	/**
	 * adds an edge between two vertices
	 * @param pre the starting vertex
	 * @param succ the target vertex
	 */
	public void addEdge(Vertex pre, Vertex succ) {
		if(pre == null) {
			System.out.println(succ.getID());
			System.out.println(succ.getIsSupplyVertex());
		}
		listOfEdges.add(new Edge(pre, succ));
	}
	
	
	/**
	 * 
	 * creates an array with every Vertex ID, Vertex Supply/Demand and its predecessor
	 * following this scheme [vertex position(i)][ID]
	 * 											 [supply/demand]
	 * 											 [ID of predecessor]
	 * @return an Array representation of the Subgraph
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
				if(((DemandVertex)v).getPredecessor() == null) {
					
					System.out.println(((DemandVertex)v).getID());
					System.out.println(this.getVertexList());
					printEdges();
				}
				
				
				vertexAsArray[i][2] = ((DemandVertex)v).getPredecessor().getID(); //TODO sometimes throws nullpointer exception
			}
			
		}
		
		
		return vertexAsArray;
	}
	
	public void printOutSubgraph() {
		SupplyVertex SupplyV = (SupplyVertex) subGraph.get(0);
		System.out.println("ID: " + SupplyV.getID() + " Sup" + SupplyV.getInitialSupply());
		for(int i = 1; i <= subGraph.size() - 1; i++) {
			
			DemandVertex v = (DemandVertex) subGraph.get(i);
			System.out.println("ID: " + v.getID() + " Dem" + v.getDemand() + " Pre: " + v.getPredecessor().getID());
		}
	}
	

	/**
	 * finds a fitting Vertex to add to the Subgraph 
	 * 1: trait, which selects based on the max demand, which can be fullfilled
	 * 2: trait, which selects based on the number of Adj. Vertices
	 * 3: trait, which uses the Ratio between Demand / number of Adj Vertices
	 * 4: by using random traits of the first three
	 * 5: beeing a random vertex that is adjacent and fitts
	 * @param traitNumber selects by which criteria the Vertex should be selected
	 * @return Vertex to add that fitts the demand
	 */
	public Vertex[] getVertexToAdd(int traitNumber) {
		//that the trait is set for the new Vertex, so only one trait is used for every new vertex
		int traitNum;
		if(traitNumber == 4) { //4 beeing random trait 
			Random randomNumber = new Random();
			int ranOneTwoThree = randomNumber.nextInt(3) + 1;
			traitNum = ranOneTwoThree;
			
		}else if(traitNumber == 5) { //5 beeing random vertex
			return getRandomVertex();
			
			
		}else if(traitNumber == 6){
			//TODO remove later if not used, if used replace trait 4
			Random randomNumberTest = new Random();
			int ranOneTwoThreeFour = randomNumberTest.nextInt(4) + 1;
			//System.out.println(ranOneTwoThreeFour);
			if(ranOneTwoThreeFour == 4) {
				return getRandomVertex();
			}else {
				traitNum = ranOneTwoThreeFour;
			}
			
			
		}else {
			traitNum = traitNumber;
		}
		
	
		int remainingSupply = getSubgraphsRemainingSupply();
		
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
						trait = selectTrait(traitNum, maxTrait, k);
						
						
						
						
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
	
	
	/**
	 * selctets the trait based on the int numberOfTrait
	 * @param numberOfTrait selected trait
	 * @param currentMax current best trait
	 * @param k Vertex for which the trait is beeing checked
	 * @return array [0]= 1 if vertex k outperformces current 
	 */
	private int[] selectTrait(int numberOfTrait, int currentMax, Vertex k) {
		switch (numberOfTrait) {
		case 1:
			return traitMaxDemand(currentMax, k);
			
		case 2:
			return traitAdjacentVertex(currentMax, k);
			
		case 3:
			return traitDemandAdjVertexRatio(currentMax, k);
		
		case 4:
			return traitRandomTrait(currentMax, k);
			
		default:
			return traitAdjacentVertex(currentMax, k);
		}
	}
	
	/**
	 * trait 1
	 * returns an Array with [0] if vertex k outperformces current best and [1] being its demand
	 * selects the best fitting vertex by selecting the adjacent vertex, which has the most uncovered demand
	 * @param maxTrait current max
	 * @param k Vertex
	 * @return array[0][1]
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
	

	/**
	 * trait 2
	 * returns an Array with [0] if vertex k outperformces current best and [1] being its number of adjacent vertices
	 * selects the best fitting vertex by selecting the one with the most adjacent vertices, which demand is not yet covered
	 * @param maxTrait current max
	 * @param k Vertex
	 * @return
	 */
	private int[] traitAdjacentVertex(int maxTrait, Vertex k) {
		int[] trait = new int[2];
		if(k.getNumberofAdjNotCoveredFittingVertexes(getSubgraphsRemainingSupply()) > maxTrait) {
			trait[0] = 1;
			trait[1] = k.getNumberofAdjNotCoveredFittingVertexes(getSubgraphsRemainingSupply());
			return trait;
		}else {
			trait[0] = 0;
			trait[1] = 0;
			return trait;
		}	
	}
	
	/**
	 * trait 3
	 * returns an Array with [0] if vertex k outperformces current best and [1] being the ratio between demand / number of vertices
	 * selects the best fitting vertex by using a ratio of Demand/Number of adjacent vertices
	 * @param maxTrait current max
	 * @param k Vertex
	 * @return
	 */
	private int[] traitDemandAdjVertexRatio(int maxTrait, Vertex k) {
		int[] trait = new int[2];
		ArrayList<Vertex> listOfAdjVDemNotCovered = k.getListOfAdjNotCoveredFittingVertexes(getSubgraphsRemainingSupply());
		int numberOfAdjVDemNotCovered = listOfAdjVDemNotCovered.size();
		
		int demToCov = ((DemandVertex) k).getDemand();
		double x = (double)demToCov / ((double)(numberOfAdjVDemNotCovered + 1));
		double doubleRatio = 1 / x;
		double upscaledRatio = doubleRatio * 100;
		//System.out.println(maxTrait + " " + doubleRatio + " " + upscaledRatio);
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
	
	/**
	 * trait 4
	 * uses a random trait of 3 as criteria
	 * returns an Array with [0] if vertex k outperformces current best and [1] being the maximum
	 * @param maxTrait current max 
	 * @param k Vertex 
	 * @return
	 */
	public int[] traitRandomTrait(int maxTrait, Vertex k) {
		int[] trait = new int[2];
		//Random number between 1 and 3
		Random randomNumber = new Random();
		int ranOneTwoThree = randomNumber.nextInt(3) + 1;
		switch (ranOneTwoThree) {
		case 1:
			trait = traitAdjacentVertex(maxTrait, k);
			break;
		case 2:
			trait = traitMaxDemand(maxTrait, k);
			break;
		case 3:
			trait = traitDemandAdjVertexRatio(maxTrait, k);
			break;
		default:
			break;
		}
		
		return trait;
	}
	
	/**
	 * returns an Array with [0] being a random Vertex and [1] being its predecessor
	 * with the vertex being a random adjacent vertex
	 * is not a trait like the others, but a function, which can replace the main function
	 * or now in addation can be used as "trait" if traitNumber = 5
	 * @return Array[Vertex][Predecessor]
	 */
	public Vertex[] getRandomVertex() {
		int remainingSupply = getSubgraphsSupplyVertex().getRemainingSupply();
		//TODO null fix?
		Vertex predecessor = null;
		ArrayList<DemandVertex> listOfPossibleAdj = new ArrayList<>();
		//TODO fix predecessor
		ArrayList<Vertex> predecessorList = new ArrayList<>();
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
	
	/**
	 * creates an array of Strings that represents the edges that represent the subgraph
	 * @return Array of Strings representing the edges
	 */
	public String[] getSubgraphsEdgesStringArray() {
		String[] arrayOfEdgesID = new String[listOfEdges.size()];
		for(int i = 0; i <= arrayOfEdgesID.length - 1; i++) {
			arrayOfEdgesID[i] = listOfEdges.get(i).getEdgeKeyString();
		}
		return arrayOfEdgesID;
	}
	
	/**
	 * creates an String of the Edge Array, for better representation of the edges
	 * @return one concatenated String of the edges
	 */
	public String getArrayOfEdgesAsString() {
		String solutionString = "Edges: ";
		String[] arrayOfStrings = getSubgraphsEdgesStringArray();
		for(int i = 0; i <= arrayOfStrings.length - 1; i++) {
			solutionString = solutionString.concat(arrayOfStrings[i] + " ");
		}
		return solutionString;
	}

	/**
	 * 
	 * @return int subgraphs total covered demand
	 */
	public int getSubsCovDemand() {
		return subsCovDemand;
	}
	
	/**
	 * 
	 * @return int number of demand vertices in subgraph
	 */
	public int getSubsNumOfDemVer() {
		return subsNumOfDemVer;
	}

	/**
	 * adds total demand covered
	 * @param covDemand the demand to be added to the total demand covered
	 */
	public void updateSubsCovDemand(int covDemand) {
		subsCovDemand += covDemand;
	}

	/**
	 * adds 1 to the number of demand vertices in the subgraph
	 */
	public void addOneNumDemVer() {
		subsNumOfDemVer += 1;
	}
	
	/**
	 * Checks if the subgraphs vertices are connected.
	 * @return boolean indicating if the subgraph is connected
	 */
	public boolean checkConnectivity() {
		
		//just in case, but wont happen, due to supply vertex initialisation
		if(subGraph.isEmpty()) {
			System.out.println("subgraph is empty");
			return true;
		}
		
		 // Use DFS to check connectivity
        Set<Vertex> visited = new HashSet<>();
        Stack<Vertex> stack = new Stack<>();
        stack.push(subGraph.get(0));

        while (!stack.isEmpty()) {
            Vertex current = stack.pop();
            if (!visited.contains(current)) {
                visited.add(current);
                for (Vertex neighbor : current.getAdjVertexList()) {
                    if (subGraph.contains(neighbor) && !visited.contains(neighbor)) {
                        stack.push(neighbor);
                    }
                }
            }
        }

        return visited.size() == subGraph.size();
	}
	
	public SubGraph extractConnectedComponent() {
	    if (subGraph.isEmpty()) {
	        System.out.println("subgraph is empty");
	        return this;
	    }

	    // Use DFS to find connected component
	    Set<Vertex> visited = new HashSet<>();
	    Stack<Vertex> stack = new Stack<>();
	    stack.push(subGraph.get(0));

	    SubGraph connectedSubGraph = new SubGraph((SupplyVertex)subGraph.get(0));
	    
	    while (!stack.isEmpty()) {
	        Vertex current = stack.pop();
	        if (!visited.contains(current)) {
	            visited.add(current);
	            for (Vertex neighbor : current.getAdjVertexList()) {
	                if (subGraph.contains(neighbor) && !visited.contains(neighbor)) {
	                    stack.push(neighbor);
	                    connectedSubGraph.addEdge(current, neighbor);
	                }
	            }
	        }
	    }

	    // Create a new subgraph with only the connected vertices
	    
	    for (Vertex v : visited) {
	    	if(!connectedSubGraph.getVertexList().contains(v)) {
	    		connectedSubGraph.addVertex(v);

	    	}
	        
	     
	    }
	    
	    // Identify disconnected vertices and reset their previous set status (predecessor/ successor/ dmeandCovered boolean)
	    for (Vertex v : subGraph) {
	        if (!visited.contains(v) && !v.getIsSupplyVertex()) {
	            ((DemandVertex) v).resetDemandVertex();
	        }
	    }

	    return connectedSubGraph;
	}
	
	
	
	public void printEdges() {
		
		String edgeString = "Edges in Subgraph: ";
		for(Edge e: listOfEdges) {
			edgeString = edgeString.concat( e.getEdgeKeyString() + "|");
		}
		System.out.println(edgeString);
	}

	


	
	


	
	
	
	
	
	
	
}