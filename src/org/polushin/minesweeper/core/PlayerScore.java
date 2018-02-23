package org.polushin.minesweeper.core;

/**
 * Счет игрока при победе в игре.
 */
public class PlayerScore {
	public final String nick;
	public final int square, time;

	/**
	 * @param nick Ник игрока.
	 * @param square Площадь поля игры.
	 * @param time Время (в секундах) разрешения игры.
	 */
	PlayerScore(String nick, int square, int time) {
		this.nick = nick;
		this.square = square;
		this.time = time;
	}

	@Override
	public String toString() {
		return String.format("%s -- Square: %d, Time: %dsec", nick, square, time);
	}
}
