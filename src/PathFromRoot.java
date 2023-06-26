public class PathFromRoot {
    public static boolean doesPathExist(BinNode<Character> root, String str) {
        // TODO: Add your code for part A1 here...
        boolean flag = false;
        if (str.length() == 0) {
            return true;
        }
        if(str.length() == 1){
            return str.charAt(0) == root.getData();
        }
        if(root.getData() == str.charAt(0)) {
            if (str.charAt(1) == root.getLeft().getData()) {
                flag = doesPathExist(root.getLeft(), str.substring(1));
            } else if (str.charAt(1) == root.getRight().getData()) {
               flag = doesPathExist(root.getRight(), str.substring(1));
            }
        }
        return flag;
    }
}
