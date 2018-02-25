package org.polushin.minesweeper;

import org.polushin.minesweeper.gui.MainFrame;

import java.io.File;

public class Main {

	public static void main(String[] args) {
		new MainFrame(new File("scores.json"));
	}
}
