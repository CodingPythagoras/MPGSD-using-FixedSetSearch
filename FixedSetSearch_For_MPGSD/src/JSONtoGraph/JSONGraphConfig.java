package JSONtoGraph;
import java.util.List;

/**
 * functions as blueprint to convert the JSON text to concrete our objects
 * @author Manuel
 *
 */
public class JSONGraphConfig {
    private List<JSONVertexConfig> supplyVertices;
    private List<JSONVertexConfig> demandVertices;
    private List<JSONAdjacencyConfig> adjacencies;
    //getters and setters
    
    
    
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




