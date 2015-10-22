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

package org.mafagafogigante.dungeon.gui;

import org.mafagafogigante.dungeon.commands.CommandHistory;
import org.mafagafogigante.dungeon.commands.IssuedCommand;
import org.mafagafogigante.dungeon.game.ColoredString;
import org.mafagafogigante.dungeon.game.Game;
import org.mafagafogigante.dungeon.game.GameState;
import org.mafagafogigante.dungeon.game.Writable;
import org.mafagafogigante.dungeon.io.Loader;
import org.mafagafogigante.dungeon.logging.DungeonLogger;

import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
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
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

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
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class GameWindow extends JFrame {

  /**
   * Returns how many text rows are shown in the Window.
   */
  public static final int ROWS = 30;
  public static final int COLS = 100;
  private static final int FONT_SIZE = 15;
  private static final Font FONT = getMonospacedFont();
  private static final String WINDOW_TITLE = "Dungeon";

  /**
   * The border, in pixels.
   */
  private static final int MARGIN = 5;
  private final SimpleAttributeSet attributeSet = new SimpleAttributeSet();
  private final StyledDocument document;
  private JTextField textField;
  private JTextPane textPane;

  private boolean acceptingNextCommand;

  /**
   * Constructs a new GameWindow.
   */
  public GameWindow() {
    initComponents();
    document = textPane.getStyledDocument();
    setVisible(true);
  }

  /**
   * Returns the monospaced font used by the game interface.
   */
  private static Font getMonospacedFont() {
    Font font = new Font(Font.MONOSPACED, Font.PLAIN, FONT_SIZE);
    InputStream fontStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("DroidSansMono.ttf");
    try {
      font = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(Font.PLAIN, FONT_SIZE);
    } catch (FontFormatException bad) {
      DungeonLogger.warning("threw FontFormatException during font creation.");
    } catch (IOException bad) {
      DungeonLogger.warning("threw IOException during font creation.");
    } finally {
      if (fontStream != null) {
        try {
          fontStream.close();
        } catch (IOException ignore) {
          // An IO error occurred. Not much left to do.
        }
      }
    }
    return font;
  }

  /**
   * Try to set the system's look and feel.
   *
   * <p>If the system's default is GTK, the cross-platform L&F is used because GTK L&F does not let you change the
   * background coloring of a JTextField.
   */
  private static void setSystemLookAndFeel() {
    try {
      String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
      if (lookAndFeel.equals("com.sun.java.swing.plaf.gtk.GTKLookAndFeel")) {
        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
      } else {
        UIManager.setLookAndFeel(lookAndFeel);
      }
    } catch (UnsupportedLookAndFeelException ignored) {
      // Nothing can be done about this.
    } catch (ClassNotFoundException ignored) {
      // Nothing can be done about this.
    } catch (InstantiationException ignored) {
      // Nothing can be done about this.
    } catch (IllegalAccessException ignored) {
      // Nothing can be done about this.
    }
  }

  private static void logExecutionExceptionAndExit(ExecutionException fatal) {
    DungeonLogger.logSevere(fatal);
    System.exit(1);
  }

  private void initComponents() {
    setSystemLookAndFeel();

    JPanel panel = new JPanel(new GridBagLayout());
    panel.setBackground(SharedConstants.MARGIN_COLOR);

    textPane = new JTextPane();
    textPane.setEditable(false);
    textPane.setBackground(SharedConstants.INSIDE_COLOR);
    textPane.setFont(FONT);

    JScrollPane scrollPane = new JScrollPane();
    scrollPane.setViewportView(textPane);
    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    scrollPane.setBorder(BorderFactory.createEmptyBorder());
    scrollPane.getVerticalScrollBar().setBackground(SharedConstants.INSIDE_COLOR);
    scrollPane.getVerticalScrollBar().setUI(new DungeonScrollBarUi());

    textField = new JTextField();
    textField.setBackground(SharedConstants.INSIDE_COLOR);
    textField.setForeground(Color.LIGHT_GRAY);
    textField.setCaretColor(Color.WHITE);
    textField.setFont(FONT);
    textField.setFocusTraversalKeysEnabled(false);
    textField.setBorder(BorderFactory.createEmptyBorder());

    textField.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
        textFieldActionPerformed();
      }
    });

    textField.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent event) {
        textFieldKeyPressed(event);
      }
    });

    GridBagConstraints constants = new GridBagConstraints();
    constants.insets = new Insets(MARGIN, MARGIN, MARGIN, MARGIN);
    panel.add(scrollPane, constants);

    constants.gridy = 1;
    constants.fill = GridBagConstraints.HORIZONTAL;
    constants.insets = new Insets(0, MARGIN, MARGIN, MARGIN);
    panel.add(textField, constants);

    setTitle(WINDOW_TITLE);

    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent event) {
        super.windowClosing(event);
        Game.exit();
      }
    });

    Action save = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent event) {
        if (acceptingNextCommand) {
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
    FontMetrics fontMetrics = getFontMetrics(FONT);
    int width = fontMetrics.charWidth(' ') * (COLS + 1); // columns + magic constant
    int height = fontMetrics.getHeight() * ROWS;
    return new Dimension(width, height);
  }

  /**
   * The method that gets called when the player presses ENTER.
   */
  private void textFieldActionPerformed() {
    if (acceptingNextCommand) {
      final String text = getTrimmedTextFieldText();
      if (!text.isEmpty()) {
        clearTextField();
        acceptingNextCommand = false;
        SwingWorker<Void, Void> inputRenderer = new SwingWorker<Void, Void>() {
          @Override
          protected Void doInBackground() {
            Game.renderTurn(new IssuedCommand(text));
            return null;
          }

          @Override
          protected void done() {
            // This method is invoked on the EDT after doInBackground finishes.
            // Only by calling get() we can get any exceptions that might have been thrown during doInBackground().
            // The default behaviour is to log the exception and exit the game with code 1.
            try {
              get();
            } catch (InterruptedException ignore) {
              // For some reason the thread was interrupted. Nothing should be done.
            } catch (ExecutionException fatal) {
              logExecutionExceptionAndExit(fatal);
            }
            acceptingNextCommand = true;
          }
        };
        inputRenderer.execute();
      }
    }
  }

  /**
   * Handles a key press in the text field. This method checks for a command history access by the keys UP, DOWN, or TAB
   * and, if this is the case, processes this query.
   *
   * @param event the KeyEvent.
   */
  private void textFieldKeyPressed(KeyEvent event) {
    int keyCode = event.getKeyCode();
    if (isUpDownOrTab(keyCode)) { // Check if the event is of interest.
      GameState gameState = Game.getGameState();
      if (gameState != null) {
        CommandHistory commandHistory = gameState.getCommandHistory();
        if (keyCode == KeyEvent.VK_UP) {
          textField.setText(commandHistory.getCursor().moveUp().getSelectedCommand());
        } else if (keyCode == KeyEvent.VK_DOWN) {
          textField.setText(commandHistory.getCursor().moveDown().getSelectedCommand());
        } else if (keyCode == KeyEvent.VK_TAB) {
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

  private boolean isUpDownOrTab(int keyCode) {
    return keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_TAB;
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
   * Schedules the writing of the contents of a Writable with the provided specifications on the Event Dispatch Thread.
   * This method can be called on any thread.
   *
   * @param writable a Writable object
   * @param specifications a WritingSpecifications object
   */
  public void scheduleWriteToTextPane(@NotNull final Writable writable,
      @NotNull final WritingSpecifications specifications) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        writeToTextPane(writable, specifications);
      }
    });
  }

  /**
   * Effectively updates the text pane. Should only be invoked on the Event Dispatch Thread.
   *
   * @param writable a Writable object, not empty
   * @param specifications a WritingSpecifications object
   */
  private void writeToTextPane(Writable writable, WritingSpecifications specifications) {
    for (ColoredString coloredString : writable.toColoredStringList()) {
      StyleConstants.setForeground(attributeSet, coloredString.getColor());
      try {
        document.insertString(document.getLength(), coloredString.getString(), attributeSet);
      } catch (BadLocationException warn) {
        DungeonLogger.warning("insertString resulted in a BadLocationException.");
      }
    }
    textPane.setCaretPosition(specifications.shouldScrollDown() ? document.getLength() : 0);
  }

  /**
   * Clears the TextPane by erasing everything in the local Document.
   *
   * <p>This schedules the operation to be ran on the EDT, so it is safe to invoke this on any thread.
   */
  public void clearTextPane() {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        try {
          document.remove(0, document.getLength());
        } catch (BadLocationException ignored) { // Never happens.
        }
      }
    });
  }

  /**
   * Schedules a focus request on the text field.
   */
  public void requestFocusOnTextField() {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        textField.requestFocusInWindow();
      }
    });
  }

  private void clearTextField() {
    textField.setText(null);
  }

  /**
   * Signalizes to this window that it should start accepting commands.
   *
   * <p>This must be done after the first GameState is loaded. Other changes of GameState do not need to be protected
   * this way because the SwingWorker toggles the acceptingNextCommand variable to false and just changes it back to
   * true after it is finished (and the GameState is loaded).
   */
  public void startAcceptingCommands() {
    acceptingNextCommand = true;
  }

}
