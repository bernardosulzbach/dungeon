package org.dungeon.gui;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Dungeon's custom ScrollBarUI that is a subclass of BasicScrollBarUI.
 * <p/>
 * Created by Bernardo Sulzbach on 12/12/14.
 */
public class DungeonScrollBarUI extends BasicScrollBarUI {

  private static final Dimension zeroDimension = new Dimension(0, 0);

  private static final int TRACK_W = 4;
  private static final int THUMB_W = TRACK_W * 2;

  @Override
  protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
    if (trackBounds.isEmpty() || !c.isEnabled()) {
      return;
    }
    // Fill the bar with the color of the text pane.
    g.setColor(SharedConstants.INSIDE_COLOR);
    g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
    // Fill the track with a darker color.
    g.setColor(SharedConstants.SCROLL_COLOR);
    int y = trackBounds.y + TRACK_W;
    int height = trackBounds.height - TRACK_W * 2;
    g.fillRect(calculateX(trackBounds.x, trackBounds.width, TRACK_W), y, TRACK_W, height);
  }

  @Override
  protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
    if (thumbBounds.isEmpty() || !c.isEnabled()) {
      return;
    }
    g.setColor(SharedConstants.MARGIN_COLOR);
    g.fillRect(calculateX(thumbBounds.x, thumbBounds.width, THUMB_W), thumbBounds.y, THUMB_W, thumbBounds.height);
  }

  @Override
  protected JButton createDecreaseButton(int orientation) {
    return createZeroButton();
  }

  @Override
  protected JButton createIncreaseButton(int orientation) {
    return createZeroButton();
  }

  /**
   * @param x         the leftmost X coordinate of the area.
   * @param areaWidth the width of the area.
   * @param barWidth  the width of the bar.
   * @return the leftmost X coordinate of a bar centered in a specified area.
   */
  private int calculateX(int x, int areaWidth, int barWidth) {
    return x + (areaWidth - barWidth) / 2;
  }

  /**
   * This is a temporary solution to remove the buttons from BasicScrollBarUI.
   *
   * @return a JButton with all sizes set to zero.
   */
  private static JButton createZeroButton() {
    JButton jButton = new JButton();
    jButton.setPreferredSize(zeroDimension);
    jButton.setMinimumSize(zeroDimension);
    jButton.setMaximumSize(zeroDimension);
    return jButton;
  }

}
