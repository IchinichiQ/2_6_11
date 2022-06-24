package ru.vsu.cs.p_p_v;

/**
 * Рализация AVL-дерева:
 * http://qaru.site/questions/1973343/avl-tree-rotation-in-java
 *
 * @param <T>
 */
public class AVLTree<T extends Comparable<? super T>> implements BSTree<T> {

    // не указываем модификаторы доступа, чтобы был доступ из того же пакета
    // (в частности из AVLTreeNode и AVLTreePainter)
    class AVLTreeNode implements BinaryTree.TreeNode<T> {

        public T value;
        public AVLTreeNode left;
        public AVLTreeNode right;
        public int height;
        public int subnodesCount;

        public AVLTreeNode(T value, AVLTreeNode left, AVLTreeNode right, int height, int subnodesCount) {
            this.value = value;
            this.left = left;
            this.right = right;
            this.height = height;
            this.subnodesCount = subnodesCount;
        }

        public AVLTreeNode(T value) {
            this(value, null, null, 0, 0);
        }

        public void recalcHeight() {
            height = Math.max((left == null ? -1 : left.height), (right == null ? -1 : right.height)) + 1;
        }

        public int getHeightDiff() {
            return (left == null ? -1 : left.height) - (right == null ? -1 : right.height);
        }

        public void recalcSubnodes() {
            subnodesCount = (left == null ? 0 : left.subnodesCount + 1) + (right == null ? 0 : right.subnodesCount + 1);
        }

        public int getSubnodesCount() {
            return subnodesCount;
        }

        // Ниже идет реализация интерфейса BinaryTree.TreeNode<T>

        @Override
        public T getValue() {
            return value;
        }

        @Override
        public BinaryTree.TreeNode<T> getLeft() {
            return left;
        }

        @Override
        public BinaryTree.TreeNode<T> getRight() {
            return right;
        }
    }

    AVLTreeNode root = null;
    private int size = 0;

    /**
     * Рекурсивное добавление значения в поддерево node
     *
     * @param node  Узел, в который (в него или его поддеревья) добавляем
     *              значение value
     * @param value Добавляемое значение
     * @return Узел, который должен оказаться на месте node
     */
    private AVLTreeNode put(AVLTreeNode node, T value) {
        if (node == null) {
            size++;
            return new AVLTreeNode(value);
        }
        int cmp = value.compareTo(node.value);
        if (cmp == 0) {
            // в узле значение, равное value
            node.value = value;
        } else {
            if (cmp < 0) {
                node.left = put(node.left, value);
            } else {
                node.right = put(node.right, value);
            }
            node.recalcHeight();
            node.recalcSubnodes();
            // балансировка
            node = balance(node);
        }
        return node;
    }

    private AVLTreeNode remove(AVLTreeNode node, T value) {
        if (node == null) {
            return null;
        }
        int cmp = value.compareTo(node.value);
        if (cmp == 0) {
            // в узле значение, равное value
            if (node.left != null && node.right != null) {
                node.value = BSTreeAlgorithms.getMinNode(node.right).getValue();
                node.right = remove(node.right, node.value);
            } else {
                node = (node.left != null) ? node.left : node.right;
                size--;
            }
        } else if (cmp < 0)
            node.left = remove(node.left, value);
        else {
            node.right = remove(node.right, value);
        }
        return balance(node);
    }

    public TreeNode<T> getNodeByIndex(int index) {
        if (index >= size || index < 0)
            return null;

        class Inner {
            AVLTreeNode getNodeByIndex(AVLTreeNode node, int indexToFind, int accumulatedIndex) {
                if (node == null)
                    return null;

                AVLTreeNode left = node.left;
                AVLTreeNode right = node.right;

                int currentIndex;
                if (left != null)
                    currentIndex = accumulatedIndex + left.getSubnodesCount() + 1;
                else
                    currentIndex = accumulatedIndex + node.getSubnodesCount() - right.getSubnodesCount() - 1;

                if (currentIndex == indexToFind)
                    return node;

                if (left != null) {
                    if (left.getSubnodesCount() == 0) {
                        // Если поддерево лист, то можно сразу вычислить его индекс
                        if (currentIndex - 1 == indexToFind)
                            return left;
                    } else {
                        AVLTreeNode temp = getNodeByIndex(left, indexToFind, accumulatedIndex);
                        if (temp != null)
                            return temp;
                    }
                }

                if (right != null) {
                    if (right.getSubnodesCount() == 0) {
                        if (currentIndex + 1 == indexToFind)
                            return right;
                    } else {
                        AVLTreeNode temp = getNodeByIndex(right, indexToFind, accumulatedIndex + currentIndex + 1);
                        if (temp != null)
                            return temp;
                    }
                }

                return null;
            }
        }

        return new Inner().getNodeByIndex(root, index, 0);
    }

