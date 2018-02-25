package org.polushin.minesweeper.core.field;

/**
 * Поле игры.
 */
public abstract class MineField {

	protected final Cell[][] field;
	protected int minesCount;

	private boolean explode;
	private boolean isWon;
	private int flags;

	/**
	 * @param width Ширина поля.
	 * @param height Высота поля.
	 *
	 * @throws IllegalArgumentException Если ширина или высота не положительные.
	 */
	public MineField(int width, int height) {
		if (width <= 0 || height <= 0)
			throw new IllegalArgumentException("Width and height must be greater than zero!");
		field = new Cell[width][height];
	}

	/**
	 * @return Кол-во установленных мин.
	 */
	public int getMinesCount() {
		return minesCount;
	}

	/**
	 * @param posX Позиция по X.
	 * @param posY Позиция по Y.
	 *
	 * @return Клетку на заданной позиции.
	 */
	public Cell getCell(int posX, int posY) {
		Cell cell = field[posX][posY];
		if (cell == null) {
			init(posX, posY);
			cell = field[posX][posY];
		}
		return cell;
	}

	/**
	 * @return Выиграна ли игра.
	 */
	public boolean isGameWon() {
		if (isWon)
			return true;
		if (explode)
			return false;

		if (field[0][0] == null)
			return false;

		for (Cell[] line : field)
			for (Cell cell : line)
				if (!cell.isOpened() && !cell.isFlagSet())
					return false;
		return isWon = true;
	}

	/**
	 * @return Проиграна ли игра.
	 */
	public boolean isGameOver() {
		return explode;
	}

	/**
	 * @return Ширина поля.
	 */
	public int getWidth() {
		return field.length;
	}

	/**
	 * @return Высота поля.
	 */
	public int getHeight() {
		return field[0].length;
	}

	/**
	 * @return Кол-во выставленных флагов.
	 */
	public int getFlagsCount() {
		return flags;
	}

	/**
	 * Генерирует мины на поле с учетом первого хода игрока.
	 *
	 * @param startX Первый ход игрока по X.
	 * @param startY Первый ход игрока по Y.
	 */
	protected abstract void generate(int startX, int startY);

	/**
	 * Обновляет соседей клетки при ее открытии.
	 */
	void updateNearestCells(InteractResult result, int x, int y) {
		if (x != 0)
			field[x - 1][y].tryToOpen(result);
		if (x + 1 != getWidth())
			field[x + 1][y].tryToOpen(result);
		if (y != 0)
			field[x][y - 1].tryToOpen(result);
		if (y + 1 != getHeight())
			field[x][y + 1].tryToOpen(result);
	}

	/**
	 * Открывает все клетки поля, кроме правильно помеченных флагом, в результате взрыва.
	 *
	 * @return Все клетки поля.
	 */
	InteractResult explosion() {
		explode = true;
		InteractResult result = new InteractResult(true);
		for (Cell[] line : field)
			for (Cell cell : line) {
				cell.openExactly();
				result.addCell(cell);
			}
		return result;
	}

	/**
	 * Обновляет счетчики флагов у соседних клеток.
	 *
	 * @param cell Изменившаясяя клетка.
	 */
	void updateNearestFlags(Cell cell, InteractResult result) {
		flags += cell.isFlagSet() ? 1 : -1;

		for (int i = cell.posX - 1; i <= cell.posX + 1; i++)
			if (i >= 0 && i < getWidth())
				for (int j = cell.posY - 1; j <= cell.posY + 1; j++)
					if (j >= 0 && j < getHeight()) {
						if (cell.isFlagSet())
							field[i][j].addNearFlag();
						else
							field[i][j].removeNearFlag();
						result.addCell(field[i][j]);
					}
	}

	/**
	 * Инициализирует игровое поле после первого хода игрока.
	 */
	private void init(int startX, int startY) {
		generate(startX, startY);

		// Омечаем кол-во соседей
		for (int i = 0; i < field.length; i++)
			for (int j = 0; j < field[i].length; j++)
				field[i][j].setMineNeighbors(countMinesAround(i, j));
	}

	/**
	 * Подсчитывает кол-во мин вокруг клетки.
	 * Если на клетке уже есть мина, будет возвращено 0.
	 */
	private int countMinesAround(int x, int y) {
		if (field[x][y].mine)
			return 0;

		int around = 0;

		for (int i = x - 1; i <= x + 1; i++)
			if (i >= 0 && i < getWidth())
				for (int j = y - 1; j <= y + 1; j++)
					if (j >= 0 && j < getHeight())
						around += field[i][j].mine ? 1 : 0;

		return around;
	}

}
