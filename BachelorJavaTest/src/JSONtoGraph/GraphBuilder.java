package JSONtoGraph;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import com.google.gson.Gson;

import GraphStructures.MPGSDGraph;
import VertexStructure.DemandVertex;
import VertexStructure.SupplyVertex;
import VertexStructure.Vertex;

public class GraphBuilder {
	
	/**
	 * builds a MPGSD Graph using a JSON
	 * @param jsonFilePath JSON which is used to create the graph
	 * @return MPGSDGraph with adjacencies demand and supply
	 * @throws IOException
	 */
	public static MPGSDGraph buildGraphFromJson(String jsonFilePath) throws IOException {
        Gson gson = new Gson();
        FileReader reader = new FileReader(jsonFilePath);
        //gives the JSON structure a meaning
        JSONGraphConfig config = gson.fromJson(reader, JSONGraphConfig.class);
        reader.close();

        //creates a List of all supply vertices
        ArrayList<SupplyVertex> supplyVertices = new ArrayList<>();
        for (JSONVertexConfig vc : config.getSupplyVertices()) {
            supplyVertices.add(new SupplyVertex(vc.getId(), vc.getValue()));
            //System.out.println("Test supplyVertices: " + vc.getValue());
        }
        //System.out.println("Test supplyVertices: " + supplyVertices);
        
        //creates a List of all demand vertices
        ArrayList<DemandVertex> demandVertices = new ArrayList<>();
        for (JSONVertexConfig vc : config.getDemandVertices()) {
            demandVertices.add(new DemandVertex(vc.getId(), vc.getValue()));
        }
        //System.out.println("Test demandVertices: " + demandVertices);
        
        //creates the MPGSD graph using those List of vertices
        MPGSDGraph graph = new MPGSDGraph(supplyVertices, demandVertices);
     
        //adds the adjacencies to all the vertices
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
	
	public static MPGSDGraph getGraphSupXDem(int supply, int demand, boolean highlyConnected) throws IOException {
		
		String supplyAsString = Integer.toString(supply);
		String demandAsString = Integer.toString(demand);
		
		if(highlyConnected == true) {
			return GraphBuilder.buildGraphFromJson("src\\JSONforGraph\\highConnectivity\\MPGSD_Graph_high_" + supplyAsString + "x" + demandAsString + ".json");
		}else {
			return GraphBuilder.buildGraphFromJson("src\\JSONforGraph\\lowConnectivity\\MPGSD_Graph_low_" + supplyAsString + "x" + demandAsString + ".json");
		}
	
	}
}
