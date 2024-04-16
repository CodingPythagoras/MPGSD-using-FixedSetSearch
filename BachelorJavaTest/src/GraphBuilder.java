import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;

public class GraphBuilder {
	/*
	 * builds a MPGSD Graph using a JSON
	 */
	public static MPGSDGraph buildGraphFromJson(String jsonFilePath) throws IOException {
        Gson gson = new Gson();
        FileReader reader = new FileReader(jsonFilePath);
        JSONGraphConfig config = gson.fromJson(reader, JSONGraphConfig.class);
        reader.close();

        LinkedList<SupplyVertex> supplyVertices = new LinkedList<>();
        for (JSONVertexConfig vc : config.getSupplyVertices()) {
            supplyVertices.add(new SupplyVertex(vc.getId(), vc.getValue()));
            //System.out.println("Test supplyVertices: " + vc.getValue());
        }
        //System.out.println("Test supplyVertices: " + supplyVertices);
        LinkedList<DemandVertex> demandVertices = new LinkedList<>();
        for (JSONVertexConfig vc : config.getDemandVertices()) {
            demandVertices.add(new DemandVertex(vc.getId(), vc.getValue()));
        }
        //System.out.println("Test demandVertices: " + demandVertices);
        MPGSDGraph graph = new MPGSDGraph(supplyVertices, demandVertices);
     
        for (JSONAdjacencyConfig ac : config.getAdjacencies()) {
            Vertex source = graph.getVertexById(ac.getSource());
            for (Integer targetId : ac.getTargets()) {
                Vertex target = graph.getVertexById(targetId);
                if (source != null && target != null) {
                    source.addAdjVertex(target);
                }
            }
        }

        return graph;
    }
}
