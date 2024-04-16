import java.util.List;

public class JSONGraphConfig {
    private List<JSONVertexConfig> supplyVertices;
    private List<JSONVertexConfig> demandVertices;
    private List<JSONAdjacencyConfig> adjacencies;
    // Getters and setters
    
	public List<JSONVertexConfig> getSupplyVertices() {
		return supplyVertices;
	}
	
	public void setSupplyVertices(List<JSONVertexConfig> supplyVertices) {
		this.supplyVertices = supplyVertices;
	}
	
	public List<JSONVertexConfig> getDemandVertices() {
		return demandVertices;
	}
	
	public void setDemandVertices(List<JSONVertexConfig> demandVertices) {
		this.demandVertices = demandVertices;
	}
	
	public List<JSONAdjacencyConfig> getAdjacencies() {
		return adjacencies;
	}
	
	public void setAdjacencies(List<JSONAdjacencyConfig> adjacencies) {
		this.adjacencies = adjacencies;
	}

   
}




