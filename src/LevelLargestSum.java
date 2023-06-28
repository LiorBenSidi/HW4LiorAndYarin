import java.util.ArrayDeque;
import java.util.Queue;

public class LevelLargestSum {
    public static int getLevelWithLargestSum(BinNode<Integer> root) {
        // TODO: Add your code for part A2 here...
        // Check if the root is null, and return -1
        if (root == null) {
            return -1;
        }

        int maxLevel = 0; // minimum level
        int maxSum = root.getData(); // sum of minimum level
        int currentLevel = 0; // counter of levels

        // Create a queue to perform level order traversal
        Queue<BinNode<Integer>> queue = new ArrayDeque<>();
        queue.offer(root);

        // Perform level order traversal
        while (!queue.isEmpty()) {
            int levelSum = 0;
            int levelSize = queue.size();

            for (int i = 0; i < levelSize; i++) {
                // Remove a node from the queue
                BinNode<Integer> node = queue.poll();
                // Add the node's data to the level sum
                levelSum += node.getData();

                // Add the left child to the queue if it is not null
                if (node.getLeft() != null) {
                    queue.offer(node.getLeft());
                }
                // Add the right child to the queue if it is not null
                if (node.getRight() != null) {
                    queue.offer(node.getRight());
                }
            }

            // Update the maximum sum and level if the current level sum is bigger
            if (levelSum > maxSum) {
                maxSum = levelSum;
                maxLevel = currentLevel;
            }

            currentLevel++;
        }

        // Return the level with the largest sum
        return maxLevel;


        /*
        int sum;
        int level;
        if(root == null){
            return -1;
        }
        if(root.getLeft() == null && root.getRight() == null){
            return root.getData();
        } else if (root.getLeft() != null && root.getRight() != null) {
            sum = getLevelWithLargestSum(root.getLeft()) + getLevelWithLargestSum(root.getRight());

        }
         */
    }
}
