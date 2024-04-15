import java.util.LinkedList;
import java.util.Random;



public class Vertex {
	private LinkedList<Vertex> AdjVertexList = new LinkedList<>();
	protected boolean isSupplyVertex;
	private int ID;
	
	
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
	
	
	
	
	/*
	 * returns the highest fitting Vertex, that is not already in a subgraph (demand covered),
	 * highest fitting, means Vertex with the most supply, which can be fullfilled
	 */
	public Vertex getHighestFittingAdjVertex(int remainingSupply /*, LinkedList<Vertex> vList*/) {
		int currentMaxDemand = 0;
		Vertex currentMaxAdjDemandVertex = null;
		
		for (int i = 0; i <= AdjVertexList.size() - 1; i++) {
			Vertex k = AdjVertexList.get(i);
			
			
			if (k instanceof DemandVertex) {
				int currentDemand = ((DemandVertex) k).getDemand();
				if(currentMaxDemand < currentDemand && currentDemand <= remainingSupply && ((DemandVertex) k).getDemandIsCovered() == false /*&& !vList.contains(k)*/) {
					currentMaxDemand = currentDemand;
					currentMaxAdjDemandVertex = k;
				}
				
			}
		}
		return currentMaxAdjDemandVertex;	
	}
	
	/*
	 * returns the highest fitting Vertex, that is not already in a subgraph (demand covered),
	 * highest fitting, means Vertex with the most Adj Vertexes, which demand can be fullfilled
	 */
	public Vertex getVertexWMostAdjV(int remainingSupply) {
		int currentMaxAdjV = 0;
		Vertex currentMaxAdjVertex = null;
		
		for (int i = 0; i <= AdjVertexList.size() - 1; i++) {
			Vertex k = AdjVertexList.get(i);
			
			if(!k.isSupplyVertex && ((DemandVertex) k).getDemandIsCovered() == false && remainingSupply >= ((DemandVertex) k).getDemand()) {
				if(k.AdjVertexList.size() > currentMaxAdjV) {
					currentMaxAdjV = k.AdjVertexList.size();
					currentMaxAdjVertex = k;
				}
			}
		}
		return currentMaxAdjVertex;
	}
	
	/*
	 * returns the highest fitting Vertex, that is not already in a subgraph (demand covered),
	 * highest fitting, means Vertex with the best Ratio of 1 / (demand / adjacent Vertexes that are not covered)
	 * and that the demand can be fullfilled
	 */
	public Vertex getVertexUsingDemVertexRatio(int remainingSupply) {
		double currentBestRatio = 0;
		Vertex currentBestRatioVertex = null;
		
		for (int i = 0; i <= AdjVertexList.size() - 1; i++) {
			Vertex k = AdjVertexList.get(i);
			
			if(!k.isSupplyVertex && ((DemandVertex) k).getDemandIsCovered() == false && remainingSupply >= ((DemandVertex) k).getDemand()) {
				int numberOfAdjVDemNotCovered = 0;
				/*
				 * Determines how many Adj vertexes arent supplyed yet
				 */
				for(int j = 0; j <= k.AdjVertexList.size() - 1 ; j++) {
					Vertex kj = k.AdjVertexList.get(j);
					if(!kj.getIsSupplyVertex() && !((DemandVertex) kj).getDemandIsCovered()) {
						numberOfAdjVDemNotCovered += 1;
					}
				}
				int demToCov = ((DemandVertex) k).getDemand();
				double x = demToCov / (numberOfAdjVDemNotCovered + 1);
				double currentRatio = 1 / x;
				System.out.println(x);
				if(currentRatio > currentBestRatio) {
					currentBestRatio = currentRatio;
					currentBestRatioVertex = k;
				}
			}
		}
		return currentBestRatioVertex;
	}
	
	/*
	 * returns the highest fitting Vertex using either
	 * getHighestFittingAdjVertex (Adj Vertex with highest demand)
	 * getVertexWMostAdjV (Vertex with most adj Vertexes)
	 * getVertexUsingDemVertexRatio (determines Vertex by combining both criteria)
	 */
	public Vertex getVertexUsingRandomTrait(int remainingSupply) {
		Vertex randomBestVertex = null;
		//Random number between 1 and 3
		Random randomNumber = new Random();
		int ranOneTwoThree = randomNumber.nextInt(3) + 1;
		switch (ranOneTwoThree) {
		case 1:
			randomBestVertex = getHighestFittingAdjVertex(remainingSupply);
			break;
		case 2:
			randomBestVertex = getVertexWMostAdjV(remainingSupply);
			break;
		case 3:
			randomBestVertex = getVertexUsingDemVertexRatio(remainingSupply);
			break;
		default:
			break;
		}
		
		return randomBestVertex;
	}
	
	public Vertex getRandomAdjVertex(int remainingSupply) {
		
		Vertex ranAdjVer = null;
		LinkedList<Vertex> possibleAdjDemandVertexes = getListOfAdjNotCoveredFittingVertexes(remainingSupply);
		int sizeList = possibleAdjDemandVertexes.size();
		if(sizeList > 0) {
			Random randomNumber = new Random();
			int ranAdj = randomNumber.nextInt(sizeList);
			ranAdjVer = possibleAdjDemandVertexes.get(ranAdj);
		}
		return ranAdjVer;
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
	
	
}
