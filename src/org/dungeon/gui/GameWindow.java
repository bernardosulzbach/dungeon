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

import org.dungeon.core.game.Game;
import org.dungeon.utils.Constants;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

public class GameWindow extends JFrame {

    // Window configuration. Note that this is fixed in stone so that the text formatting makes sense.
    private static final int WIDTH = 830;
    // With "Monospaced 14", this height allows for 24 rows of output.
    private static final int HEIGHT = 640;

    // java.awt.Color.ORANGE is not orange enough.
    private static final Color ORANGE = new Color(255, 127, 0);
    private final StyledDocument document;
    private final SimpleAttributeSet attributeSet = new SimpleAttributeSet();
    private JTextField textField;
    private JTextPane textPane;

    public GameWindow() {
        initComponents();
        document = textPane.getStyledDocument();
        setVisible(true);
    }

    private void initComponents() {

        textPane = new javax.swing.JTextPane();
        textField = new javax.swing.JTextField();

        JScrollPane scrollPane = new JScrollPane();
        JScrollBar scrollBar = scrollPane.getVerticalScrollBar();

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

        scrollBar.setBackground(Color.DARK_GRAY);

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

    // The method that gets called when the player presses ENTER.
    private void textFieldActionPerformed() {
        String text = textField.getText().trim();
        if (!text.isEmpty()) {
            if (!Game.renderTurn(text)) {
                System.exit(0);
            }
        }
        textField.setText(null);
    }

    public void writeToTextPane(String str, Color color, boolean endLine) {
        StyleConstants.setForeground(attributeSet, color);
        try {
            if (endLine) {
                document.insertString(document.getLength(), str + '\n', attributeSet);
            } else {
                document.insertString(document.getLength(), str, attributeSet);
            }
            textPane.setCaretPosition(document.getLength());
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
