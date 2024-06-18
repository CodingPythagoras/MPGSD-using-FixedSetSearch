import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import GraphStructures.MPGSDGraph;
import GraphStructures.SolvedGraph;
import GraphStructures.SubGraph;
import VertexStructure.DemandVertex;
import VertexStructure.SupplyVertex;
import VertexStructure.Vertex;

public class OptimalMPGSDSolver {

    public static SolvedGraph findOptimalSolution(MPGSDGraph g) {

    	List<List<List<DemandVertex>>> allCombinationsForEachSupply  = generateAllCombinationsForEachSub(g);
    	System.out.println("Size: " + allCombinationsForEachSupply.size());
    	System.out.println("PossiblecombinationsSup1: " + allCombinationsForEachSupply.get(0).size());
    	
    	
    	SolvedGraph optimalSolution = null;
    	List<DemandVertex> optimalCombination = null;
    	int maxCoveredDemand = 0;

    	List<List<DemandVertex>> allSolutions = addUpAllPossibleCombinations(allCombinationsForEachSupply);
    	analyseViableCombinations();
    	for(List<DemandVertex> graphCombination : allSolutions) {
    		int covDem = analyseCovDem(graphCombination);
    		if (covDem > maxCoveredDemand) {
    			maxCoveredDemand = covDem;
    			optimalCombination = graphCombination;
    		}
    	}
    	optimalSolution = buildSolutionFromList(optimalCombination);
    	
    	
        return optimalSolution;
    }


	private static SolvedGraph buildSolutionFromList(List<DemandVertex> optimalCombination) {
		// TODO Auto-generated method stub
		return null;
	}


	private static int analyseCovDem(List<DemandVertex> graphCombination) {
		// TODO Auto-generated method stub
		return 0;
	}


	private static void analyseViableCombinations() {
		// TODO Auto-generated method stub
		
	}


	private static List<List<DemandVertex>> addUpAllPossibleCombinations(
			List<List<List<DemandVertex>>> allCombinationsForEachSupply) {
		// TODO Auto-generated method stub
		return null;
	}


	private static List<List<List<DemandVertex>>> generateAllCombinationsForEachSub(MPGSDGraph g) {
    	List<List<List<DemandVertex>>> listWithAllPossibleSol = new ArrayList<>();
    	List<List<DemandVertex>> allDemandCombinations = getAllDemandCombinations(g.getListOfDemandVertexes());
    	
    	for(int i = 0; i < g.getNumberofSupplyVertexes(); i++) {
    		SupplyVertex supplyVertex = g.getListOfSupplyVertexes().get(i);
    		List<List<DemandVertex>> allRemainingCombForSupply = new ArrayList<>();
    		
    		//Empty List represents the SupplyVertex alone
    		 allRemainingCombForSupply.add(new ArrayList<>());
    		 
    		 for (List<DemandVertex> demandCombination : allDemandCombinations) {
    	        	if(demandCombinationIsValid(demandCombination, supplyVertex)) {
    	        		allRemainingCombForSupply.add(demandCombination);
    	        	}
    	          
    	        }
    		 listWithAllPossibleSol.add(allRemainingCombForSupply);
    	}
        
        

       
        return listWithAllPossibleSol;
    }

    private static List<List<DemandVertex>> getAllDemandCombinations(List<DemandVertex> demandVertices) {
        List<List<DemandVertex>> allCombinations = new ArrayList<>();
        int n = demandVertices.size();
        
        for (int i = 1; i < (1 << n); i++) { // Generate all non-empty subsets
            List<DemandVertex> combination = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                if ((i & (1 << j)) != 0) {
                    combination.add(demandVertices.get(j));
                }
            }
            allCombinations.add(combination);
            
        }
        System.out.println("All Combi: " + allCombinations.size());
        return allCombinations;
    }
    
    
    private static boolean demandCombinationIsValid(List<DemandVertex> combination, SupplyVertex supplyVertex) {
    	
    	// Ensure the supply vertex is connected to at least one vertex in the combination
        boolean supplyConnected = false;
        for (DemandVertex demandVertex : combination) {
            if (supplyVertex.getAdjVertexList().contains(demandVertex)) {
                supplyConnected = true;
                break;
            }
        }
        if (!supplyConnected) {
            return false;
        }
    	
    	int totalDemand = 0;
    	// Ensure each demand vertex is connected to at least one other vertex in the combination
        for (DemandVertex demandVertex : combination) {
        	totalDemand = totalDemand + demandVertex.getDemand();
            boolean containsAtLeastOne = false;
            
            for (DemandVertex otherVertex : combination) {
            	
                if (demandVertex != otherVertex && demandVertex.getAdjVertexList().contains(otherVertex)) {
                    containsAtLeastOne = true;
                    break;
                }
            }
            
            if (!containsAtLeastOne || totalDemand > supplyVertex.getInitialSupply()) {
                return false;
            }
        }
        
        return true;
    }

}



