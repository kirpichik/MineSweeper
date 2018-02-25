package org.polushin.minesweeper.gui;

import org.polushin.minesweeper.core.GameHandler;
import org.polushin.minesweeper.core.GameTimer;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class MainFrame extends JFrame {

	public MainFrame(File scores) {

		setBounds(0, 0, 700, 700);
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints cn = new GridBagConstraints();
		setLayout(layout);

		// Игровое поле
		GameFieldFrame gameField = new GameFieldFrame(new GameHandler(new GUITimer(), scores, "kirpichik"));
		cn.gridx = 0;
		cn.gridy = 0;
		cn.gridwidth = 1;
		cn.gridheight = 1;
		cn.weightx = 1;
		cn.weighty = 1;
		cn.anchor = GridBagConstraints.WEST;
		cn.fill = GridBagConstraints.BOTH;
		layout.setConstraints(gameField, cn);
		add(gameField);

		//

		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	/**
	 * Центрирует окно при первом создании.
	 */
	private void initFramePosition() {

	}

	private static class GUITimer extends GameTimer {

		@Override
		protected void updateTimer(int currTime) {
			System.out.println(currTime);
		}
	}

}
