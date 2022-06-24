package ru.vsu.cs.p_p_v;

import java.util.*;

public class AVLTreeSet<T extends Comparable<? super T>> extends AbstractSet<T> implements Set<T> {
    private static final Object PRESENT = new Object();

    AVLTree<T> tree;

    public AVLTreeSet(){
        tree = new AVLTree<>();
    }

    @Override
    public int size() {
        return tree.size();
    }

    @Override
    public boolean isEmpty() {
        return tree.size() == 0;
    }

    @Override
    public boolean contains(Object o) {
        return tree.contains((T) o);
    }

    @Override
    public Iterator<T> iterator() {
        return tree.iterator();
    }

    @Override
    public boolean add(T t) {
        return tree.put(t) == null;
    }

    public T get(int index) {
        return tree.getNodeByIndex(index).getValue();
    }

    @Override
    public boolean remove(Object o) {
        return tree.remove((T) o) == PRESENT;
    }

    @Override
    public void clear() {
        tree.clear();
    }
}
