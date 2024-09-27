package JSONtoGraph;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


import com.google.gson.Gson;

import GraphStructures.MPGSDGraph;
import VertexStructure.DemandVertex;
import VertexStructure.SupplyVertex;
import VertexStructure.Vertex;


/**
 * used for building MPGSD graphs and to get the desired graphs as objects
 * @author Manuel
 *
 */
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
        
        //gives the JSON structure a meaning, saving them in specified lists
        JSONGraphConfig config = gson.fromJson(reader, JSONGraphConfig.class);
        reader.close();

        //creates a List of all supply vertices out of the List<JSONVertexConfig> from JSONGraphConfig
        ArrayList<SupplyVertex> supplyVertices = new ArrayList<>();
        for (JSONVertexConfig vc : config.getSupplyVertices()) {
            supplyVertices.add(new SupplyVertex(vc.getId(), vc.getValue()));
        }
      
        
        //creates a List of all demand vertices as before
        ArrayList<DemandVertex> demandVertices = new ArrayList<>();
        for (JSONVertexConfig vc : config.getDemandVertices()) {
            demandVertices.add(new DemandVertex(vc.getId(), vc.getValue()));
        }
        
        //creates the MPGSD graph using both Lists of vertices
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
	
	
	/**
	 * Allows us to receive a specific SupXDem graph, with desired connectivity from the ones out of the JSONforGraph folder in the project
	 * @param supply the corresponding supply of the MPGSD graph, we want to receive
	 * @param demand the corresponding demand of the MPGSD graph, we want to receive
	 * @param highlyConnected we can choose whether we want to receive a highly connected or low connected graph
	 * @return return the specific graph, from the available ones
	 * @throws IOException
	 */
	public static MPGSDGraph getGraphSupXDem(int supply, int demand, boolean highlyConnected) throws IOException {
		
		String supplyAsString = Integer.toString(supply);
		String demandAsString = Integer.toString(demand);
		
		//TODO may need to be adjusted for different users based on their systems path
		String basePathEclipse = "src/JSONforGraph";
		
		if(highlyConnected == true) {
			return GraphBuilder.buildGraphFromJson(basePathEclipse +"/highConnectivity/MPGSD_Graph_high_" + supplyAsString + "x" + demandAsString + ".json");
		}else {
			return GraphBuilder.buildGraphFromJson(basePathEclipse +"/lowConnectivity/MPGSD_Graph_low_" + supplyAsString + "x" + demandAsString + ".json");
		}
	
	}

	
	
	/**
	 * Allows us to receive a specific SupXDem graph, with desired connectivity and test number from 0 to 39
	 * from the Literature by Jovanovic et al. (2015), out of the JSONforGraph folder in the project
	 * @param supply the corresponding supply of the MPGSD graph, we want to receive
	 * @param demand the corresponding demand of the MPGSD graph, we want to receive
	 * @param highlyConnected we can choose whether we want to receive a highly connected or low connected graph
	 * @param testNumber the test number of the graph from 0 to 39
	 * @return return the specific graph, from the available ones
	 * @throws IOException
	 */
	public static MPGSDGraph getLitGraphSupXDem(int supAmount, int demAmount, Boolean highlyConnected, int testNumber) throws IOException {
		String supplyAsString = Integer.toString(supAmount);
		String demandAsString = Integer.toString(demAmount);
		
		//TODO may need to be adjusted for different users based on their systems path
		String basePathEclipse = "src/JSONforGraph";
		
		if(highlyConnected == true) {
			return GraphBuilder.buildGraphFromJson(basePathEclipse +"/highConnectivity/test_" + supplyAsString + "_" + demandAsString + "_" + testNumber + ".json");
		}else {
			return GraphBuilder.buildGraphFromJson(basePathEclipse +"/lowConnectivity/test_" + supplyAsString + "_" + demandAsString + "_" + testNumber + ".json");
		}
	}
}