    private AVLTreeNode balance(AVLTreeNode node) {
        if (node == null) {
            return null;
        }
        if (node.getHeightDiff() < -1) {
            // высота правого поддерева для node больше левого более, чем на 1 (на 2)

            if (node.right != null && node.right.getHeightDiff() > 0) {
                // двойной право-левый поворот (RL-rotation)
                node.right = rightRotate(node.right);
                node = leftRotate(node);
            } else {
                // левый поворот (L-rotation)
                node = leftRotate(node);
            }
        } else if (node.getHeightDiff() > 1) {
            // высота левого поддерева для node больше правого более, чем на 1 (на 2)

            if (node.left != null && node.left.getHeightDiff() < 0) {
                // двойной лево-правый поворот (LR-rotation)
                node.left = leftRotate(node.left);
                node = rightRotate(node);
            } else {
                // правый поворот (R-rotation)
                node = rightRotate(node);
            }
        }
        return node;
    }

    private AVLTreeNode leftRotate(AVLTreeNode node) {
        AVLTreeNode right = node.right;
        node.right = right.left;
        right.left = node;
        node.recalcHeight();
        right.recalcHeight();
        node.recalcSubnodes();
        right.recalcSubnodes();
        return right;
    }

    private AVLTreeNode rightRotate(AVLTreeNode node) {
        AVLTreeNode left = node.left;
        node.left = left.right;
        left.right = node;
        node.recalcHeight();
        left.recalcHeight();
        node.recalcSubnodes();
        left.recalcSubnodes();
        return left;
    }

    public String toGraphvizStrWithSubnodesCount() {
        StringBuilder sb = new StringBuilder();

        sb.append("graph {\r\n");
        BinaryTreeAlgorithms.preOrderVisit(root, (node) -> {
            AVLTreeNode AVLNode = (AVLTreeNode) node;
            AVLTreeNode left = (AVLTreeNode) AVLNode.getLeft();
            AVLTreeNode right = (AVLTreeNode) AVLNode.getRight();

            if (left != null)
                addNodeWithSubnodesCount(sb, AVLNode, left);
            if (right != null)
                addNodeWithSubnodesCount(sb, AVLNode, right);
        });
        sb.append("}");

        return sb.toString();
    }

    private void addNodeWithSubnodesCount(StringBuilder sb, AVLTreeNode from, AVLTreeNode to) {
        sb.append('"');
        sb.append(from.getValue());
        sb.append(" (");
        sb.append(from.getSubnodesCount());
        sb.append(')');
        sb.append('"');

        sb.append(" -- ");

        sb.append('"');
        sb.append(to.getValue());
        sb.append(" (");
        sb.append(to.getSubnodesCount());
        sb.append(')');
        sb.append('"');
        sb.append("\r\n");
    }

    // Ниже идет реализация интерфейса BSTree<T> (@Override-методы)

    @Override
    public TreeNode<T> getRoot() {
        return root;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T put(T value) {
        // крайне неэффективная реализация, т.к. поиск по дереву иде 2 раза (надо переписать)
        T oldValue = this.get(value);
        this.root = put(root, value);
        return oldValue;
    }

    @Override
    public T remove(T value) {
        // крайне неэффективная реализация, т.к. поиск по дереву иде 2 раза (надо переписать)
        T oldValue = this.get(value);
        this.root = remove(root, value);
        return oldValue;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }
}
