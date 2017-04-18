package student;

import cz.cvut.atg.zui.astar.RoadGraph;
import eu.superhub.wp5.graphcommon.graph.EdgeId;
import eu.superhub.wp5.graphcommon.graph.Graph;
import eu.superhub.wp5.graphcommon.graph.GraphBuilder;
import eu.superhub.wp5.planner.planningstructure.GraphEdge;
import eu.superhub.wp5.planner.planningstructure.GraphNode;
import eu.superhub.wp5.wp5common.GPSLocation;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graphs {

    public static RoadGraph straight() {
        GraphBuilder<GraphNode, GraphEdge> builder = new GraphBuilder<>();

        GraphNode node1 = new GraphNode(1, null, null, null);
        GraphNode node2 = new GraphNode(2, null, null, null);
        GraphNode node3 = new GraphNode(3, null, null, null);

        builder.addNode(node1);
        builder.addNode(node2);
        builder.addNode(node3);

        builder.addEdge(new GraphEdge(1,2, 1,1, null));
        builder.addEdge(new GraphEdge(2,3, 1,1, null));

        return new RoadGraph(builder.createGraph());
    }

    public static RoadGraph star() {
        GraphBuilder<GraphNode, GraphEdge> builder = new GraphBuilder<>();

        GraphNode node1 = new GraphNode(1, null, null, null);
        GraphNode node2 = new GraphNode(2, null, null, null);
        GraphNode node3 = new GraphNode(3, null, null, null);

        builder.addNode(node1);
        builder.addNode(node2);
        builder.addNode(node3);

        builder.addEdge(new GraphEdge(1,2, 1,1, null));
        builder.addEdge(new GraphEdge(1,3, 3,1, null));
        builder.addEdge(new GraphEdge(2,3, 1,1, null));

        return new RoadGraph(builder.createGraph());
    }

    public static RoadGraph mustReturn() {
        GraphBuilder<GraphNode, GraphEdge> builder = new GraphBuilder<>();

        GraphNode node1 = new GraphNode(1, null, null, null);
        GraphNode node2 = new GraphNode(2, null, null, null);
        GraphNode node3 = new GraphNode(3, null, null, null);

        builder.addNode(node1);
        builder.addNode(node2);
        builder.addNode(node3);

        builder.addEdge(new GraphEdge(1,2, 1,1, null));
        builder.addEdge(new GraphEdge(1,3, 3,1, null));
        builder.addEdge(new GraphEdge(2,3, 3,1, null));

        return new RoadGraph(builder.createGraph());
    }

    public static RoadGraph mustReturnLevelTwo() {
        GraphBuilder<GraphNode, GraphEdge> builder = new GraphBuilder<>();

        GraphNode node1 = new GraphNode(1, null, null, null);
        GraphNode node2 = new GraphNode(2, null, null, null);
        GraphNode node3 = new GraphNode(3, null, null, null);
        GraphNode node4 = new GraphNode(4, null, null, null);

        builder.addNode(node1);
        builder.addNode(node2);
        builder.addNode(node3);
        builder.addNode(node4);

        builder.addEdge(new GraphEdge(1,2, 1,1, null));
        builder.addEdge(new GraphEdge(1,3, 5,1, null));
        builder.addEdge(new GraphEdge(2,4, 2,1, null));
        builder.addEdge(new GraphEdge(4,3, 3,1, null));

        return new RoadGraph(builder.createGraph());
    }

    public static RoadGraph domka1() {
        GraphBuilder<GraphNode, GraphEdge> builder = new GraphBuilder<>();

        GraphNode node1 = new GraphNode(1, null, null, null);
        GraphNode node2 = new GraphNode(2, null, null, null);
        GraphNode node3 = new GraphNode(3, null, null, null);
        GraphNode node4 = new GraphNode(4, null, null, null);
        GraphNode node5 = new GraphNode(5, null, null, null);
        GraphNode node6 = new GraphNode(6, null, null, null);

        builder.addNode(node1);
        builder.addNode(node2);
        builder.addNode(node3);
        builder.addNode(node4);
        builder.addNode(node5);
        builder.addNode(node6);

        builder.addEdge(new GraphEdge(1,3, 1000,1, null));
        builder.addEdge(new GraphEdge(1,2, 1000,1, null));
        builder.addEdge(new GraphEdge(1,4, 4000,1, null));
        builder.addEdge(new GraphEdge(3,5, 4000,1, null));
        builder.addEdge(new GraphEdge(3,4, 5000,1, null));
        builder.addEdge(new GraphEdge(2,4, 2000,1, null));
        builder.addEdge(new GraphEdge(5,6, 1000,1, null));

        return new RoadGraph(builder.createGraph());

    }





    public static RoadGraph slovakia() {
//        GraphBuilder<GraphNode, GraphEdge> builder = new GraphBuilder<>();
//
//        builder.
//
        GraphNode node1 = new GraphNode(1, new GPSLocation(1,1), "node1", Collections.emptySet());
        GraphNode node2 = new GraphNode(2, new GPSLocation(10,10), "node2", Collections.emptySet());
        GraphNode node3 = new GraphNode(3, new GPSLocation(20,10), "node3", Collections.emptySet());
        GraphNode node4 = new GraphNode(4, new GPSLocation(10,20), "node4", Collections.emptySet());

        Map<Long, GraphNode> nodes = new HashMap<>();
        nodes.put(1L, node1);
        nodes.put(2L, node2);
        nodes.put(3L, node3);
        nodes.put(4L, node4);

        GraphEdge edge1 = new GraphEdge(1, 2, 8, 10, Collections.emptySet());
        GraphEdge edge2 = new GraphEdge(1, 3, 15, 10, Collections.emptySet());
        GraphEdge edge3 = new GraphEdge(2, 3, 4, 10, Collections.emptySet());
        GraphEdge edge4 = new GraphEdge(3, 4, 1, 10, Collections.emptySet());

        Map<EdgeId, GraphEdge> edges = new HashMap<>();
        edges.put(new EdgeId(1,2), edge1);
        edges.put(new EdgeId(1,3), edge2);
        edges.put(new EdgeId(2,3), edge3);
        edges.put(new EdgeId(3,4), edge4);


        Map<Long, List<GraphEdge>> outcoming = new HashMap<>();
        outcoming.put(1L, Arrays.asList(edge1, edge2));
        outcoming.put(2L, Arrays.asList(edge3));
        outcoming.put(3L, Arrays.asList(edge4));

        Map<Long, List<GraphEdge>> incoming = new HashMap<>();
        incoming.put(4L, Arrays.asList(edge4));
        incoming.put(3L, Arrays.asList(edge2, edge3));
        incoming.put(2L, Arrays.asList(edge1));

        Graph<GraphNode, GraphEdge> t = new Graph<>(nodes, edges, outcoming, incoming);
        return new RoadGraph(t);
    }
}
