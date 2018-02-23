package org.polushin.minesweeper.core;

/**
 * Поле игры.
 */
public abstract class MineField {

	protected final Cell[][] field;

	/**
	 * @param width Ширина поля.
	 * @param height Высота поля.
	 */
	public MineField(int width, int height) {
		if (width <= 0 || height <= 0)
			throw new IllegalArgumentException("Width and height must be greater than zero!");
		field = new Cell[width][height];
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
		for (Cell[] line : field)
			for (Cell cell : line)
				if (!cell.isOpened() || cell.isMine && !cell.isFlagSet())
					return false;
		return true;
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
		for (int i = x - 1; i <= x + 1; i++)
			if (i > 0 && i < getWidth())
				for (int j = y - 1; j <= y + 1; j++)
					if (j > 0 && j < getHeight())
						field[i][j].open(result);
	}

	/**
	 * Инициализирует игровое поле после первого хода игрока.
	 */
	private void init(int startX, int startY) {
		generate(startX, startY);

		// Отмечаем кол-во соседей и создаем оставшиеся клетки.
		for (int i = 0; i < field.length; i++)
			for (int j = 0; j < field[i].length; i++) {
				Cell cell = field[i][j];
				if (cell == null)
					field[i][j] = cell = new Cell(this, i, j, Cell.EMPTY);
				cell.setMineNeighbors(countMinesAround(i, j));
			}
	}

	/**
	 * Подсчитывает кол-во мин вокруг клетки.
	 * Если на клетке уже есть мина, будет возвращено 0.
	 */
	private int countMinesAround(int x, int y) {
		if (field[x][y].isMine)
			return 0;

		int around = 0;

		for (int i = x - 1; i <= x + 1; i++)
			if (i > 0 && i < getWidth())
				for (int j = y - 1; j <= y + 1; j++)
					if (j > 0 && j < getHeight())
						around += field[i][j].isMine ? 1 : 0;

		return around;
	}

}
