package JSONtoGraph;


/**
 * gives a blueprint to convert the JSON in vertices
 * used in GrpahBuilder
 * @author Manuel
 *
 */
public class JSONVertexConfig {
    private int id;
    
    //value will be used for either supply or demand, depending on type of vertex
    private int value;  
    
    //getters and setters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}

    
    
    
}
