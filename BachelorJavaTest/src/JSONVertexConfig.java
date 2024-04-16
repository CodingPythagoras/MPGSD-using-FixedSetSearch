import com.google.gson.annotations.SerializedName;

public class JSONVertexConfig {
    private int id;
    
    private int value;  // This will be used for either supply or demand
    
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

    
    
    // Getters and setters
}
