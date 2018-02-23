package org.polushin.minesweeper.core;

/**
 * Клетка поля.
 */
public class Cell {

	public static final boolean MINE = true;
	public static final boolean EMPTY = false;

	public final int posX, posY;

	private final MineField field;

	final boolean isMine;

	private int mineNeighbors;
	private boolean opened;
	private boolean flag;

	/**
	 * @param field Игровое поле на котором находится клетка.
	 * @param posX Позиция по X.
	 * @param posY Позиция по Y.
	 * @param isMine Является ли данная клетка миной.
	 */
	public Cell(MineField field, int posX, int posY, boolean isMine) {
		this.field = field;
		this.posX = posX;
		this.posY = posY;
		this.isMine = isMine;
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
	 * Устанавливает или удаляет флаг на клетке.
	 */
	public void changeFlagSet() {
		flag = !flag;
	}

	/**
	 * @return Открыта ли данная клетка.
	 */
	public boolean isOpened() {
		return opened;
	}

	/**
	 * Открывает клетку.
	 *
	 * @return Список обновленных клеток после открытия данной.
	 *
	 * @throws MineExplosionException При попытке открыть клетку с миной.
	 */
	public InteractResult open() throws MineExplosionException {
		if (opened)
			return new InteractResult();
		if (isMine)
			throw new MineExplosionException();
		opened = true;
		InteractResult result = new InteractResult(this);
		field.updateNearestCells(result, posX, posY);
		return result;
	}

	/**
	 * Если на клетке не находится мина, открывает клетку и пытается открыть своих соседей.
	 */
	void open(InteractResult result) {
		if (isMine || opened)
			return;
		opened = true;
		result.addCell(this);
		field.updateNearestCells(result, posX, posY);
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
