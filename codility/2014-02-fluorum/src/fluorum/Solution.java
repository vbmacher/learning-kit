package fluorum;

import java.util.LinkedList;
import java.util.List;

/*
A country network consisting of N cities and N − 1 roads connecting them is given. Cities are labeled with distinct
integers within the range [0..(N − 1)]. Roads connect cities in such a way that each distinct pair of cities is connected
either by a direct road or through a path consisting of direct roads. There is exactly one way to reach any city from
any other city.

Starting out from city K, you have to plan a series of daily trips. Each day you want to visit a previously unvisited
city in such a way that, on a route to that city, you will also pass through a maximal number of other unvisited cities
(which will then be considered to have been visited). We say that the destination city is our daily travel target.

In the case of a tie, you should choose the city with the minimal label. The trips cease when every city has been
visited at least once.

For example, consider K = 2 and the following network consisting of seven cities and six roads:

You start in city 2. From here you make the following trips:
day 1 − from city 2 to city 0 (cities 1 and 0 become visited),
day 2 − from city 0 to city 6 (cities 4 and 6 become visited),
day 3 − from city 6 to city 3 (city 3 becomes visited),
day 4 − from city 3 to city 5 (city 5 becomes visited).
The goal is to find the sequence of travel targets. In the above example we have the following travel targets:
(2, 0, 6, 3, 5).

Write a function:

class Solution { public int[] solution(int K, int[] T); }

that, given a non-empty zero-indexed array T consisting of N integers describing a network of N cities and N − 1 roads,
returns the sequence of travel targets.

Array T describes a network of cities as follows:
if T[P] = Q and P ≠ Q, then there is a direct road between cities P and Q.
For example, given the following array T consisting of seven elements (this array describes the network shown above)
and K = 2:
    T[0] = 1
    T[1] = 2
    T[2] = 3
    T[3] = 3
    T[4] = 2
    T[5] = 1
    T[6] = 4
the function should return a sequence [2, 0, 6, 3, 5], as explained above.
Assume that:
N is an integer within the range [1..90,000];
each element of array T is an integer within the range [0..(N−1)];
there is exactly one (possibly indirect) connection between any two distinct roads.
Complexity:
expected worst-case time complexity is O(N);
expected worst-case space complexity is O(N), beyond input storage (not counting the storage required for input arguments).
Elements of input arrays can be modified.
*/

public class Solution {

    private class PathNode {
        private final List<PathNode> children = new LinkedList<PathNode>();
        private final int name;
        private int height;
        private int depth;
        private PathNode deepestChild;
        private PathNode parent;

        public PathNode(int name, int height, PathNode parent) {
            this.name = name;
            this.height = height;
            this.parent = parent;
        }

        public boolean isEmpty() {
            return children.isEmpty();
        }

        public void addChild(PathNode node) {
            children.add(node);
        }

        public void setDepth(int depth) {
            this.depth = depth;
        }

        public int getHeight() {
            return height;
        }

        public List<PathNode> getChildren() {
            return children;
        }

        private void removeParentsRecursively(PathNode current) {
            while (current != null) {
                // nasiel som vrchol pod ktorym su uz len nepouzite cesty
                // treba ho vymazat a jeho deti dat do parentskych deti
                PathNode grandParent = current.parent;
                if (grandParent != null) {
                    grandParent.children.remove(current);
                    grandParent.children.addAll(current.children);

                    // treba upgradnut parenta deti
                    for (PathNode child : current.children) {
                        child.parent = grandParent;
                        child.height--;
                    }
                }
                current = grandParent;
            }
        }

        public void removeFromParent() {
            if (parent != null) {
                parent.children.remove(this);

                PathNode current = parent;
                while (current != null && current.isEmpty()) {
                    if (current.parent != null) {
                        current.parent.children.remove(current);
                    }
                    current = current.parent;
                }
                removeParentsRecursively(current);
            }
        }

        @Override
        public String toString() {
            String repeated = new String(new char[height]).replace("\0", "  ");
            String deepestName = (deepestChild == null) ? "" : ",deepest=" + deepestChild.name;
            String parentName = (parent == null) ? ",root" : ",parent=" + parent.name;
            String msg = repeated + name + " (depth=" + depth + deepestName + parentName + ")\n";
            for (PathNode node : children) {
                msg += node;
            }
            return msg;
        }

