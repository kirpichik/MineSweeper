package org.polushin.minesweeper.gui;

import org.polushin.minesweeper.core.field.Cell;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

/**
 * Кнопка клетки на игровом поле.
 */
public class CellButton extends JButton {

	public static final int SIDE_SIZE = 25;
	private static final Font TEXT_FONT = new Font("Arial", Font.BOLD, 16);
	private static final Border RAISED_BORDER = BorderFactory.createRaisedBevelBorder();
	private static final Border LOWERED_BORDER = BorderFactory.createLoweredBevelBorder();
	private static final Border EMPTY_BORDER = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
	private static Icon FLAG_ICON;
	private static Icon MINE_ICON;

	static {
		try {
			FLAG_ICON = new ImageIcon(ImageIO.read(CellButton.class.getResource("resources/flag.png"))
					                          .getScaledInstance(SIDE_SIZE, SIDE_SIZE, Image.SCALE_DEFAULT));
			MINE_ICON = new ImageIcon(ImageIO.read(CellButton.class.getResource("resources/mine.png"))
					                          .getScaledInstance(SIDE_SIZE, SIDE_SIZE, Image.SCALE_DEFAULT));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private final int posX, posY;
	private final GameFieldFrame field;

	public CellButton(GameFieldFrame field, int x, int y) {
		if (FLAG_ICON == null || MINE_ICON == null)
			throw new RuntimeException("Images not loaded!");
		posX = x;
		posY = y;
		this.field = field;
		addMouseListener(new InteractListener());
		setBorder(RAISED_BORDER);
	}

	/**
	 * Обновляет вид кнопки в соответствии с сотоянием клетки.
	 *
	 * @param cell Клетка.
	 */
	void updateState(Cell cell) {
		if (cell.isOpened()) {
			if (cell.isMine()) {
				setBackground(cell.isFlagSet() ? Color.GREEN : Color.YELLOW);
				setIcon(MINE_ICON);
			} else if (cell.getMineNeighbors() > 0) {
				setIcon(null);
				setText(String.valueOf(cell.getMineNeighbors()));
				setFont(TEXT_FONT);
				setForeground(getNumberColor(cell.getMineNeighbors()));
			}
			setBorder(EMPTY_BORDER);
		} else if (cell.isFlagSet() && getIcon() != FLAG_ICON)
			setIcon(FLAG_ICON);
		else if (!cell.isFlagSet() && getIcon() == FLAG_ICON)
			setIcon(null);

		if (cell.isFlagsOverflow())
			setBackground(Color.MAGENTA);
		else if (!cell.isMine())
			setBackground(Color.WHITE);
	}

	/**
	 * Цвет цифры на клетке.
	 */
	private Color getNumberColor(int number) {
		switch (number) {
			case 1:
				return new Color(11, 36, 251);
			case 2:
				return new Color(15, 127, 18);
			case 3:
				return new Color(252, 13, 27);
			case 4:
				return new Color(21, 1, 77);
			case 5:
				return new Color(63, 12, 24);
			case 6:
				return new Color(121, 210, 193);
			case 7:
				return Color.BLACK;
			case 8:
				return Color.GRAY;
			default:
				return null;
		}
	}

	/**
	 * Слушатель нажатий на клетку.
	 */
	private final class InteractListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {

		}

		// Вынесено из-за плохой стандартной обработки нажаний мышью.
		private void mouseClick(MouseEvent e) {
			if (getBorder() == EMPTY_BORDER)
				return;

			if (e.getButton() == MouseEvent.BUTTON1 && getIcon() != FLAG_ICON)
				field.openCell(posX, posY);
			else if (e.getButton() == MouseEvent.BUTTON3)
				field.flagCell(posX, posY);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1 && getBorder() != EMPTY_BORDER && getIcon() != FLAG_ICON)
				setBorder(LOWERED_BORDER);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1 && getBorder() != EMPTY_BORDER)
				setBorder(RAISED_BORDER);
			if (e.getX() <= getWidth() && e.getY() <= getHeight())
				mouseClick(e);
		}

		@Override
		public void mouseEntered(MouseEvent e) {

		}

		@Override
		public void mouseExited(MouseEvent e) {

		}
	}

}
