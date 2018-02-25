package org.polushin.minesweeper.gui;

import org.polushin.minesweeper.core.GameHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

public class MainFrame extends JFrame {

	private final GameFieldFrame gameField;
	private final GameStatsDisplay gameStats;
	private final SettingsPanel settingsPanel = new SettingsPanel(this);
	private final RecordsPanel recordsPanel;

	public MainFrame(File scores) {
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints cn = new GridBagConstraints();
		setLayout(layout);

		gameStats = new GameStatsDisplay();
		GameHandler game = new GameHandler(gameStats.getTimer(), scores, SettingsPanel.DEFAULT_NICK);
		gameField = new GameFieldFrame(game, gameStats);
		recordsPanel = new RecordsPanel(this, game);

		resizeWindow();

		// Размещение игрового поля
		JPanel gamePanel = new JPanel();
		gamePanel.setLayout(null);
		gamePanel.add(gameField);
		cn.gridx = 0;
		cn.gridy = 0;
		cn.gridwidth = game.getWidth();
		cn.gridheight = game.getHeight();
		cn.weightx = gameField.getWidth() / (double) getWidth();
		cn.weighty = 1;
		cn.anchor = GridBagConstraints.CENTER;
		cn.fill = GridBagConstraints.BOTH;
		cn.insets = new Insets(5, 5, 0, 0);
		layout.setConstraints(gamePanel, cn);
		add(gamePanel);

		// Размещение кнопки перезапуска игры
		JButton buttonRestart = new JButton("Перезапустить");
		buttonRestart.addMouseListener(new PressDelegate(this::pressRestart));
		cn.gridx = game.getWidth();
		cn.gridy = 0;
		cn.gridheight = 1;
		cn.gridwidth = 4;
		cn.weightx = 0;
		cn.weighty = 0;
		cn.insets = new Insets(0, 10, 0, 10);
		layout.setConstraints(buttonRestart, cn);
		add(buttonRestart);

		// Размещение кнопки настроек
		JButton buttonSettings = new JButton("Настройки");
		buttonSettings.addMouseListener(new PressDelegate(this::pressSettings));
		cn.gridy = 1;
		layout.setConstraints(buttonSettings, cn);
		add(buttonSettings);

		// Размещение кнопки "О программе"
		JButton buttonAbout = new JButton("О программе");
		buttonAbout.addMouseListener(new PressDelegate(this::pressAbout));
		cn.gridy = 2;
		layout.setConstraints(buttonAbout, cn);
		add(buttonAbout);

		// Размещение кнопки рекордов
		JButton buttonRecords = new JButton("Рекорды");
		buttonRecords.addMouseListener(new PressDelegate(this::pressRecords));
		cn.gridy = 3;
		layout.setConstraints(buttonRecords, cn);
		add(buttonRecords);

		// Размещение статистики текущей игры
		cn.gridy = 4;
		layout.setConstraints(gameStats, cn);
		add(gameStats);

		setResizable(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	private void pressRestart() {
		gameField.restartGame(settingsPanel.getMinesCount(), settingsPanel.getUserWidth(),
		                      settingsPanel.getUserHeight());
		gameField.setNick(settingsPanel.getNick());
		resizeWindow();
	}

	private void pressSettings() {
		settingsPanel.setVisible(true);
	}

	private void pressAbout() {
		JOptionPane.showMessageDialog(this, "Автор: Кирилл Полушин", "О программе", JOptionPane.WARNING_MESSAGE);
	}

	private void pressRecords() {
		recordsPanel.setVisible(true);
	}

	/**
	 * Изменяет размеры окна при создании нового поля.
	 */
	private void resizeWindow() {
		setSize(gameField.getWidth() + gameStats.getMaxWidth() + 25, gameField.getHeight() + 30);
	}

	/**
	 * Упрощение для обработки нажатий на кнопки.
	 */
	private class PressDelegate implements MouseListener {

		final PressListener listener;

		PressDelegate(PressListener listener) {
			this.listener = listener;
		}

		@Override
		public void mouseClicked(MouseEvent e) {

		}

		@Override
		public void mousePressed(MouseEvent e) {

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1)
				listener.press();
		}

		@Override
		public void mouseEntered(MouseEvent e) {

		}

		@Override
		public void mouseExited(MouseEvent e) {

		}
	}

	private interface PressListener {

		void press();

	}

}
