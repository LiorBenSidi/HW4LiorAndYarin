public class PathFromRoot {
    public static boolean doesPathExist(BinNode<Character> root, String str) {
        // TODO: Add your code for part A1 here...
        if(str.length() == 0){
            return true;
        }
        if (str.charAt(1) == root.getLeft().getData()) {
            doesPathExist(root.getLeft() ,str.substring(1));
        } else if (str.charAt(1) == root.getRight().getData()) {
            doesPathExist(root.getRight() ,str.substring(1));
        }
        return doesPathExist(root , str);
    }
}
