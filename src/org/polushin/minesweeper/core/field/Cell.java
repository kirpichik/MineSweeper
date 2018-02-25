package org.polushin.minesweeper.core.field;

/**
 * Клетка поля.
 */
public class Cell {

	public static final boolean MINE = true;
	public static final boolean EMPTY = false;

	public final int posX, posY;

	private final MineField field;

	final boolean mine;

	private int mineNeighbors;
	private boolean opened;
	private boolean flag;
	private int nearFlags;

	/**
	 * @param field Игровое поле на котором находится клетка.
	 * @param posX Позиция по X.
	 * @param posY Позиция по Y.
	 * @param mine Является ли данная клетка миной.
	 */
	Cell(MineField field, int posX, int posY, boolean mine) {
		this.field = field;
		this.posX = posX;
		this.posY = posY;
		this.mine = mine;
	}

	/**
	 * Устанавливает кол-во заминированных соседей данной клетки.
	 *
	 * @param neighbors Кол-во соседей.
	 */
	void setMineNeighbors(int neighbors) {
		mineNeighbors = neighbors;
	}

	/**
	 * @return Кол-во заминированных соседей.
	 */
	public int getMineNeighbors() {
		return mineNeighbors;
	}

	/**
	 * @return Установлен ли на клетку флаг.
	 */
	public boolean isFlagSet() {
		return flag;
	}

	/**
	 * @return Отмечена ли данная клетка как переполненная.
	 */
	public boolean isFlagsOverflow() {
		return opened && !mine && nearFlags > mineNeighbors;
	}

	/**
	 * Добавляет флаг-сосед.
	 */
	void addNearFlag() {
		nearFlags++;
	}

	/**
	 * Удаляет флаг-сосед.
	 */
	void removeNearFlag() {
		if (nearFlags != 0)
			nearFlags--;
	}

	/**
	 * Устанавливает или удаляет флаг на клетке.
	 *
	 * @return Результат взаимодействия с полем.
	 */
	public InteractResult changeFlagSet() {
		if (field.isGameOver())
			return new InteractResult(true);
		if (opened || field.isGameWon())
			return new InteractResult(false);
		flag = !flag;
		InteractResult result = new InteractResult(this);
		field.updateNearestFlags(this, result);
		return result;
	}

	/**
	 * @return Открыта ли данная клетка.
	 */
	public boolean isOpened() {
		return opened;
	}

	/**
	 * @return {@code true} только если на клетке установлена мина и она открыта.
	 */
	public boolean isMine() {
		return opened && mine;
	}

	/**
	 * Открывает клетку.
	 *
	 * @return Результат взаимодействия с полем.
	 */
	public InteractResult open() {
		if (opened)
			return new InteractResult(false);
		if (mine)
			return field.explosion();
		opened = true;
		flag = false;
		InteractResult result = new InteractResult(this);
		field.updateNearestCells(result, posX, posY);
		return result;
	}

	/**
	 * Если на клетке не находится мина, открывает клетку и пытается открыть своих соседей.
	 */
	void tryToOpen(InteractResult result) {
		if (mine || opened)
			return;
		opened = true;
		result.addCell(this);
		field.updateNearestCells(result, posX, posY);
	}

	/**
	 * Только открывает клетку.
	 */
	void openExactly() {
		opened = true;
	}

	@Override
	public int hashCode() {
		return (posX << 16) | posY & 0xffff;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Cell))
			return false;
		Cell cell = (Cell) obj;
		return cell.posX == posX && cell.posY == posY;
	}
}
