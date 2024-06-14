
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import GraphStructures.MPGSDGraph;
import GraphStructures.SolvedGraph;
import GraphStructures.SubGraph;
import VertexStructure.DemandVertex;
import VertexStructure.SupplyVertex;
import VertexStructure.Vertex;

public class OptimalTest {

    public static SolvedGraph findOptimalSolution(MPGSDGraph g) {
        List<List<List<DemandVertex>>> allCombinationsForEachSupply = generateAllCombinationsForEachSub(g);

        SolvedGraph optimalSolution = null;
        int maxCoveredDemand = 0;

        List<List<DemandVertex>> allSolutions = generateAllCombinedSolutions(allCombinationsForEachSupply);
        for (List<DemandVertex> graphCombination : allSolutions) {
            if (isValidCombination(graphCombination)) {
                int coveredDemand = calculateCoveredDemand(graphCombination);
                if (coveredDemand > maxCoveredDemand) {
                    maxCoveredDemand = coveredDemand;
                    optimalSolution = buildSolutionFromCombination(g, graphCombination);
                }
            }
        }

        return optimalSolution;
    }

    private static List<List<List<DemandVertex>>> generateAllCombinationsForEachSub(MPGSDGraph g) {
        List<List<List<DemandVertex>>> listWithAllPossibleSolutions = new ArrayList<>();
        List<List<DemandVertex>> allDemandCombinations = getAllDemandCombinations(g.getListOfDemandVertexes());

        for (SupplyVertex supplyVertex : g.getListOfSupplyVertexes()) {
            List<List<DemandVertex>> validCombinationsForSupply = new ArrayList<>();
            validCombinationsForSupply.add(new ArrayList<>()); // Empty list representing only the supply vertex

            for (List<DemandVertex> demandCombination : allDemandCombinations) {
                if (isCombinationConnectedToSupply(demandCombination, supplyVertex)) {
                    validCombinationsForSupply.add(demandCombination);
                }
            }
            listWithAllPossibleSolutions.add(validCombinationsForSupply);
        }
        return listWithAllPossibleSolutions;
    }

    private static List<List<DemandVertex>> getAllDemandCombinations(List<DemandVertex> demandVertices) {
        List<List<DemandVertex>> allCombinations = new ArrayList<>();
        int n = demandVertices.size();

        for (int i = 1; i < (1 << n); i++) {
            List<DemandVertex> combination = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                if ((i & (1 << j)) != 0) {
                    combination.add(demandVertices.get(j));
                }
            }
            allCombinations.add(combination);
        }
        return allCombinations;
    }

    private static boolean isCombinationConnectedToSupply(List<DemandVertex> combination, SupplyVertex supplyVertex) {
        Set<Vertex> visited = new HashSet<>();
        return isConnected(combination, supplyVertex, visited);
    }

    private static boolean isConnected(List<DemandVertex> combination, Vertex current, Set<Vertex> visited) {
        if (visited.contains(current)) return true;
        visited.add(current);

        for (Vertex adj : current.getAdjVertexList()) {
            if (combination.contains(adj) && !visited.contains(adj)) {
                if (isConnected(combination, adj, visited)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static List<List<DemandVertex>> generateAllCombinedSolutions(List<List<List<DemandVertex>>> allCombinationsForEachSupply) {
        List<List<DemandVertex>> result = new ArrayList<>();
        generateCombinations(new ArrayList<>(), 0, allCombinationsForEachSupply, result);
        return result;
    }

    private static void generateCombinations(List<DemandVertex> current, int index, List<List<List<DemandVertex>>> allCombinationsForEachSupply, List<List<DemandVertex>> result) {
        if (index == allCombinationsForEachSupply.size()) {
            result.add(new ArrayList<>(current));
            return;
        }

        for (List<DemandVertex> combination : allCombinationsForEachSupply.get(index)) {
            current.addAll(combination);
            generateCombinations(current, index + 1, allCombinationsForEachSupply, result);
            current.removeAll(combination);
        }
    }

    private static boolean isValidCombination(List<DemandVertex> combination) {
        Set<DemandVertex> uniqueVertices = new HashSet<>(combination);
        return uniqueVertices.size() == combination.size();
    }

    private static int calculateCoveredDemand(List<DemandVertex> combination) {
        int totalDemand = 0;
        for (DemandVertex demandVertex : combination) {
            totalDemand += demandVertex.getDemand();
        }
        return totalDemand;
    }

    private static SolvedGraph buildSolutionFromCombination(MPGSDGraph g, List<DemandVertex> combination) {
        SolvedGraph solution = new SolvedGraph(g);

        for (DemandVertex demandVertex : combination) {
            SupplyVertex supplyVertex = (SupplyVertex) demandVertex.getPredecessor();
            SubGraph subGraph = solution.getSubgraphWithSupplyVertex(supplyVertex);

            if (subGraph != null) {
                subGraph.addVertex(demandVertex);
                subGraph.addEdge(supplyVertex, demandVertex);
                supplyVertex.useSupply(demandVertex.getDemand());
                solution.addCoveredDemand(demandVertex.getDemand());
                solution.addUsedSupply(demandVertex.getDemand());
                solution.addNumberOfDemandVertexes(1);
            }
        }
        return solution;
    }
}