package org.polushin.minesweeper;

import org.polushin.minesweeper.cui.TextUI;

import java.io.File;
import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		//new MainFrame(new File("scores.json"));
		try {
			new TextUI(new File("scores.json")).runGame();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
