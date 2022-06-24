package ru.vsu.cs.p_p_v;

/*
        (*) Реализовать дерево, поддерживающее произвольное кол-во потомков в узле.
    Реализовать для данного дерева процедуру "переворота" дерева справа налево.
    Подсказка: для каждого узла необходимо "перевернуть" список потомков
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
