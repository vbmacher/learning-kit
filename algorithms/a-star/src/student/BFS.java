package student;

import cz.cvut.atg.zui.astar.AbstractOpenList;
import cz.cvut.atg.zui.astar.PlannerInterface;
import cz.cvut.atg.zui.astar.RoadGraph;
import eu.superhub.wp5.planner.planningstructure.GraphEdge;
import eu.superhub.wp5.planner.planningstructure.GraphNode;
import eu.superhub.wp5.planner.planningstructure.PermittedMode;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class BFS implements PlannerInterface<GraphNode> {

    private final static class OpenList extends AbstractOpenList<GraphNode> {
        private final Queue<GraphNode> nodes = new LinkedList<>();

        @Override
        protected boolean addItem(GraphNode item) {
            return nodes.add(item);
        }

        public GraphNode first() {
            return nodes.poll();
        }

        public boolean isEmpty() {
            return nodes.isEmpty();
        }
    }

    private OpenList openList = new OpenList();

    @Override
    public List<GraphEdge> plan(RoadGraph graph, GraphNode origin, GraphNode destination) {
        openList.addItem(origin);

        Map<GraphNode, Double> costs = new HashMap<>();
        Map<GraphNode, GraphEdge> cameFrom = new HashMap<>();

        costs.put(origin, 0.0);
        while (!openList.isEmpty()) {
            GraphNode current = openList.first();

            Collection<GraphEdge> edges = graph.getNodeOutcomingEdges(current.getId());
            if (edges == null) {
                edges = Collections.emptyList();
            }

            for (GraphEdge edge : edges) {
                if (!edge.getPermittedModes().contains(PermittedMode.CAR)) {
                    continue;
                }

                GraphNode child = graph.getNodeByNodeId(edge.getToNodeId());

                double newCost = costs.get(current) + computeCost(edge);

                boolean existing = costs.containsKey(child);
                if (!existing || costs.get(child) > newCost) {
                    costs.put(child, newCost);
                    cameFrom.put(child, edge);
                    openList.add(child);
                }
            }
        }

        if (costs.containsKey(destination)) {
            GraphNode current = destination;
            LinkedList<GraphEdge> path = new LinkedList<>();

            while (cameFrom.containsKey(current)) {
                GraphEdge edge = cameFrom.get(current);

                path.addFirst(edge);
                current = graph.getNodeByNodeId(edge.getFromNodeId());
            }
            return path;
        }

        return null;
    }

    private double computeCost(GraphEdge edge) {
        return edge.getLengthInMetres() / (edge.getAllowedMaxSpeedInKmph() * 1000.0D);
    }

    @Override
    public AbstractOpenList<GraphNode> getOpenList() {
        return openList;
    }
}