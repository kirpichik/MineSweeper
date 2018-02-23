package org.polushin.minesweeper.core;

import java.util.Random;

/**
 * Генератор полностью случайного поля.
 */
public class FullRandomField extends MineField {

	private final Random random;

	/**
	 * @param width Ширина поля.
	 * @param height Высота поля.
	 */
	public FullRandomField(int width, int height) {
		super(width, height);
		random = new Random();
	}

	/**
	 * @param width Ширина поля.
	 * @param height Высота поля.
	 * @param seed Зерно для генерации поля.
	 */
	public FullRandomField(int width, int height, long seed) {
		super(width, height);
		random = new Random(seed);
	}

	@Override
	protected void generate(int startX, int startY) {
		for (int i = 0; i < field.length; i++)
			for (int j = 0; j < field[i].length; j++)
				if (i != startX && j != startY && random.nextBoolean())
					field[i][j] = new Cell(this, i, j, Cell.MINE);
	}
}
