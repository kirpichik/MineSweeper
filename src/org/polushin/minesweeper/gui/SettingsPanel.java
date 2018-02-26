package org.polushin.minesweeper.gui;

import org.polushin.minesweeper.core.GameHandler;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.text.NumberFormat;

public class SettingsPanel extends JDialog {

	public static final String DEFAULT_NICK = "noname";

	private final JTextField nick;
	private final JFormattedTextField minesCount;
	private final JFormattedTextField height;
	private final JFormattedTextField width;

	private final MainFrame frame;

	public SettingsPanel(MainFrame frame) {
		super(frame, "Настройки", true);

		this.frame = frame;

		nick = new JTextField(DEFAULT_NICK);

		NumberFormat format = NumberFormat.getInstance();
		NumberFormatter minesFormatter = new NumberFormatter(format);
		minesFormatter.setValueClass(Integer.class);

		minesFormatter.setMaximum(2500);
		minesFormatter.setMinimum(0);
		minesCount = new JFormattedTextField(minesFormatter);
		minesCount.setValue(GameHandler.DEFAULT_MINES_COUNT);

		NumberFormatter sizesFormatter = new NumberFormatter(format);
		sizesFormatter.setValueClass(Integer.class);

		sizesFormatter.setMaximum(50);
		sizesFormatter.setMinimum(10);
		height = new JFormattedTextField(sizesFormatter);
		height.setValue(GameHandler.DEFAULT_HEIGHT);
		width = new JFormattedTextField(sizesFormatter);
		width.setValue(GameHandler.DEFAULT_WIDTH);

		JButton closeButton = new JButton("ОК");
		closeButton.addActionListener(e -> closeSettings());

		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

		add(new JLabel("Ник:"));
		add(nick);
		add(new JLabel("Кол-во мин (от 1 до 2500):"));
		add(minesCount);
		add(new JLabel("Ширина (от 10 до 50):"));
		add(width);
		add(new JLabel("Высота (от 10 до 50):"));
		add(height);
		add(new JLabel("Изменения будут применены после перезапуска."));
		add(closeButton);

		setSize(330, 250);
		setResizable(false);
	}

	/**
	 * @return Высота поля.
	 */
	public int getUserHeight() {
		return height.getValue() == null ? -1 : (int) height.getValue();
	}

	/**
	 * @return Ширина поля.
	 */
	public int getUserWidth() {
		return width.getValue() == null ? -1 : (int) width.getValue();
	}

	/**
	 * @return Кол-во мин.
	 */
	public int getMinesCount() {
		return minesCount.getValue() == null ? -1 : (int) minesCount.getValue();
	}

	/**
	 * @return Ник игрока.
	 */
	public String getNick() {
		return nick.getText();
	}

	private void closeSettings() {
		int mines = getMinesCount();
		int width = getUserWidth();
		int height = getUserHeight();
		if (mines == -1 || width == -1 || height == -1 || getNick().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Проверьте правильность введенных данных!", "Ошибка",
			                              JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (mines >= width * height) {
			JOptionPane.showMessageDialog(this, "Нельзя разместить столько мин на поле таких размеров!", "Ошибка",
			                              JOptionPane.WARNING_MESSAGE);
			return;
		}

		setVisible(false);

		int result = JOptionPane.showConfirmDialog(frame, "Перезапустить игру с новыми настройками?", "Перезапустить?",
		                                           JOptionPane.YES_NO_OPTION);
		if (result == JOptionPane.YES_OPTION)
			frame.pressRestart();
	}
}
