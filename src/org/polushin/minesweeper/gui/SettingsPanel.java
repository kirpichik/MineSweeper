package org.polushin.minesweeper.gui;

import org.polushin.minesweeper.core.GameHandler;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.text.NumberFormat;

public class SettingsPanel extends JDialog {

	public static final String DEFAULT_NICK = "noname";

	private final JTextField nick;
	private final JTextField minesCount;
	private final JTextField height;
	private final JTextField width;

	public SettingsPanel(JFrame frame) {
		super(frame, "Настройки", true);

		nick = new JTextField(DEFAULT_NICK);

		NumberFormat format = NumberFormat.getInstance();
		NumberFormatter minesFormatter = new NumberFormatter(format);
		minesFormatter.setValueClass(Integer.class);

		minesFormatter.setMaximum(2500);
		minesFormatter.setMinimum(0);
		minesCount = new JFormattedTextField(minesFormatter);
		minesCount.setText(String.valueOf(GameHandler.DEFAULT_MINES_COUNT));

		NumberFormatter sizesFormatter = new NumberFormatter(format);
		sizesFormatter.setValueClass(Integer.class);

		sizesFormatter.setMaximum(50);
		sizesFormatter.setMinimum(10);
		height = new JFormattedTextField(sizesFormatter);
		height.setText(String.valueOf(GameHandler.DEFAULT_HEIGHT));
		width = new JFormattedTextField(sizesFormatter);
		width.setText(String.valueOf(GameHandler.DEFAULT_WIDTH));

		JButton closeButton = new JButton("ОК");
		closeButton.addActionListener(e -> setVisible(false));

		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

		add(new JLabel("Ник:"));
		add(nick);
		add(new JLabel("Кол-во мин (максимум 2500):"));
		add(minesCount);
		add(new JLabel("Ширина (максимум 50):"));
		add(width);
		add(new JLabel("Высота (максимум 50):"));
		add(height);
		add(new JLabel("Изменения будут применены после перезапуска."));
		add(closeButton);

		setSize(330, 250);
		setResizable(false);
	}

	public int getUserHeight() {
		return Integer.parseInt(height.getText());
	}

	public int getUserWidth() {
		return Integer.parseInt(width.getText());
	}

	public int getMinesCount() {
		return Integer.parseInt(minesCount.getText());
	}

	public String getNick() {
		return nick.getText();
	}
}
