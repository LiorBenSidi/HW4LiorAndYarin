public class BinNode<E> {
    private E data;
    private BinNode<E> left;
    private BinNode<E> right;

    public BinNode(E data, BinNode<E> left, BinNode<E> right) {
        this.data = data;
        this.left = left;
        this.right = right;
    }

    public BinNode(E data) {
        this(data, null, null);
    }

    public E getData() {
        return data;
    }

    public BinNode<E> getLeft() {
        return left;
    }

    public BinNode<E> getRight() {
        return right;
    }

    public void setData(E data) {
        this.data = data;
    }

    public void setLeft(BinNode<E> left) {
        this.left = left;
    }

    public void setRight(BinNode<E> right) {
        this.right = right;
    }
}