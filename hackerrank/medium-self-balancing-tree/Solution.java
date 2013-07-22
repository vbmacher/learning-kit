   /* Class node is defined as :
    class Node 
       int val;   //Value
       int ht;      //Height
       Node left;   //Left child
       Node right;   //Right child

   */
    private static Node rotateLeft(Node root) {
        Node nodeR = root.right;

        root.right = nodeR.left;
        nodeR.left = root;

        root.ht = Math.max(height(root.left), height(root.right)) + 1;
        nodeR.ht = Math.max(height(nodeR.left), height(nodeR.right)) + 1;

        return nodeR;
    }

    private static Node rotateRight(Node root) {
        Node nodeL = root.left;

        root.left = nodeL.right;
        nodeL.right = root;

        root.ht = Math.max(height(root.left), height(root.right)) + 1;
        nodeL.ht = Math.max(height(nodeL.left), height(nodeL.right)) + 1;

        return nodeL;
    }

    private static Node rotateRightLeft(Node root) {
        Node nodeR = root.right;
        Node nodeRL = nodeR.left;

        root.right = nodeRL.left;
        nodeRL.left = root;

        nodeR.left = nodeRL.right;
        nodeRL.right = nodeR;

        root.ht = Math.max(height(root.left), height(root.right)) + 1;
        nodeR.ht = Math.max(height(nodeR.left), height(nodeR.right)) + 1;
        nodeRL.ht = Math.max(height(nodeRL.left), height(nodeRL.right)) + 1;

        return nodeRL;
    }

    private static Node rotateLeftRight(Node root) {
        Node nodeL = root.left;
        Node nodeLR = nodeL.right;

        nodeL.right = nodeLR.left;
        nodeLR.left = nodeL;

        root.left = nodeLR.right;
        nodeLR.right = root;

        root.ht = Math.max(height(root.left), height(root.right)) + 1;
        nodeL.ht = Math.max(height(nodeL.left), height(nodeL.right)) + 1;
        nodeLR.ht = Math.max(height(nodeLR.left), height(nodeLR.right)) + 1;

        return nodeLR;
    }


    static int height(Node root) {
        if (root == null) return -1;
        else return root.ht;
    }

    static Node insert(Node root, int val) {
        if (root == null) {
            root = new Node();
            root.val = val;
        } else if (root.val > val) {
            root.left = insert(root.left, val);
            root.ht = Math.max(height(root.left), height(root.right)) + 1;

            if (height(root.left) - height(root.right) == 2) {
                if (height(root.left.left) > height(root.left.right)) {
                    return rotateRight(root);
                } else {
                    return rotateLeftRight(root);
                }
            }
        } else if (root.val < val) {
            root.right = insert(root.right, val);
            root.ht = Math.max(height(root.left), height(root.right)) + 1;

            if (height(root.left) - height(root.right) == -2) {
                if (height(root.right.right) > height(root.right.left)) {
                    return rotateLeft(root);
                } else {
                    return rotateRightLeft(root);
                }
            }
        }
        return root;
    }
