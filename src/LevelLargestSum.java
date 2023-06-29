import java.util.ArrayDeque;
import java.util.Queue;

/**
 * A class that have a method for finding the first level with the largest sum in a binary tree
 */
public class LevelLargestSum {
    /**
     * Finds the level with the largest sum in a binary tree
     * If the tree is empty, so returns -1
     *
     * @param root the provided root node of a binary tree
     * @return the level that have the largest sum
     */
    public static int getLevelWithLargestSum(BinNode<Integer> root) {
        if (root == null) { // If the root is null, so returns -1
            return -1;
        }

        int maxLevel = 0; // Initialize a variable that represents the level with the largest sum
        int maxSum = root.getData(); // The sum of the root's level that can be changed later
        int currentLevel = 0; // A counter of the current level of the tree

        Queue<BinNode<Integer>> queue = new ArrayDeque<>(); // Initialize an new ArrayDeque
        queue.offer(root);

        // The level order traversal
        while (!queue.isEmpty()) {
            int levelSum = 0;
            int levelSize = queue.size();

            for (int i = 0; i < levelSize; i++) {
                BinNode<Integer> node = queue.poll(); // Removes a node from the queue
                if (node != null) {
                    levelSum += node.getData(); // Adds the data from the current node to the level sum
                }

                if (node != null && node.getLeft() != null) {
                    queue.offer(node.getLeft()); // If the left child is not null, so Adds it to the queue
                }
                if (node != null && node.getRight() != null) {
                    queue.offer(node.getRight()); // If the right child is not null, so Adds it to the queue
                }
            }

            // If the current level sum is larger than previous,
            // so update the maximum sum and it's level
            if (levelSum > maxSum) {
                maxSum = levelSum;
                maxLevel = currentLevel;
            }

            currentLevel++;
        }

        return maxLevel;
    }
}