        private void setDeepestChild(PathNode deepestChild) {
            this.deepestChild = deepestChild;
        }
    }

    /**
     * Find the articulation in the acyclic oriented graph.
     * Space complexity: 0
     * Time complexity: O(N)
     */
    private int findArticulation(int[] T) {
        for (int i = 0; i < T.length; i++) {
            if (i == T[i]) {
                return i;
            }
        }
        // we have a cycle here... shouldn't happen
        return -1;
    }

    /**
     * Prepares a list of neighbours.
     * Space complexity is O(N+M). This list contains N elements in total in all lists in the whole array.
     * Time complexity is O(N).
     */
    private List<Integer>[] prepareNeighbours(int[] T) {
        List<Integer>[] neighbours = new List[T.length];

        for (int i = 0; i < T.length; i++) {
            neighbours[i] = new LinkedList();
        }

        int articulation = findArticulation(T);
        for (int i = 0; i < T.length; i++) {
            int item = T[i];
            if (item == articulation && (i != item)) {
                neighbours[i].add(articulation);
            } else if (item != articulation) {
                neighbours[item].add(i);
            }
        }
        return neighbours;
    }

    /**
     * Prepare tree of all continuous paths, starting in K.
     * Space complexity: O(N), based on neighbours array content
     * Time complexity: O(N), based on neighbours array content
     */
    private PathNode prepareTree(List<Integer>[] neighbours, int K, int currentHeight, PathNode parent) {
        PathNode root = new PathNode(K, currentHeight, parent);
        for (Integer node : neighbours[K]) {
            root.addChild(prepareTree(neighbours, node, currentHeight + 1, root));
        }
        return root;
    }

    class DepthAndNode {
        public final int depth;
        public final PathNode node;

        public DepthAndNode(int depth, PathNode node) {
            this.depth = depth;
            this.node = node;
        }

    }

    /**
     * Assign depths to the tree.
     * Space complexity: 0
     * Time complexity: O(N)
     */
    private DepthAndNode assignDepths(PathNode root) {
        if (root.getChildren().isEmpty()) {
            root.setDepth(0);
            root.setDeepestChild(null);
            return new DepthAndNode(0, null);
        }
        int maxDepth = 0;
        PathNode deepestChild = null;

        for (PathNode child : root.getChildren()) {
            DepthAndNode depthAndNode = assignDepths(child);
            int depth = depthAndNode.depth + 1;
            child.setDepth(depth);
            child.setDeepestChild(depthAndNode.node);

            if (deepestChild == null) {
                if (depthAndNode.node != null) {
                    deepestChild = depthAndNode.node;
                } else {
                    deepestChild = child;
                }
            }
            if (maxDepth < depth) {
                maxDepth = depth;
                if (deepestChild.depth < depth) {
                    deepestChild = depthAndNode.node;
                }
            }
        }
        root.setDepth(maxDepth);
        root.setDeepestChild(deepestChild);
        return new DepthAndNode(maxDepth, deepestChild);
    }

    private int getAndDeleteDeepest(PathNode tree) {
        if (tree.deepestChild == null) {
            tree.removeFromParent();
            return tree.name;
        }
        PathNode deepest = tree.deepestChild;
        deepest.removeFromParent();
        return deepest.name;
    }

    public int[] solution(int K, int[]T) {
        List[] neighbours = prepareNeighbours(T);
        PathNode tree = prepareTree(neighbours, K, 0, null);
        assignDepths(tree);
      //  System.out.println(tree);

        List<Integer> result = new LinkedList<Integer>();
        result.add(K);
        while (!tree.isEmpty()) {
            int next = getAndDeleteDeepest(tree);
            result.add(next);
//            System.out.println(next);
            assignDepths(tree);
  //          System.out.println(tree);
        }
        Integer[] integerResult = result.toArray(new Integer[0]);
        int[] intResult = new int[integerResult.length];

        for (int index = 0; index < integerResult.length; index++) {
            intResult[index] = integerResult[index];
        }

        return intResult;
    }

}
