package org.polushin.minesweeper.gui;

import org.polushin.minesweeper.core.GameTimer;

import javax.swing.*;
import java.awt.*;

/**
 * Статистика текущей игры.
 */
public class GameStatsDisplay extends Label implements Runnable {

	private static final Font FONT = new Font("Arial", Font.BOLD, 16);

	private final GUITimer timer;

	private int mines;
	private int flags;
	private String nick;

	private volatile int currentTime;

	public GameStatsDisplay() {
		timer = new GUITimer();
		setFont(FONT);
	}

	/**
	 * Сбрасывает состояние игры.
	 *
	 * @param newMines Новое кол-во мин.
	 */
	public void resetGame(int newMines) {
		mines = newMines;
		flags = 0;
		synchronized (timer) {
			currentTime = 0;
		}
		updateDisplay();
	}

	/**
	 * Устанавливает ник игрока.
	 *
	 * @param nick Новый ник.
	 */
	public void setNick(String nick) {
		this.nick = nick;
		updateDisplay();
	}

	/**
	 * Устанавливает флаг.
	 */
	public void setFlag() {
		flags++;
		updateDisplay();
	}

	/**
	 * Удаляет флаг.
	 */
	public void removeFlag() {
		if (flags != 0)
			flags--;
		updateDisplay();
	}

	/**
	 * @return Асинхронный таймер для посчета времени.
	 */
	public GUITimer getTimer() {
		return timer;
	}

	/**
	 * @return Максимально возможную длину строки.
	 */
	public int getMaxWidth() {
		return getFontMetrics(getFont()).stringWidth(formString(mines, 1000));
	}

	/**
	 * Обновляет строку статистики на экране.
	 */
	private void updateDisplay() {
		int time;
		synchronized (timer) {
			time = currentTime;
		}
		String text = formString(flags, time);
		setText(text);

		FontMetrics metrics = getFontMetrics(getFont());
		setSize(metrics.stringWidth(text), metrics.getHeight());
	}

	/**
	 * Формирует строку с изменяемыми параметрами.
	 */
	private String formString(int flags, int time) {
		return String.format("%s: %d/%d, %d sec.", nick, flags, mines, time);
	}

	@Override
	public void run() {
		updateDisplay();
	}

	private class GUITimer extends GameTimer {

		@Override
		protected void updateTimer(int currTime) {
			synchronized (this) {
				currentTime = currTime;
			}
			SwingUtilities.invokeLater(GameStatsDisplay.this);
		}
	}
}
