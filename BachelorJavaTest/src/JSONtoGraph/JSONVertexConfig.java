package JSONtoGraph;
import com.google.gson.annotations.SerializedName;

/**
 * gives a bleprint to convert the JSON in vertices
 * used in GrpahBuilder
 * @author Manuel
 *
 */
public class JSONVertexConfig {
    private int id;
    
    private int value;  // This will be used for either supply or demand
    
    // Getters and setters
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
