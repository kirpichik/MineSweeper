package org.polushin.minesweeper.gui;

import org.polushin.minesweeper.core.GameHandler;
import org.polushin.minesweeper.core.PlayerScore;

import javax.swing.*;

public class RecordsPanel extends JDialog {

	private final DefaultListModel<String> listModel = new DefaultListModel<>();
	private final GameHandler gameHandler;

	public RecordsPanel(JFrame frame, GameHandler gameHandler) {
		super(frame, "Рекорды", true);
		this.gameHandler = gameHandler;
		JList<String> list = new JList<>(listModel);
		JScrollPane scroll = new JScrollPane(list);
		add(scroll);
		updateList();
		setSize(300, 200);
	}

	@Override
	public void setVisible(boolean v) {
		updateList();
		super.setVisible(v);
	}

	private void updateList() {
		listModel.clear();
		int pos = 0;
		for (PlayerScore score : gameHandler.getScoreTable())
			listModel.addElement(++pos + ". " + score.toString());
	}

}
