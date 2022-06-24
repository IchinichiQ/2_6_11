package ru.vsu.cs.p_p_v;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.parse.Parser;
import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.bridge.*;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.svg.SVGDocument;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SvgPanel extends JPanel {
    private String svg = null;
    private GraphicsNode svgGraphicsNode = null;

    public void paint(String svg) throws IOException {
        String xmlParser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory df = new SAXSVGDocumentFactory(xmlParser);
        SVGDocument doc = df.createSVGDocument(null, new StringReader(svg));
        UserAgent userAgent = new UserAgentAdapter();
        DocumentLoader loader = new DocumentLoader(userAgent);
        BridgeContext ctx = new BridgeContext(userAgent, loader);
        ctx.setDynamicState(BridgeContext.DYNAMIC);
        GVTBuilder builder = new GVTBuilder();
        svgGraphicsNode = builder.build(ctx, doc);

        this.svg = svg;
        repaint();
    }

    @Override
    public void paintComponent(Graphics gr) {
        super.paintComponent(gr);

        if (svgGraphicsNode == null) {
            return;
        }

        double scaleX = this.getWidth() / svgGraphicsNode.getPrimitiveBounds().getWidth();
        double scaleY = this.getHeight() / svgGraphicsNode.getPrimitiveBounds().getHeight();
        double scale = Math.min(scaleX, scaleY);
        AffineTransform transform = new AffineTransform(scale, 0, 0, scale, 0, 0);
        svgGraphicsNode.setTransform(transform);
        Graphics2D g2d = (Graphics2D) gr;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        svgGraphicsNode.paint(g2d);
    }
}
