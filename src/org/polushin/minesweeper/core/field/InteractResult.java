package org.polushin.minesweeper.core.field;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Результат взаимодействия с полем.
 * Хранит изменившиеся клетки.
 */
public class InteractResult implements Iterable<Cell> {

	private final Set<Cell> changed = new HashSet<>();

	InteractResult() {

	}

	InteractResult(Cell init) {
		changed.add(init);
	}

	void addCell(Cell cell) {
		changed.add(cell);
	}

	@Override
	public Iterator<Cell> iterator() {
		return new ResultIterator();
	}

	/**
	 * Итератор по набору результата.
	 */
	private final class ResultIterator implements Iterator<Cell> {

		private final Iterator<Cell> iterator = changed.iterator();

		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}

		@Override
		public Cell next() {
			return iterator.next();
		}
	}
}
