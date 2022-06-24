package ru.vsu.cs.p_p_v;

/*
        (*) Реализовать такое множество (Set<T>), для которого можно быстро, за время
    O(log(n)), находить K-ый по порядку элемент множества. Для этого необходимо
    разработать бинарное дерево поиска (можно модифицировать какой-то из классов
    SimpleRBTree / AVLTree / RBTree из проекта примеров к лекциям) такое, чтобы в
    каждом узле дерева дополнительно хранить информацию о количестве элементов в
    поддереве с вершиной в данном узле. Естественно, операции вставки и удаления
    элементов должны остаться O(log(n)). Далее на основе такого дерева необходимо
    реализовать множество, которое позволяет быстро найти K-ый по счету элемент.
 */

import java.io.IOException;
import java.util.Locale;

public class Main {
    public static void main(String[] args) throws IOException {
        Locale.setDefault(Locale.ROOT);

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FormMain().setVisible(true);
            }
        });
    }
}
