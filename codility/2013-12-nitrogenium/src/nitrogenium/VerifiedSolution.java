package nitrogenium;

import java.util.HashMap;
import java.util.Map;

public class VerifiedSolution {
  static class IntervalTree {
    Node root;
    Node NIL = Node.NIL;

    public IntervalTree() {
      this.root = NIL;
    }

    public void insert(int start, int end) {
      Node node = new Node(start, end);
      insert(node);
    }

    public int countOverlapping(int x) {
      if (root.isNull()) {
        return 0;
      }
      return searchAll(x, root);
    }

    private int searchAll(int x, Node node) {
      int count = 0;
      if (node.contains(x)) {
        count++;
      }
      if (!node.left.isNull() && node.left.max >= x) {
        count += searchAll(x, node.left);
      }
      if (!node.right.isNull() && node.right.min <= x) {
        count += searchAll(x, node.right);
      }
      return count;
    }

    private void insert(Node x) {
      treeInsert(x);
      x.color = Node.RED;
      while (x != this.root && x.parent.color == Node.RED) {
        if (x.parent == x.parent.parent.left) {
          Node y = x.parent.parent.right;
          if (y.color == Node.RED) {
            x.parent.color = Node.BLACK;
            y.color = Node.BLACK;
            x.parent.parent.color = Node.RED;
            x = x.parent.parent;
          } else {
            if (x == x.parent.right) {
              x = x.parent;
              this.leftRotate(x);
            }
            x.parent.color = Node.BLACK;
            x.parent.parent.color = Node.RED;
            this.rightRotate(x.parent.parent);
          }
        } else {
          Node y = x.parent.parent.left;
          if (y.color == Node.RED) {
            x.parent.color = Node.BLACK;
            y.color = Node.BLACK;
            x.parent.parent.color = Node.RED;
            x = x.parent.parent;
          } else {
            if (x == x.parent.left) {
              x = x.parent;
              this.rightRotate(x);
            }
            x.parent.color = Node.BLACK;
            x.parent.parent.color = Node.RED;
            this.leftRotate(x.parent.parent);
          }
        }
      }
      this.root.color = Node.BLACK;
    }

    private void leftRotate(Node x) {
      Node y = x.right;
      x.right = y.left;
      if (y.left != NIL) {
        y.left.parent = x;
      }
      y.parent = x.parent;
      if (x.parent == NIL) {
        this.root = y;
      } else {
        if (x.parent.left == x) {
          x.parent.left = y;
        } else {
          x.parent.right = y;
        }
      }
      y.left = x;
      x.parent = y;

      applyUpdate(x);
    }

    private void rightRotate(Node x) {
      Node y = x.left;
      x.left = y.right;
      if (y.right != NIL) {
        y.right.parent = x;
      }
      y.parent = x.parent;
      if (x.parent == NIL) {
        this.root = y;
      } else {
        if (x.parent.right == x) {
          x.parent.right = y;
        } else {
          x.parent.left = y;
        }
      }
      y.right = x;
      x.parent = y;

      applyUpdate(x);
    }

    private void treeInsert(Node x) {
      Node node = this.root;
      Node y = NIL;
      while (node != NIL) {
        y = node;
        if (x.start <= node.start) {
          node = node.left;
        } else {
          node = node.right;
        }
      }
      x.parent = y;

      if (y == NIL) {
        this.root = x;
        x.left = x.right = NIL;
      } else {
        if (x.start <= y.start) {
          y.left = x;
        } else {
          y.right = x;
        }
      }
      this.applyUpdate(x);
    }

    private void applyUpdate(Node node) {
      while (!node.isNull()) {
        this.update(node);
        node = node.parent;
      }
    }

    private void update(Node node) {
      node.max = Math.max(Math.max(node.left.max, node.right.max), node.end);
      node.min = Math.min(Math.min(node.left.min, node.right.min), node.start);
    }

    static class Node {
      public static final boolean BLACK = false;
      public static final boolean RED = true;

      int start;
      int end;

      int min;
      int max;
      Node left;
      Node right;

      boolean color;
      Node parent;

      private Node() {
        this.max = Integer.MIN_VALUE;
        this.min = Integer.MAX_VALUE;
      }

      public Node(int start, int end) {
        this();
        this.parent = NIL;
        this.left = NIL;
        this.right = NIL;
        this.start = start;
        this.end = end;
        this.color = RED;
      }

      static final Node NIL;

      static {
        NIL = new Node();
        NIL.color = BLACK;
        NIL.parent = NIL;
        NIL.left = NIL;
        NIL.right = NIL;
      }

      public boolean isNull() {
        return this == NIL;
      }

      public boolean contains(int x) {
        return (this.start <= x && x <= this.end);
      }
    }
  }

  private IntervalTree prepareIntervalTree(int[] A) {
    IntervalTree intervalTree = new IntervalTree();

    if (A[0] > 0) {
      intervalTree.insert(0, A[0] - 1); // O(log(N))
    }
    for (int i = 0; i < A.length - 1; i++) { // O(N)
//      if (A[i] < A[i+1]) {
//        intervalTree.insert(A[i], A[i+1] - 1); // O(log(N))
//      }
      // ------------- A TRY TO BETTER SOLUTION --------------------------------------------
      int item = A[i];
      if (item >= A[i+1]) {
        continue;
      }
      do {
        i++;
      } while ((i < A.length) && (A[i - 1] < A[i]));

      if (i >= A.length) {
        intervalTree.insert(item, A[A.length-1] - 1); // O(log(N))
        return intervalTree;
      }
      if (A[i-1] >= A[i]) {
        i--;
      }
      if (A[i-1] < A[i]) {
        intervalTree.insert(item, A[i] - 1); // O(log(N))
      }
    }
    return intervalTree;
  }

  public int[] solution(int[] A, int[] B) {
    int[] result = new int[B.length];

    IntervalTree intervalTree = prepareIntervalTree(A);
    if (intervalTree == null) {
      return result;
    }

    Map<Integer, Integer> cache = new HashMap<Integer, Integer>();
    for (int i = 0; i < B.length; i++) {
      Integer x = cache.get(B[i]); // O(1)
      if (x == null) {
        x = intervalTree.countOverlapping(B[i]); // O(log(N))
        cache.put(B[i], x); // O(1)
      }
      result[i] = x;
    }

    return result;
  }

}
