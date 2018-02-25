package org.polushin.minesweeper.gui;

import org.polushin.minesweeper.core.GameHandler;
import org.polushin.minesweeper.core.field.Cell;
import org.polushin.minesweeper.core.field.InteractResult;

import javax.swing.*;
import java.awt.*;

/**
 * Рамка игрового поля.
 */
public class GameFieldFrame extends JPanel {

	private static final Font STATE_FONT = new Font("Arial", Font.BOLD, 40);

	private final GameHandler game;
	private final GameStatsDisplay stats;

	private CellButton[][] buttons;
	private State gameState;

	/**
	 * @param game Основной объект игры.
	 */
	public GameFieldFrame(GameHandler game, GameStatsDisplay statsDisplay) {
		this.game = game;
		this.stats = statsDisplay;
		statsDisplay.resetGame(game.getMinesCount());
		statsDisplay.setNick(game.getNick());
		initNewSize(game.getWidth(), game.getHeight());
	}

	/**
	 * Перезапускает игру и пересоздает поле.
	 *
	 * @param mines Кол-во мин.
	 * @param width Ширина поля.
	 * @param height Высота поля.
	 */
	public void restartGame(int mines, int width, int height) {
		game.restartGame(mines, width, height);
		stats.resetGame(mines);
		initNewSize(width, height);
	}

	/**
	 * Устанавливает новый ник игрока.
	 *
	 * @param nick Новый ник.
	 */
	public void setNick(String nick) {
		game.setNick(nick);
		stats.setNick(nick);
	}

	/**
	 * Перезапускает игру и пересоздает поле.
	 *
	 * @param mines Кол-во мин.
	 * @param width Ширина поля.
	 * @param height Высота поля.
	 * @param seed Зерно генерации поля.
	 */
	public void restartGame(int mines, int width, int height, long seed) {
		game.restartGame(mines, width, height, seed);
		initNewSize(width, height);
	}

	private void initNewSize(int width, int height) {
		removeAll();
		gameState = State.NONE;
		setSize(height * CellButton.SIDE_SIZE, width * CellButton.SIDE_SIZE);
		buttons = new CellButton[width][height];
		setLayout(new GridLayout(width, height));

		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				add(buttons[i][j] = new CellButton(this, i, j));
		validate();
		repaint();
	}

	/**
	 * Открывает клетку и обновляет все кнопки в соответствии с результатом.
	 */
	void openCell(int x, int y) {
		if (game.isGameOver() || game.isGameWon())
			return;
		InteractResult result = game.openCell(x, y);
		update(result);
		if (result.isExplode()) {
			gameState = State.LOSE;
			buttons[x][y].setBackground(Color.RED);
		}

		if (game.isGameWon()) {
			gameState = State.WIN;
			repaint();
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (gameState == State.NONE)
			return;

		setFont(STATE_FONT);

		if (gameState == State.WIN) {
			g.setColor(Color.GREEN);
			drawCenter(g, "YOU WON!");
		} else if (gameState == State.LOSE) {
			g.setColor(Color.RED);
			drawCenter(g, "YOU LOSE!");
		}
	}

	/**
	 * Рисует надпись по центру экрана и обрамляет ее фоном.
	 */
	private void drawCenter(Graphics g, String text) {
		FontMetrics metrics = g.getFontMetrics(STATE_FONT);
		int textX = (getWidth() - metrics.stringWidth(text)) / 2;
		int textY = (getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();
		int rectX = textX - 10;
		int rectY = textY - metrics.getHeight() + 10;
		int rectWidth = metrics.stringWidth(text) + 20;
		int rectHeight = metrics.getHeight();

		Color textColor = g.getColor();
		g.setColor(getBackground());
		g.fillRect(rectX, rectY, rectWidth, rectHeight);

		g.setColor(textColor);
		g.drawString(text, textX, textY);
	}

	/**
	 * Отмечает флагом клетку.
	 */
	void flagCell(int x, int y) {
		if (game.isGameWon() || game.isGameOver())
			return;

		int flags = game.getFlagsCount();
		update(game.markCell(x, y));

		if (flags > game.getFlagsCount())
			stats.removeFlag();
		else if (flags < game.getFlagsCount())
			stats.setFlag();

		if (game.isGameWon()) {
			gameState = State.WIN;
			repaint();
		}
	}

	/**
	 * Обновляет клетки по итератору.
	 */
	private void update(InteractResult result) {
		for (Cell cell : result)
			buttons[cell.posX][cell.posY].updateState(cell);

		// FIXME - Некоторые клетки не перерисовываются на поле, требуется вызывать принудительную отрисовку.
		repaint();
	}

	/**
	 * Состояние игры.
	 */
	private enum State {
		NONE,
		WIN,
		LOSE
	}

}
