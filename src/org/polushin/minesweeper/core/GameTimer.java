package org.polushin.minesweeper.core;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Таймер подсчета времени игры.
 */
public abstract class GameTimer {

	private final Timer timer = new Timer(true);

	private long begin;
	private HiddenTimer timerTask;
	private boolean run;

	/**
	 * @return Пройденое время в секундах.
	 */
	public int getTimePassed() {
		return begin == 0 ? 0 : (int) ((System.currentTimeMillis() - begin) / 1000);
	}

	/**
	 * @return Запущен ли сейчас таймер.
	 */
	public boolean isRun() {
		return run;
	}

	/**
	 * Вызывается каждую секунду для обновления текущего времени на экране.
	 *
	 * @param currTime Текущее время, прошедшее с начала игры.
	 */
	protected abstract void updateTimer(int currTime);

	/**
	 * Сбрасывает таймер.
	 */
	void resetTimer() {
		stopTimer();
		run = true;
		begin = System.currentTimeMillis();
		timer.schedule(timerTask = new HiddenTimer(), 0, 1000);
	}

	/**
	 * Останавливает таймер.
	 */
	void stopTimer() {
		if (timerTask != null)
			timerTask.cancel();
		run = false;
	}

	private class HiddenTimer extends TimerTask {

		@Override
		public void run() {
			updateTimer(getTimePassed());
		}
	}

}
