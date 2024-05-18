package JSONtoGraph;
import java.util.List;

/**
 * sets the adjacencies between the vertices, using the JSON
 * @author Manuel
 *
 */
public class JSONAdjacencyConfig {
    private int source;
    private List<Integer> targets;
    
	public int getSource() {
		return source;
	}
	public void setSource(int source) {
		this.source = source;
	}
	public List<Integer> getTargets() {
		return targets;
	}
	public void setTargets(List<Integer> targets) {
		this.targets = targets;
	}

    // Getters and setters
}