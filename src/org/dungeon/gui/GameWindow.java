/* 
 * Copyright (C) 2014 Bernardo Sulzbach
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.dungeon.gui;

import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import org.dungeon.core.game.Game;
import org.dungeon.utils.Constants;
import org.dungeon.utils.Utils;

public class GameWindow extends javax.swing.JFrame {

    // Window configuration. Note that this is fixed in stone so that the text formatting makes sense.
    public static final int WIDTH = 830;
    // With "Monospaced 14", this height allows for 24 rows of output.
    public static final int HEIGHT = 640;

    // java.awt.Color.ORANGE is not orange enough.
    public static final Color ORANGE = new Color(255, 127, 0);

    // GUI components.
    private JScrollPane scrollPane;
    private JScrollBar scrollBar;
    private JTextField textField;
    private JTextPane textPane;

    private final StyledDocument document;
    private final SimpleAttributeSet attributeSet = new SimpleAttributeSet();

    public GameWindow() {
        initComponents();
        document = textPane.getStyledDocument();
        setVisible(true);
    }

    private void initComponents() {

        textPane = new javax.swing.JTextPane();
        scrollPane = new javax.swing.JScrollPane();
        textField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(Constants.FULLNAME);
        setResizable(false);

        textPane.setEditable(false);
        textPane.setBackground(Color.DARK_GRAY);
        textPane.setForeground(Color.LIGHT_GRAY);
        textPane.setSelectionColor(ORANGE);
        textPane.setFont(new java.awt.Font("Monospaced", Font.PLAIN, 14));

        scrollPane.setViewportView(textPane);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        scrollBar = scrollPane.getVerticalScrollBar();
        scrollBar.setBackground(Color.DARK_GRAY);
        scrollBar.addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                Adjustable adjustable = e.getAdjustable();
                adjustable.setValue(adjustable.getMaximum());
            }
        });

        getContentPane().add(scrollPane, BorderLayout.CENTER);

        textField.setBackground(Color.DARK_GRAY);
        textField.setForeground(Color.LIGHT_GRAY);
        textField.setSelectionColor(ORANGE);
        textField.setFont(new java.awt.Font("Monospaced", Font.PLAIN, 14));
        textField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
                textFieldActionPerformed();
            }
        });
        getContentPane().add(textField, BorderLayout.SOUTH);

        setSize(new java.awt.Dimension(WIDTH, HEIGHT));
        setLocationRelativeTo(null);
    }

    private void textFieldActionPerformed() {
        String text = textField.getText().trim();
        if (!text.isEmpty()) {
            if (!Game.renderTurn(text)) {
                System.exit(0);
            }
        }
        textField.setText(null);
    }

    public void writeToTextPane(String str, Color color) {
        StyleConstants.setForeground(attributeSet, color);
        try {
            int length = document.getLength();
            if (length != 0) {
                document.insertString(document.getLength(), "\n" + str, attributeSet);
            } else {
                document.insertString(document.getLength(), str, attributeSet);
            }
            scrollBar = scrollPane.getVerticalScrollBar();
            scrollBar.setValue(scrollBar.getMaximum());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void clearTextPane() {
        try {
            document.remove(0, document.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void requestFocusOnTextField() {
        textField.requestFocusInWindow();
    }
}
