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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;

class DungeonScrollBarUI extends BasicScrollBarUI {

  private static final Dimension ZERO_DIMENSION = new Dimension(0, 0);
  private static final int THUMB_MARGIN = 4;
  private static final int THUMB_WIDTH = 4;

  /**
   * This is a temporary solution to remove the buttons from BasicScrollBarUI.
   *
   * @return a JButton with all sizes set to zero
   */
  private static JButton createZeroButton() {
    JButton jButton = new JButton();
    jButton.setPreferredSize(ZERO_DIMENSION);
    jButton.setMinimumSize(ZERO_DIMENSION);
    jButton.setMaximumSize(ZERO_DIMENSION);
    return jButton;
  }

  @Override
  protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
  }

  @Override
  protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
    if (thumbBounds.isEmpty() || !c.isEnabled()) {
      return;
    }
    g.setColor(SharedConstants.MARGIN_COLOR);
    int x = thumbBounds.width - THUMB_WIDTH - THUMB_MARGIN;
    int y = thumbBounds.y + THUMB_MARGIN;
    int height = thumbBounds.height - THUMB_MARGIN * 2;
    g.fillRect(x, y, THUMB_WIDTH, height);
  }

  @Override
  protected JButton createDecreaseButton(int orientation) {
    return createZeroButton();
  }

  @Override
  protected JButton createIncreaseButton(int orientation) {
    return createZeroButton();
  }

}
