import json
import random
import os

def generate_graph(num_supply, num_demand, num_of_min_targets, num_of_max_demand_for_vertices, min_supply_for_vertices, max_supply_for_vertices, num_of_min_demand_for_vertices):
    graph = {
        "supplyVertices": [],
        "demandVertices": [],
        "adjacencies": []
    }

    #generate supply vertices
    for i in range(1, num_supply + 1):
        graph["supplyVertices"].append({"id": i, "value": random.randint(min_supply_for_vertices, max_supply_for_vertices)})

    all_vertices = [v["id"] for v in graph["supplyVertices"]]
    total_demand_vertices_needed = num_demand

    demand_vertex_id = num_supply + 1
    demand_vertices = []
    total_possible_demand_that_can_be_covered = 0
    adjacency_dict = {vertex_id: [] for vertex_id in all_vertices}

    #create exact coverage chains for each supply vertex "snake like approach"
    for supply_vertex in graph["supplyVertices"]:
        supply_value = supply_vertex["value"]
        covered_demand = 0
        previous_vertex = supply_vertex["id"]
        while covered_demand < supply_value:
            remaining_supply = supply_value - covered_demand
            if remaining_supply < num_of_min_demand_for_vertices:
                demand_value = remaining_supply
            else:
                demand_value = random.randint(num_of_min_demand_for_vertices, min(remaining_supply, num_of_max_demand_for_vertices))
            
            demand_vertex = {"id": demand_vertex_id, "value": demand_value}
            demand_vertices.append(demand_vertex)
            all_vertices.append(demand_vertex_id)

            #connect previous vertex to the new demand vertex
            adjacency_dict[previous_vertex].append(demand_vertex_id)
            adjacency_dict[demand_vertex_id] = [previous_vertex]

            covered_demand += demand_value
            previous_vertex = demand_vertex_id
            demand_vertex_id += 1

        total_possible_demand_that_can_be_covered += covered_demand

    #add the remaining demand vertices to meet the total number of demand vertices
    while len(demand_vertices) < total_demand_vertices_needed:
        demand_value = random.randint(num_of_min_demand_for_vertices, num_of_max_demand_for_vertices)
        demand_vertex = {"id": demand_vertex_id, "value": demand_value}
        demand_vertices.append(demand_vertex)
        all_vertices.append(demand_vertex_id)
        adjacency_dict[demand_vertex_id] = []
        #each new demand vertex directly gets num_of_min_targets targets
        for _ in range(num_of_min_targets):
            random_target = random.randint(1, (num_supply + len(demand_vertices)))
            adjacency_dict[demand_vertex_id].append(random_target)
            adjacency_dict[random_target].append(demand_vertex_id)
        demand_vertex_id += 1

    graph["demandVertices"].extend(demand_vertices)

    #ensure the graph is connected
    def is_connected(graph, total_vertices):
        visited = set()

        def dfs(v):
            stack = [v]
            while stack:
                node = stack.pop()
                if node not in visited:
                    visited.add(node)
                    targets = adjacency_dict[node]
                    stack.extend(targets)

        dfs(graph["supplyVertices"][0]["id"])
        return len(visited) == total_vertices

    total_vertices = num_supply + num_demand
    while not is_connected(graph, total_vertices):
        visited = set()

        def dfs(v):
            stack = [v]
            while stack:
                node = stack.pop()
                if node not in visited:
                    visited.add(node)
                    targets = adjacency_dict[node]
                    stack.extend(targets)
            return node

        last_vertex = dfs(graph["supplyVertices"][0]["id"])
        random_target = random.randint(1, total_vertices)
        adjacency_dict[last_vertex].append(random_target)
        adjacency_dict[random_target].append(last_vertex)

    for source, targets in adjacency_dict.items():
        graph["adjacencies"].append({"source": source, "targets": targets})
    
    
    print(f"Total Possible Demand That Can Be Covered: {total_possible_demand_that_can_be_covered}")
    return graph


#parameters can be repalced based on own preferences
num_supply_vertices = 400
num_demand_vertices = 1200

#1 means low connectivity, 10 high (in terms of thesis), but can be chnaged to own preferences
num_of_min_targets = 1

#demand of 25 is average
num_of_min_demand_for_vertices = 10
num_of_max_demand_for_vertices = 40

#(num of demand vertices * average demand) / amount of supplyvertices = max min = 1/10 of it
min_supply_for_vertices = 8
max_supply_for_vertices = 75

#generate graph
graph = generate_graph(num_supply_vertices, num_demand_vertices, num_of_min_targets, num_of_max_demand_for_vertices, min_supply_for_vertices, max_supply_for_vertices, num_of_min_demand_for_vertices)

#write to a JSON file in the same folder as the script
script_dir = os.path.dirname(os.path.abspath(__file__))
output_file_path = os.path.join(script_dir, "MPGSD_Graph_low_" + str(num_supply_vertices) + "x" + str(num_demand_vertices) + ".json")

with open(output_file_path, 'w') as f:
    json.dump(graph, f, indent=4)

print(f"Graph JSON has been written to {output_file_path}")
