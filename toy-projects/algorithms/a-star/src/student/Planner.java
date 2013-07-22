package student;

import cz.cvut.atg.zui.astar.AbstractOpenList;
import cz.cvut.atg.zui.astar.PlannerInterface;
import cz.cvut.atg.zui.astar.RoadGraph;
import cz.cvut.atg.zui.astar.Utils;
import eu.superhub.wp5.planner.planningstructure.GraphEdge;
import eu.superhub.wp5.planner.planningstructure.GraphNode;
import eu.superhub.wp5.planner.planningstructure.PermittedMode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Planner implements PlannerInterface<GraphNode> {
    private OpenList<GraphNode> openList;

    @Override
    public List<GraphEdge> plan(RoadGraph graph, GraphNode origin, GraphNode destination) {
        final Map<GraphNode, Double> uniformCosts = new HashMap<>();
        final Map<GraphNode, Double> heuristics = new HashMap<>();
        final Map<GraphNode, GraphEdge> cameFrom = new HashMap<>();

        openList = new OpenList<>();
        final Set<GraphNode> closedList = new HashSet<>();

        final double maxSpeed = computeMaxSpeed(graph, origin);

        uniformCosts.put(origin, 0.0);
        heuristics.put(origin, computeHeuristics(origin, destination, maxSpeed));

        openList.setNextCost(uniformCosts.get(origin) + heuristics.get(origin));
        openList.add(origin);

        GraphNode currentNode = origin;
        while (!openList.isEmpty()) {
            currentNode = openList.retrieveBest();
            if (currentNode.equals(destination)) {
                break;
            }

            List<GraphEdge> outgoingEdges = graph.getNodeOutcomingEdges(currentNode.getId());
            if (outgoingEdges == null) {
                outgoingEdges = Collections.emptyList();
            }

            closedList.add(currentNode);
            for (GraphEdge childEdge : outgoingEdges) {
                if (!childEdge.getPermittedModes().contains(PermittedMode.CAR)) {
                    continue;
                }

                GraphNode child = graph.getNodeByNodeId(childEdge.getToNodeId());
                if (closedList.contains(child)) {
                    // by the way, since the currentNode is already in the closed list,
                    // we eliminate self-loops
                    continue;
                }

                double immediateCost = computeCost(childEdge);
                double nextUniformCost = uniformCosts.get(currentNode) + immediateCost;
                double nextHeuristics = Math.max(
                        computeHeuristics(child, destination, maxSpeed), heuristics.get(currentNode) - immediateCost
                ); //pathmax

                double astarCost = nextUniformCost + nextHeuristics;
                if (!openList.contains(child)) {
                    openList.setNextCost(astarCost);
                    openList.add(child);

                    uniformCosts.put(child, nextUniformCost);
                    heuristics.put(child, nextHeuristics);
                    cameFrom.put(child, childEdge);
                } else if (nextUniformCost < uniformCosts.get(child)) {
                    openList.update(child, astarCost);

                    uniformCosts.put(child, nextUniformCost);
                    heuristics.put(child, nextHeuristics);
                    cameFrom.put(child, childEdge);
                }
            }
        }

        if (currentNode.equals(destination)) {
            return reconstructPath(cameFrom, graph, currentNode);
        }
        return null;
    }

    private void removeIncomingEdges(List<GraphEdge> allEdges, RoadGraph graph, GraphNode node) {
        Collection<GraphEdge> incomingEdges = graph.getNodeIncomingEdges(node.getId());
        if (incomingEdges == null) {
            incomingEdges = Collections.emptyList();
        }
        allEdges.removeAll(incomingEdges);
    }

    private double computeMaxSpeed(RoadGraph graph, GraphNode origin) {
        List<GraphEdge> allEdges = new ArrayList<>(graph.getAllEdges());
        removeIncomingEdges(allEdges, graph, origin);

        double maxSpeed = 1.0;
        for (GraphEdge edge : allEdges) {
            maxSpeed = Math.max(maxSpeed, edge.getAllowedMaxSpeedInKmph());
        }
        return maxSpeed;
    }

    private double computeCost(GraphEdge edge) {
        return edge.getLengthInMetres() / (edge.getAllowedMaxSpeedInKmph() * 1000.0D);
    }

    private double computeHeuristics(GraphNode first, GraphNode destination, double maxSpeedKmph) {
        return Utils.distanceInKM(first, destination) / maxSpeedKmph;
    }

    private List<GraphEdge> reconstructPath(Map<GraphNode, GraphEdge> cameFrom, RoadGraph graph, GraphNode current) {
        LinkedList<GraphEdge> path = new LinkedList<>();

        while (cameFrom.containsKey(current)) {
            GraphEdge edge = cameFrom.get(current);

            path.addFirst(edge);
            current = graph.getNodeByNodeId(edge.getFromNodeId());
        }
        return path;
    }

    @Override
    public AbstractOpenList<GraphNode> getOpenList() {
        return openList;
    }
}
