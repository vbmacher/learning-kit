package student;

import cz.cvut.atg.zui.astar.AbstractOpenList;
import cz.cvut.atg.zui.astar.Counter;
import cz.cvut.atg.zui.astar.PlannerExecutor;
import cz.cvut.atg.zui.astar.RoadGraph;
import eu.superhub.wp5.graphcommon.graph.Graph;
import eu.superhub.wp5.planner.planningstructure.GraphEdge;
import eu.superhub.wp5.planner.planningstructure.GraphNode;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class PlannerTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
//                { 1, 4, true }
                { 675864903, 244056540, false},
                {27044560, 308896030, false},
                {254633949, 1045079615, true},
                {965538689, 35412182, false},
                {283253281, 28837535, false},
                {21103346, 20957753, false},
                {560520134, 26607349, false},
                {25708383, 282428851, false},
                {170807, 25544320, false},
                {321574227, 7045042, false},
                {152601815, 25641108, true},
                {25915779, 570773, false},
                {375603805, 15723844, false},
                {368406501, 319105319, false},
                {368333265, 254020606, false},
                {306703880, 336563911, false},
                {25572272, 33583164, true},
                {1325483899, 26165062, false},
                {196587, 25594532, false},
                {272388545, 10820355, false},
                {161638545, 11151929, false},
                {263771569, 27185373, false},
                {434139048, 250118692, false},
                {20986093, 19828970, false},
                {1995935, 1073807795, false},
                {776278724, 195388640, false},
                {304061, 691392799, false},
                {27462844, 57047904, false},
                {311309613, 786470321, true},
                {11943738, 368114, false},
                {23188717, 1533017655, false},
                {1448490445, 350552122, false},
                {283202478, 1394193602, false},
                {393974, 21554358, false},
                {1357468007, 1061499, false},
                {304897206, 29638125, false},
                {26282534, 8078490, false},
                {621328, 21716508, false},
                {29057229, 355012644, false},
                {435098363, 15723843, false},
                {28981062, 803573895, false},
                {151370233, 1481420579, false},
                {3479844, 201295354, true},
                {15318414, 469955029, false},
                {12655810, 113556071, true},
                {352016, 45444285, false},
                {253196756, 26121586, false},
                {18233267, 243641508, false},
                {305165, 257868331, true},
                {194176609, 595662319, false},
                {534404006, 443160672, true},
                {6080833, 270202587, true},
                {447528523, 166330472, false},
                {34169675, 593856503, true},
                {59778003, 31943212, true},
                {9594892, 367659417, false},
                {386539, 365759961, true},
                {315296328, 690736808, true},
                {598173145, 29270131, false},
                {24940467, 458934494, false},
                {1112478161, 308896296, false},
                {283486393, 375603769, true},
                {10458710, 8255996, false},
                {243771180, 30388424, false},
                {1417032193, 34490266, false},
                {314442686, 29780331, false},
                {34486476, 1341407908, false},
                {582591094, 132459, false},
                {31175004, 15824859, false},
                {38953607, 292501, true},
                {266585894, 350462, true},
                {262209038, 309752764, false},
                {1067873801, 431459128, false},
                {255105012, 276780472, false},
                {25033464, 534077215, false},
                {373530267, 1010904656, true},
                {312504410, 666385, false},
                {248093329, 574193, false},
                {440970189, 701278, false},
                {534333350, 21692837, false},
                {33675134, 283508835, true},
                {266204139, 1152092881, false},
                {791893705, 385463, true},
                {253983378, 240088633, true},
                {204027, 20849072, true},
                {878732177, 385024, false},
                {14594160, 202001, false},
                {278959266, 30058811, false},
                {27199870, 1544154, false},
                {27456343, 21700339, false},
                {26935285, 338440521, false},
                {360787771, 25347063, false},
                {1358405644, 297652222, false},
                {322783, 58677736, false},
                {14594350, 12135850, false},
                {205230, 825308556, false},
                {297264932, 296222, false},
                {7888411, 447540620, false},
                {31890969, 298737731, false},
                {10031395, 249545985, false},
        });
    }

    private static final class PlanProperties {
        final double length;
        final double time;
        final int numberOfNodes;
        final List<GraphEdge> path;
        final int count;

        PlanProperties(double length, double time, int numberOfNodes, List<GraphEdge> path) {
            this.length = length;
            this.time = time;
            this.numberOfNodes = numberOfNodes;
            this.path = path;
            if (path == null) {
                count = 0;
            } else {
                count = path.size();
            }
        }

        @Override
        public String toString() {
            return "len=" + length +
                    ", time=" + time +
                    ", nodes=" + numberOfNodes +
                    ", count=" + count +
                    '}';
        }
    }


    private static RoadGraph roadGraph;

    private final Counter counter = Counter.getInstance();
    private final GraphNode origin;
    private final GraphNode destination;
    private final boolean shouldBeOK;

    public PlannerTest(int startNodeID, int goalNodeID, boolean shouldBeOK) {
        this.origin = roadGraph.getNodeByNodeId(startNodeID);
        this.destination = roadGraph.getNodeByNodeId(goalNodeID);
        this.shouldBeOK = shouldBeOK;
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        Graph<GraphNode, GraphEdge> newroadGraph = deserialize(PlannerExecutor.DATA_FILE);
        roadGraph = new RoadGraph(newroadGraph);
        System.out.println("Graph loaded.");

//        roadGraph = Graphs.domka1();
    }

    private static Graph<GraphNode, GraphEdge> deserialize(String filename) {
        try{
            //use buffering
            InputStream file = new FileInputStream(filename);
            InputStream buffer = new BufferedInputStream( file );
            ObjectInput input = new ObjectInputStream( buffer );
            try{
                //deserialize the graph
                Graph<GraphNode, GraphEdge> roadGraph = (Graph<GraphNode, GraphEdge>) input.readObject();
                return roadGraph;
            }
            finally{
                input.close();
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        //This shouldn't happen! Luckily, one does not simply reach this line.
        System.exit(1);
        return null;
    }

    private PlanProperties findPlanProperties(List<GraphEdge> plan, AbstractOpenList openList) {
        double planLength = 0;
        double planTime = 0;

        if (plan == null) {
            return new PlanProperties(0,0, openList.getCounter().getCount(), plan);
        }
        for(GraphEdge edge: plan){
            planLength+=edge.getLengthInMetres()/1000;
            planTime +=edge.getLengthInMetres()/1000.0/edge.getAllowedMaxSpeedInKmph();
        }

        return new PlanProperties(planLength, planTime, openList.getCounter().getCount(), plan);
    }


    @Test
    public void testNodes() throws Exception {
        counter.clearData();
        Planner planner = new Planner();
        PlanProperties testedProps = findPlanProperties(
                planner.plan(roadGraph, origin, destination), planner.getOpenList()
        );

        counter.clearData();
        BFS bfs = new BFS();
        PlanProperties bfsProps = findPlanProperties(
                bfs.plan(roadGraph, origin, destination), bfs.getOpenList()
        );

        checkOriginAndDestinationAreInPath(bfsProps);
        checkOriginAndDestinationAreInPath(testedProps);

        if (!shouldBeOK) {
            System.err.println("THIS TEST FAILED IN REPORT!");
        }

//        System.out.println("BFS: " + bfsProps.path);
//        System.out.println("TEST: " + testedProps.path);

        check(bfsProps, testedProps);

        System.out.println("BFS: " + bfsProps);
        System.out.println("TEST: " + testedProps);
    }

    private void check(PlanProperties etalonProps, PlanProperties testedProps) {
        if (etalonProps.path != null && testedProps.path != null) {
            checkContinuousPath(etalonProps);
            checkContinuousPath(testedProps);

        }
        assertTrue(
                "bfs[" + etalonProps + "] != tested[" + testedProps + "]",
                etalonProps.length == testedProps.length
        );
        assertTrue(
                "bfs[" + etalonProps + "] != tested[" + testedProps + "]"
                , etalonProps.time == testedProps.time
        );
        assertTrue(
                "bfs.count[" + etalonProps.count + "] != tested.count[" + testedProps.count + "]",
                etalonProps.count == testedProps.count
        );
    }

    private void checkContinuousPath(PlanProperties etalonProps) {
        GraphEdge previous = null;
        for (GraphEdge edge : etalonProps.path) {
            if (previous != null) {
                assertEquals("Path is not continuous!!", previous.getToNodeId(), edge.getFromNodeId());
            }
            previous = edge;
        }
    }

    private void checkOriginAndDestinationAreInPath(PlanProperties props) {
        if (props.path != null && !props.path.isEmpty()) {
            assertTrue(roadGraph.getNodeByNodeId(props.path.get(0).getFromNodeId()) == origin);
            assertTrue(roadGraph.getNodeByNodeId(props.path.get(props.path.size() - 1).getToNodeId()) == destination);
        }
    }

}