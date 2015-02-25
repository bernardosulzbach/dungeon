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

import org.dungeon.game.Game;
import org.dungeon.game.GameData;
import org.dungeon.game.GameState;
import org.dungeon.game.IssuedCommand;
import org.dungeon.io.Loader;
import org.dungeon.util.CommandHistory;
import org.dungeon.util.Constants;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameWindow extends JFrame {

  /**
   * Returns how many text rows are shown in the Window.
   */
  public static final int ROWS = 30;
  // The border, in pixels.
  private static final int MARGIN = 5;
  private final SimpleAttributeSet attributeSet = new SimpleAttributeSet();
  private final StyledDocument document;
  private JTextField textField;
  private JTextPane textPane;
  private boolean idle;

  public GameWindow() {
    initComponents();
    document = textPane.getStyledDocument();
    setVisible(true);
    setIdle(true);
  }

  private void initComponents() {
    setSystemLookAndFeel();

    JPanel panel = new JPanel(new GridBagLayout());
    panel.setBackground(SharedConstants.MARGIN_COLOR);

    textPane = new JTextPane();
    textField = new JTextField();

    JScrollPane scrollPane = new JScrollPane();

    textPane.setEditable(false);
    textPane.setBackground(SharedConstants.INSIDE_COLOR);
    textPane.setFont(GameData.FONT);

    scrollPane.setViewportView(textPane);
    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    scrollPane.setBorder(BorderFactory.createEmptyBorder());
    scrollPane.getVerticalScrollBar().setUI(new DScrollBarUI());

    textField.setBackground(SharedConstants.INSIDE_COLOR);
    textField.setForeground(Constants.FORE_COLOR_NORMAL);
    textField.setCaretColor(Color.WHITE);
    textField.setFont(GameData.FONT);
    textField.setFocusTraversalKeysEnabled(false);
    textField.setBorder(BorderFactory.createEmptyBorder());

    textField.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
        textFieldActionPerformed();
      }
    });

    textField.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        textFieldKeyPressed(e);
      }
    });

    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(MARGIN, MARGIN, MARGIN, MARGIN);
    panel.add(scrollPane, c);

    c.gridy = 1;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.insets = new Insets(0, MARGIN, MARGIN, MARGIN);
    panel.add(textField, c);

    setTitle(Constants.NAME);

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

    add(panel);

    setResizable(false);
    resize();
  }

  /**
   * Try to set the system's look and feel.
   * <p/>
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
    FontMetrics fontMetrics = getFontMetrics(GameData.FONT);
    int width = fontMetrics.charWidth(' ') * (Constants.COLS + 1); // columns + magic constant
    int height = fontMetrics.getHeight() * ROWS;
    return new Dimension(width, height);
  }

  /**
   * The method that gets called when the player presses ENTER.
   */
  private void textFieldActionPerformed() {
    final String text = getTrimmedTextFieldText();
    if (!text.isEmpty()) {
      clearTextField();
      setIdle(false);
      SwingWorker inputRenderer = new SwingWorker<Void, Void>() {
        @Override
        protected Void doInBackground() {
          Game.renderTurn(new IssuedCommand(text));
          return null;
        }

        @Override
        protected void done() {
          textField.requestFocusInWindow();
          setIdle(true);
        }
      };
      inputRenderer.execute();
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

  /**
   * Handles a key press in the text field.
   *
   * @param e the KeyEvent.
   */
  private void textFieldKeyPressed(KeyEvent e) {
    if (idle) {
      GameState gameState = Game.getGameState();
      if (gameState != null) {
        CommandHistory commandHistory = gameState.getCommandHistory();
        if (e.getKeyCode() == KeyEvent.VK_UP) {
          textField.setText(commandHistory.getCursor().moveUp().getSelectedCommand());
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
          textField.setText(commandHistory.getCursor().moveDown().getSelectedCommand());
        } else if (e.getKeyCode() == KeyEvent.VK_TAB) {
          // Using the empty String to get the last similar command will always retrieve the last command.
          // Therefore, there is no need to check if there is something in the text field.
          String lastSimilarCommand = commandHistory.getLastSimilarCommand(getTrimmedTextFieldText());
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
   * @param string     the string to be written.
   * @param color      the Color of the foreground text.
   * @param scrollDown if true, the TextPane will be scrolled down after writing.
   */
  public void writeToTextPane(String string, Color color, boolean scrollDown) {
    writeToTextPane(string, color, textPane.getBackground(), scrollDown);
  }

  /**
   * Adds a string with a specific foreground and background color to the text pane.
   *
   * @param string     the string to be written.
   * @param fore       the Color of the foreground text.
   * @param back       the Color of the background text.
   * @param scrollDown if true, the TextPane will be scrolled down after writing.
   */
  void writeToTextPane(String string, Color fore, Color back, boolean scrollDown) {
    StyleConstants.setForeground(attributeSet, fore);
    StyleConstants.setBackground(attributeSet, back);
    try {
      document.insertString(document.getLength(), string, attributeSet);
      if (scrollDown) {
        textPane.setCaretPosition(document.getLength());
      } else {
        textPane.setCaretPosition(0);
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
