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
import org.dungeon.core.game.GameData;
import org.dungeon.utils.CommandHistory;
import org.dungeon.utils.Constants;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameWindow extends JFrame {

    private final SimpleAttributeSet attributeSet = new SimpleAttributeSet();
    private CommandHistory commandHistory;
    private int commandIndex;
    private StyledDocument document;

    private JTextField textField;
    private JTextPane textPane;

    private int rows = Constants.ROWS;

    public GameWindow() {
        initComponents();
        document = textPane.getStyledDocument();
        setVisible(true);
    }

    private void initComponents() {
        textPane = new javax.swing.JTextPane();
        textField = new javax.swing.JTextField();

        JScrollPane scrollPane = new JScrollPane();

        textPane.setEditable(false);
        textPane.setBackground(Constants.DEFAULT_BACK_COLOR);
        textPane.setFont(GameData.monospaced);

        scrollPane.setViewportView(textPane);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        textField.setBackground(Color.BLACK);
        textField.setForeground(Constants.FORE_COLOR_NORMAL);
        textField.setCaretColor(Color.WHITE);
        textField.setFont(GameData.monospaced);
        textField.setFocusTraversalKeysEnabled(false);

        textField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
                textFieldActionPerformed();
            }
        });

        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                textFieldKeyPressed(e);
            }
        });

        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(textField, BorderLayout.SOUTH);

        setTitle(Constants.TITLE);

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                Game.exit();
            }
        });

        setResizable(false);
        resize();
    }

    /**
     * Resizes and centers the frame.
     */
    private void resize() {
        textPane.setPreferredSize(calculateTextPaneSize());
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Evaluates the preferred size for the TextPane.
     *
     * @return a Dimension with the preferred TextPane dimensions.
     */
    private Dimension calculateTextPaneSize() {
        FontMetrics fontMetrics = getFontMetrics(GameData.monospaced);
        int width = fontMetrics.charWidth(' ') * (Constants.COLS + 1); // columns + magic constant
        int height = fontMetrics.getHeight() * rows;
        return new Dimension(width, height);
    }

    /**
     * Changes the number of rows displayed in the TextPane and resizes the frame. You should verify that the argument
     * is reasonable as this method does not verify if the row count is too big.
     *
     * @param rows the new number of rows.
     * @return a boolean indicating if the number of rows was altered or not.
     */
    public boolean setRows(int rows) {
        if (rows < 0) {
            throw new IllegalArgumentException("rows should be positive.");
        }
        if (rows != this.rows) {
            this.rows = rows;
            resize();
            return true;
        }
        return false;
    }

    // The method that gets called when the player presses ENTER.
    private void textFieldActionPerformed() {
        String text = getTrimmedTextFieldText();
        if (!text.isEmpty()) {
            clearTextField();
            textField.setEnabled(false);
            Game.renderTurn(text);
            resetCommandIndex();
            textField.requestFocusInWindow();
            textField.setEnabled(true);
        }
    }

    private void textFieldKeyPressed(KeyEvent e) {
        if (commandHistory == null) {
            commandHistory = Game.getGameState().getCommandHistory();
            resetCommandIndex();
        }
        boolean validKeyPress = false;
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            if (commandIndex > 0) {
                validKeyPress = true;
                commandIndex--;
            }
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (commandIndex < commandHistory.getCommandCount()) {
                commandIndex++;
                // When the index is set to one past the last saved command, just clear the text field.
                if (commandIndex == commandHistory.getCommandCount()) {
                    clearTextField();
                    return;
                }
                validKeyPress = true;
            }
        } else if (e.getKeyCode() == KeyEvent.VK_TAB) {
            String trimmedTextFieldText = getTrimmedTextFieldText();
            if (trimmedTextFieldText.isEmpty() && !commandHistory.isEmpty()) {
                textField.setText(commandHistory.getLastCommand());
            } else {
                String lastSimilarCommand = commandHistory.getLastSimilarCommand(trimmedTextFieldText);
                if (lastSimilarCommand != null) {
                    textField.setText(lastSimilarCommand);
                }
            }
        }
        if (validKeyPress) {
            textField.setText(commandHistory.getCommandAt(commandIndex));
        }
    }

    /**
     * Convenience method that returns the text in the text field after trimming it.
     *
     * @return a trimmed String.
     */
    private String getTrimmedTextFieldText() {
        return textField.getText().trim();
    }

    private void resetCommandIndex() {
        // This is correct.
        // The index should point to one after the last command, so the up key retrieves the last command.
        commandIndex = commandHistory.getCommandCount();
    }

    /**
     * Adds a string with a specific foreground color to the text pane.
     *
     * @param string the string to be written.
     * @param color  the color of the text.
     */
    public void writeToTextPane(String string, Color color, long wait) {
        writeToTextPane(string, color, textPane.getBackground(), wait);
    }

    /**
     * Adds a string with a specific foreground and background color to the text pane.
     *
     * @param string the string to be written.
     * @param fore   the color of the text.
     */
    void writeToTextPane(String string, Color fore, Color back, long wait) {
        StyleConstants.setForeground(attributeSet, fore);
        StyleConstants.setBackground(attributeSet, back);
        if (Game.getGameState() != null) {
            StyleConstants.setBold(attributeSet, Game.getGameState().isBold());
        }
        try {
            document.insertString(document.getLength(), string, attributeSet);
            try {
                if (wait > 0) {
                    textPane.update(textPane.getGraphics());
                    Thread.sleep(wait);
                }
            } catch (InterruptedException ignored) {
            }
        } catch (BadLocationException ignored) {
        }
    }

    public void clearTextPane() {
        try {
            document.remove(0, document.getLength());
        } catch (BadLocationException ignored) {
        }
    }

    public void requestFocusOnTextField() {
        textField.requestFocusInWindow();
    }

    private void clearTextField() {
        textField.setText(null);
    }

}
