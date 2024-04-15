import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class GraphBuilder {
	public MPGSDGraph buildGraphFromJson(String jsonFilePath) throws IOException {
        Gson gson = new Gson();
        FileReader reader = new FileReader(jsonFilePath);
        JSONGraphConfig config = gson.fromJson(reader, JSONGraphConfig.class);
        reader.close();

        List<SupplyVertex> supplyVertices = new ArrayList<>();
        for (JSONVertexConfig vc : config.getSupplyVertices()) {
            supplyVertices.add(new SupplyVertex(vc.getId(), vc.getValue()));
        }

        List<DemandVertex> demandVertices = new ArrayList<>();
        for (JSONVertexConfig vc : config.getDemandVertices()) {
            demandVertices.add(new DemandVertex(vc.getId(), vc.getValue()));
        }

        MPGSDGraph graph = new MPGSDGraph();
        graph.addVertices(supplyVertices);
        graph.addVertices(demandVertices);

        for (AdjacencyConfig ac : config.getAdjacencies()) {
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
