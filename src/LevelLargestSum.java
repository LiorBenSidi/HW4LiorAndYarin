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

        Queue<BinNode<Integer>> queue;
        queue = new ArrayDeque<>();
        queue.offer(root);
        int levelSum;
        int maxSum = root.getData();
        int maxLevel = 0;
        int currentLevel = 0;

        while (!queue.isEmpty()) {
            levelSum = 0;
            int levelSize = queue.size();

            for (int i = 0; i < levelSize; i++) {
                BinNode<Integer> node = queue.poll();
                if (node != null) {
                    levelSum = levelSum + node.getData();
                }

                BinNode<Integer> nodeLeft = node != null ? node.getLeft() : null;
                if (nodeLeft != null) {
                    queue.offer(nodeLeft);
                }

                BinNode<Integer> nodeRight = node != null ? node.getRight() : null;
                if (nodeRight != null) {
                    queue.offer(nodeRight);
                }
            }

            // If the current level sum is larger than previous,
            // so update the maximum sum and it's level
            if (levelSum > maxSum) {
                maxLevel = currentLevel;
                maxSum = levelSum;
            }

            currentLevel++;
        }

        return maxLevel;
    }
}
