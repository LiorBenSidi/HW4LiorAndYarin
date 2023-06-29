/**
 * A class that have static method for checking if there is a path in the provided binary tree,
 * from the root, that creates a string that equals to the given string
 */
public class PathFromRoot {
    /**
     * checking if there is a path in the provided binary tree,
     * from the root, that creates a string that equals to the given string
     *
     * @param root the provided root node of a binary tree
     * @param str the provided string
     * @return true if the path is existing, else - false
     */
    public static boolean doesPathExist(BinNode<Character> root, String str) {
        if (root == null) { // If the root is null, the path is obviously does not exist.
            return false;
        }

        if (str.length() == 0) { // If the string is empty, the path is obviously exists.
            return true;
        }

        // If the string length is equal to one,
        // checks if the remaining character is matching to the data from the current node.
        if (str.length() == 1) {
            return str.charAt(0) == root.getData();
        }

        // if the data from the current node is matching the first character in the provided string,
        // so, use recursion for checking the left and right child nodes data with the remaining characters
        if (root.getData() == str.charAt(0)) {
            return doesPathExist(root.getLeft(), str.substring(1))
                    || doesPathExist(root.getRight(), str.substring(1));
        }

        // if the data from the current node is not matching the first character in the string,
        // so the path for the provided string does not exist.
        return false;
    }
}
