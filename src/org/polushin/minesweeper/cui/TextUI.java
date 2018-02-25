package org.polushin.minesweeper.cui;

import org.polushin.minesweeper.core.GameHandler;
import org.polushin.minesweeper.core.GameTimer;
import org.polushin.minesweeper.core.PlayerScore;
import org.polushin.minesweeper.core.field.Cell;
import org.polushin.minesweeper.core.field.InteractResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class TextUI extends GameTimer {

	private static final char CLOSED = '.';
	private static final char MINE = '*';
	private static final char FLAG = '#';

	private char[][] field;
	private volatile int currTime;
	private final GameHandler game;

	public TextUI(File scores) {
		game = new GameHandler(this, scores, "noname");
		createField(game.getWidth(), game.getHeight());
	}

	public void runGame() throws IOException {
		String line;
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		printField();
		System.out.print("> ");
		while ((line = reader.readLine()) != null) {
			if (line.isEmpty()) {
				System.out.print("> ");
				continue;
			}
			String[] split = line.split(" ");
			String cmd = split[0];
			String[] args = new String[split.length - 1];
			System.arraycopy(split, 1, args, 0, args.length);
			if (!executeCommand(cmd, args))
				System.out.println("Command not found.");
			else
				printField();
			System.out.print("> ");
		}
	}

	private boolean executeCommand(String cmd, String[] args) {
		switch (cmd) {
			case "nick":
				commandNick(args);
				break;
			case "records":
				commandRecords(args);
				break;
			case "reset":
				commandReset(args);
				break;
			case "f":
			case "flag":
				commandFlag(args);
				break;
			case "o":
			case "open":
				commandOpen(args);
				break;
			case "exit":
				commandExit(args);
				break;
			default:
				return false;
		}
		return true;
	}

	private void printField() {
		if (game.isGameWon()) {
			System.out.println("YOU WIN!");
			return;
		} else if (game.isGameOver()) {
			System.out.println("YOU LOSE!");
			return;
		}
		for (char[] line : field) {
			for (char cell : line)
				System.out.print(cell + " ");
			System.out.println('\n');
		}
		System.out.println(String.format("%d/%d, %d sec.", game.getFlagsCount(), game.getMinesCount(), currTime));
	}

	private void createField(int width, int height) {
		field = new char[game.getWidth()][game.getHeight()];

		for (int i = 0; i < game.getWidth(); i++)
			for (int j = 0; j < game.getHeight(); j++)
				field[i][j] = CLOSED;
	}

	@Override
	protected void updateTimer(int currTime) {
		this.currTime = currTime;
	}

	private void updateCells(InteractResult result) {
		for (Cell cell : result) {
			char value;
			if (cell.isMine())
				value = MINE;
			else if (cell.isFlagSet())
				value = FLAG;
			else
				value = (char) ('0' + cell.getMineNeighbors());
			field[cell.posX][cell.posY] = value;
		}
	}

	private void commandOpen(String[] args) {
		if (args.length != 2) {
			System.out.println("Need args: <x> <y>");
			return;
		}
		int x, y;
		try {
			x = Integer.parseInt(args[0]);
			y = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			System.out.println("Wrong args.");
			return;
		}
		updateCells(game.openCell(x, y));
	}

	private void commandFlag(String[] args) {
		if (args.length != 2) {
			System.out.println("Need args: <x> <y>");
			return;
		}
		int x, y;
		try {
			x = Integer.parseInt(args[0]);
			y = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			System.out.println("Wrong args.");
			return;
		}
		updateCells(game.markCell(x, y));
	}

	private void commandReset(String[] args) {
		if (args.length != 3) {
			System.out.println("Need args: <mines> <width> <height>");
			return;
		}
		int mines, width, height;
		try {
			mines = Integer.parseInt(args[0]);
			width = Integer.parseInt(args[1]);
			height = Integer.parseInt(args[2]);
		} catch (NumberFormatException e) {
			System.out.println("Wrong args.");
			return;
		}
		game.restartGame(mines, width, height);
		createField(width, height);
	}

	private void commandNick(String[] args) {
		if (args.length != 1) {
			System.out.println("Need arg: <nick>");
			return;
		}
		game.setNick(args[0]);
		System.out.println("Nick set.");
	}

	private void commandRecords(String[] args) {
		System.out.println("Scores:");
		for (PlayerScore score : game.getScoreTable())
			System.out.println(score.toString());
	}

	private void commandExit(String[] args) {
		System.exit(0);
	}
}
