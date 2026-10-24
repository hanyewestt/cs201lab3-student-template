
import java.util.*;

public class Q1Test {

    public static void main(String[] args) {

        String result, traversal1, traversal2, traversal3;

        traversal1 = "1-2";
        traversal2 = "2-1-3";
        traversal3 = "1-3-2";
        result = verify(traversal1, traversal2, traversal3);
        System.out.println("Traversal 1 : " + traversal1);
        System.out.println("Traversal 2 : " + traversal2);
        System.out.println("Traversal 3 : " + traversal3);
        System.out.println("Expected Result : Invalid traversals");
        System.out.println("  Actual Result : " + result);
        System.out.println();

        traversal1 = "1-2-3";
        traversal2 = "2-1-3";
        traversal3 = "1-3-2";
        result = verify(traversal1, traversal2, traversal3);
        System.out.println("Traversal 1 : " + traversal1);
        System.out.println("Traversal 2 : " + traversal2);
        System.out.println("Traversal 3 : " + traversal3);
        System.out.println(
                "Expected Result : Traversal 1 - Inorder, Traversal 2 - Preorder, Traversal 3 - Postorder");
        System.out.println("  Actual Result : " + result);
        System.out.println();

        traversal1 = "1-2-3";
        traversal2 = "3-2-1";
        traversal3 = "2-3-1";
        result = verify(traversal1, traversal2, traversal3);
        System.out.println("Traversal 1 : " + traversal1);
        System.out.println("Traversal 2 : " + traversal2);
        System.out.println("Traversal 3 : " + traversal3);
        System.out.println("Expected Result : Invalid traversals");
        System.out.println("  Actual Result : " + result);
        System.out.println();

        traversal1 = "3-1-2-5-4";
        traversal2 = "2-1-4-5-3";
        traversal3 = "1-2-3-4-5";
        result = verify(traversal1, traversal2, traversal3);
        System.out.println("Traversal 1 : " + traversal1);
        System.out.println("Traversal 2 : " + traversal2);
        System.out.println("Traversal 3 : " + traversal3);
        System.out.println(
                "Expected Result : Traversal 1 - Preorder, Traversal 2 - Postorder, Traversal 3 - Inorder");
        System.out.println("  Actual Result : " + result);
        System.out.println();

        traversal1 = "10-20-30-40-50";
        traversal2 = "20-10-30-40-50";
        traversal3 = "10-50-40-30-20";
        result = verify(traversal1, traversal2, traversal3);
        System.out.println("Traversal 1 : " + traversal1);
        System.out.println("Traversal 2 : " + traversal2);
        System.out.println("Traversal 3 : " + traversal3);
        System.out.println(
                "Expected Result : Traversal 1 - Inorder, Traversal 2 - Preorder, Traversal 3 - Postorder");
        System.out.println("  Actual Result : " + result);
        System.out.println();
    }

    public static String verify(String t1, String t2, String t3) {

        if (t1.length() != t2.length() || t2.length() != t3.length()) {
            return "Invalid traversals";
        }

        List<Integer> t1list = parseTraversal(t1);
        List<Integer> t2list = parseTraversal(t2);
        List<Integer> t3list = parseTraversal(t3);

        List<List<Integer>> allTraversalLists = List.of(t1list, t2list, t3list);

        int inorderIdx;      // stores index of in order
        int preorderIdx = -1;      // stores index of pre order
        boolean isInOrderTraversal = false;
        for (inorderIdx = 0; inorderIdx < 3; inorderIdx++) {
            isInOrderTraversal = isInOrder(allTraversalLists.get(inorderIdx));
            if (isInOrderTraversal) {

                int offset = findPreOrderPotential(
                        allTraversalLists.get((inorderIdx + 1) % 3),
                        allTraversalLists.get((inorderIdx + 2) % 3));

                if (offset == 0) {
                    return "Invalid traversals";
                }
                preorderIdx = (inorderIdx + offset) % 3;
                break;
            }
        }

        // all 3 traversals checked and no in order found
        if (isInOrderTraversal == false) {
            return "Invalid traversals";
        }

        int postorderIdx = 3 - inorderIdx - preorderIdx;

        //    System.out.printf("inorder %d, preorder %d, postorder %d", inorderIdx, preorderIdx, postorderIdx);
        // make bst with pre order traversal
        BST tree = new BST();
        for (int e : allTraversalLists.get(preorderIdx)) {
            tree.insert(e);
        }

        if (tree.postorder().equals(allTraversalLists.get(postorderIdx))) {
            String[] traversalTypeArray = new String[3];

            traversalTypeArray[preorderIdx] = "Preorder";
            traversalTypeArray[inorderIdx] = "Inorder";
            traversalTypeArray[postorderIdx] = "Postorder";

            String result = String.format("Traversal 1 - %s, Traversal 2 - %s, Traversal 3 - %s",
                    traversalTypeArray[0],
                    traversalTypeArray[1],
                    traversalTypeArray[2]);
            return result;

        }

        // post order given does not match post order constructed from tree
        return "Invalid traversals";

    }

    private static List<Integer> parseTraversal(String t) {
        List<Integer> list;

        list = Arrays.stream(t.split("-")).map(Integer::parseInt).toList();

        return list;

    }

    private static boolean isInOrder(List<Integer> t) {

        int temp = t.get(0);
        for (int i = 1; i < t.size(); i++) {
            if (t.get(i) <= temp) {
                return false;
            }
            temp = t.get(i);
        }
        return true;
    }

    private static int findPreOrderPotential(List<Integer> t1, List<Integer> t2) {
        if (t1.get(0) == t2.get(t2.size() - 1)) {
            return 1;           // t1 is the preorder, offset 1 from inorder
        }
        if (t2.get(0) == t1.get(t2.size() - 1)) {
            return 2;           // t2 is the preorder
        }
        return 0;               // no preorder found, offset 2 from inorder
    }

}

class BST {

    private static class Node {

        Node left;
        Node right;
        int value;

        public Node(int value) {
            this.value = value;
        }

    }

    private Node root;

    public boolean insert(int value) {
        if (root == null) {
            root = new Node(value);
            return true;
        }

        Node curr = root;
        while (true) {
            if (value < curr.value) {
                if (curr.left == null) {
                    curr.left = new Node(value);
                    return true;
                }
                curr = curr.left;

            } else if (value > curr.value) {
                if (curr.right == null) {
                    curr.right = new Node(value);
                    return true;
                }
                curr = curr.right;
            } else {
                // dupe values
                return false;
            }
        }
    }

    public List<Integer> inorder() {
        List<Integer> result = new ArrayList<>();
        _inorder(root, result);
        return result;
    }

    private void _inorder(Node root, List<Integer> result) {

        if (root == null) {
            return;
        }
        _inorder(root.left, result);
        result.add(root.value);
        _inorder(root.right, result);
    }

    public List<Integer> postorder() {
        List<Integer> result = new ArrayList<>();
        _postorder(root, result);
        return result;
    }

    private void _postorder(Node root, List<Integer> result) {
        if (root == null) {
            return;
        }
        _postorder(root.left, result);
        _postorder(root.right, result);
        result.add(root.value);
    }

}
