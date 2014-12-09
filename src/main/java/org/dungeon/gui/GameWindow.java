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

import org.dungeon.game.Command;
import org.dungeon.game.Game;
import org.dungeon.game.GameData;
import org.dungeon.io.DLogger;
import org.dungeon.io.Loader;
import org.dungeon.utils.CommandHistory;
import org.dungeon.utils.Constants;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class GameWindow extends JFrame {

    private final SimpleAttributeSet attributeSet = new SimpleAttributeSet();
    private StyledDocument document;

    private JTextField textField;
    private JTextPane textPane;

    private int rows = Constants.ROWS;

    private boolean idle;

    public GameWindow() {
        initComponents();
        document = textPane.getStyledDocument();
        setVisible(true);
        setIdle(true);
    }

    private void initComponents() {
        setSystemLookAndFeel();

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

        Action save = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (idle) {
                    clearTextPane();
                    Loader.saveGame(Game.getGameState());
                }
            }
        };
        textField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK), "SAVE");
        textField.getActionMap().put("SAVE", save);

        URL icon = Thread.currentThread().getContextClassLoader().getResource("icon.png");
        if (icon != null) {
            setIconImage(new ImageIcon(icon).getImage());
        } else {
            DLogger.warning("Could not find the icon.");
        }

        setResizable(false);
        resize();
    }

    /**
     * Try to set the system's look and feel.
     *
     * If the system's default is GTK, the cross-platform L&F is used because GTK L&F does not let you change the
     * background coloring of a JTextField.
     */
    private void setSystemLookAndFeel() {
        try {
            String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
            if (lookAndFeel.equals("com.sun.java.swing.plaf.gtk.GTKLookAndFeel")) {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } else {
                UIManager.setLookAndFeel(lookAndFeel);
            }
        } catch (UnsupportedLookAndFeelException ignored) {
        } catch (ClassNotFoundException ignored) {
        } catch (InstantiationException ignored) {
        } catch (IllegalAccessException ignored) {
        }
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
            setIdle(false);
            Game.renderTurn(new Command(text));
            Game.getGameState().getCommandHistory().getCursor().moveToEnd();
            textField.requestFocusInWindow();
            setIdle(true);
        }
    }

    /**
     * Sets the idle state variable of this GameWindow. The player can only enter input or save the game when the window
     * is idle.
     *
     * @param idle the new idle state for this window.
     */
    private void setIdle(boolean idle) {
        this.idle = idle;
        textField.setEnabled(idle);
    }

    private void textFieldKeyPressed(KeyEvent e) {
        CommandHistory commandHistory = Game.getGameState().getCommandHistory();
        if (idle && commandHistory != null) {
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                textField.setText(commandHistory.getCursor().moveUp().getSelectedCommand());
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                textField.setText(commandHistory.getCursor().moveDown().getSelectedCommand());
            } else if (e.getKeyCode() == KeyEvent.VK_TAB) {
                String trimmedTextFieldText = getTrimmedTextFieldText();
                if (trimmedTextFieldText.isEmpty() && !commandHistory.isEmpty()) {
                    textField.setText(commandHistory.getCursor().moveToEnd().moveUp().getSelectedCommand());
                } else {
                    String lastSimilarCommand = commandHistory.getLastSimilarCommand(trimmedTextFieldText);
                    if (lastSimilarCommand != null) {
                        textField.setText(lastSimilarCommand);
                    }
                }
            }
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
