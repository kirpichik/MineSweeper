package org.polushin.minesweeper;

import org.polushin.minesweeper.cui.TextUI;
import org.polushin.minesweeper.gui.MainFrame;

import java.io.File;
import java.io.IOException;

public class Main {

	private static final File SCORES_FILE = new File("scores.json");

	public static void main(String[] args) throws IOException {
		if (args.length < 1) {
			System.out.println("No args, run GUI...");
			new MainFrame(SCORES_FILE);
			return;
		}

		switch (args[0]) {
			case "gui":
				new MainFrame(SCORES_FILE);
				break;
			case "cui":
				new TextUI(SCORES_FILE).runGame();
				break;
			default:
				System.out.println("Need arg: <gui/cui>");
				break;
		}
	}
}
