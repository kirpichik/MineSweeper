package org.polushin.minesweeper.core.field;

import java.util.Random;

/**
 * Генератор случайного поля.
 */
public class RandomMinesGenerator extends MineField {

	private final Random random;
	private final int mines;

	/**
	 * @param mines Кол-во мин на поле.
	 * @param width Ширина поля.
	 * @param height Высота поля.
	 *
	 * @throws IllegalArgumentException Если ширина, высота или кол-во мин не положительны или
	 * невозможно расставить данное кол-во мин.
	 */
	public RandomMinesGenerator(int mines, int width, int height) {
		super(width, height);
		if (mines <= 0)
			throw new IllegalArgumentException("Mines count must be positive!");
		if (width * height <= mines)
			throw new IllegalArgumentException(
					"Cannot place " + mines + " mines to " + width + "x" + height + "field.");
		random = new Random();
		this.mines = mines;
	}

	/**
	 * @param mines Кол-во мин на поле.
	 * @param width Ширина поля.
	 * @param height Высота поля.
	 * @param seed Зерно для генерации поля.
	 *
	 * @throws IllegalArgumentException Если ширина, высота или кол-во мин не положительны или
	 * невозможно расставить данное кол-во мин.
	 */
	public RandomMinesGenerator(int mines, int width, int height, long seed) {
		super(width, height);
		if (mines <= 0)
			throw new IllegalArgumentException("Mines count must be positive!");
		if (width * height <= mines)
			throw new IllegalArgumentException(
					"Cannot place " + mines + " mines to " + width + "x" + height + "field.");
		this.mines = mines;
		random = new Random(seed);
	}

	@Override
	protected void generate(int startX, int startY) {
		field[startX][startY] = new Cell(this, startX, startY, Cell.EMPTY);

		for (int placed = 0; placed < mines; placed++) {
			int x = random.nextInt(getWidth());
			int y = random.nextInt(getHeight());
			if (field[x][y] != null) // Клетка не пуста, ищем ближайшую пустую
				while (true) {
					if (field[x][y] == null && x != startX && y != startY) // Нашли пустую клетку
						break;

					y++;
					if (y == getHeight()) { // Конец столбцов, переходим на следующую строку
						y = 0;
						if (++x == getWidth()) // Конец строк, начинаем по кругу
							x = 0;
					}
				}
			field[x][y] = new Cell(this, x, y, Cell.MINE);
		}

		// Заполняем оставшиеся клетки.
		for (int i = 0; i < getWidth(); i++)
			for (int j = 0; j < getHeight(); j++)
				if (field[i][j] == null)
					field[i][j] = new Cell(this, i, j, Cell.EMPTY);
	}
}
