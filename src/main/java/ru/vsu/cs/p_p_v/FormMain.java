package ru.vsu.cs.p_p_v;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.*;
import guru.nidi.graphviz.parse.Parser;

import javax.swing.*;
import java.util.HashSet;
import java.util.Scanner;

public class FormMain extends JFrame {
    private JPanel panelMain;
    private JTextArea textAreaSet;
    private JButton buttonDrawGraph;
    private JPanel panelGraph;
    private JButton buttonFindByIndex;
    private JSpinner spinnerIndex;

    SvgPanel panelGraphvizPainter = new SvgPanel();

    public FormMain() {
        this.setTitle("2_5_11");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panelMain);
        this.setSize(1000, 500);

        panelGraph.add(panelGraphvizPainter);
        panelGraph.repaint();

        buttonDrawGraph.addActionListener(e -> buttonDrawGraphPressed());
        buttonFindByIndex.addActionListener(e -> buttonFindByIndexPressed());
    }

    private void drawGraph(String graphvizStr) {
        try {
            MutableGraph g = new Parser().read(graphvizStr);
            panelGraphvizPainter.paint(Graphviz.fromGraph(g).render(Format.SVG).toString());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
        }
    }

    private void buttonDrawGraphPressed() {
        try {
            AVLTree<Integer> tree = new AVLTree<>();

            Scanner scan = new Scanner(textAreaSet.getText());
            while (scan.hasNextInt()) {
                tree.put(scan.nextInt());
            }

            drawGraph(tree.toGraphvizStrWithSubnodesCount());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
        }
    }

    private void buttonFindByIndexPressed() {
        try {
            AVLTreeSet<Integer> set = new AVLTreeSet<>();

            Scanner scan = new Scanner(textAreaSet.getText());
            while (scan.hasNextInt()) {
                set.add(scan.nextInt());
            }

            Integer elementWithIndex = set.get((Integer) spinnerIndex.getValue());

            if (elementWithIndex != null)
                JOptionPane.showMessageDialog(null, elementWithIndex);
            else
                JOptionPane.showMessageDialog(null, "Такого индекса не существует!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
        }
    }
}
