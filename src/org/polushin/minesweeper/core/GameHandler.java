package org.polushin.minesweeper.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.polushin.minesweeper.core.field.InteractResult;
import org.polushin.minesweeper.core.field.MineField;
import org.polushin.minesweeper.core.field.RandomMinesGenerator;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Основной обработчик управления игрой.
 */
public class GameHandler {

	public static final int DEFAULT_WIDTH = 20;
	public static final int DEFAULT_HEIGHT = 20;
	public static final int DEFAULT_MINES_COUNT = 200;

	private static final Gson GSON;

	private List<PlayerScore> scores = new LinkedList<>();
	private final File scoresFile;
	private final GameTimer timer;

	private String nick;
	private MineField game;
	private boolean resultRecorded;

	/**
	 * @param timer Таймер для отображения изменений.
	 * @param scores Файл с предыдущими рекордами.
	 * @param nick Ник игрока.
	 * @param mines Кол-во мин.
	 * @param width Ширина поля.
	 * @param height Высота поля.
	 *
	 * @throws IllegalArgumentException Если timer, или scores, или nick равны {@code null},
	 * либо ширина или высота не положительны.
	 */
	public GameHandler(GameTimer timer, File scores, String nick, int mines, int width, int height) {
		if (timer == null)
			throw new IllegalArgumentException("Timer cannot be null!");
		if (scores == null)
			throw new IllegalArgumentException("Scores file cannot be null!");
		if (nick == null)
			throw new IllegalArgumentException("Nick cannot be null!");
		this.timer = timer;
		scoresFile = scores;
		this.nick = nick;
		loadScores();
		restartGame(mines, width, height);
	}

	/**
	 * @param timer Таймер для отображения изменений.
	 * @param scores Файл с предыдущими рекордами.
	 * @param nick Ник игрока.
	 *
	 * @throws IllegalArgumentException timer, or scores, or nick is {@code null}.
	 */
	public GameHandler(GameTimer timer, File scores, String nick) {
		this(timer, scores, nick, DEFAULT_MINES_COUNT, DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}

	/**
	 * @return Ширина поля.
	 */
	public int getWidth() {
		return game.getWidth();
	}

	/**
	 * @return Высота поля.
	 */
	public int getHeight() {
		return game.getHeight();
	}

	/**
	 * Перезапускает игру с заданными новыми размерами поля и кол-вом мин.
	 *
	 * @param mines Кол-во мин.
	 * @param width Ширина поля.
	 * @param height Высота поля.
	 *
	 * @throws IllegalArgumentException Если ширина или высота не положительны.
	 */
	public void restartGame(int mines, int width, int height) {
		game = new RandomMinesGenerator(mines, width, height);
		resultRecorded = false;
		timer.stopTimer();
	}

	/**
	 * Перезапускает игру с заданными новыми размерами поля, кол-вом мин и заданным зерном.
	 *
	 * @param mines Кол-во мин.
	 * @param width Ширина поля.
	 * @param height Высота поля.
	 * @param seed Зерно генерации поля.
	 *
	 * @throws IllegalArgumentException Если ширина или высота не положительны.
	 */
	public void restartGame(int mines, int width, int height, long seed) {
		game = new RandomMinesGenerator(mines, width, height, seed);
		resultRecorded = false;
		timer.stopTimer();
	}

	/**
	 * Пытается открыть клетку.
	 *
	 * @param x Позиция клетки по X.
	 * @param y Позиция клетки по Y.
	 *
	 * @return Результат взаимодействия с полем.
	 *
	 * @throws IllegalArgumentException Попытка открыть клетку вне поля.
	 * @throws IllegalStateException Попытка изменить поле во время окончания игры.
	 */
	public InteractResult openCell(int x, int y) {
		if (game.isGameOver() || game.isGameWon())
			throw new IllegalStateException("Game ends.");
		if (x < 0 || x >= game.getWidth() || y < 0 || y >= game.getHeight())
			throw new IllegalArgumentException("Cell must be at field!");
		if (!timer.isRun())
			timer.resetTimer();
		InteractResult result = game.getCell(x, y).open();
		if (result.isExplode())
			timer.stopTimer();
		return result;
	}

	/**
	 * Отмечает клетку флажком.
	 *
	 * @param x Позиция клетки по X.
	 * @param y Позиция клетки по Y.
	 *
	 * @return Результат взаимодействия с полем.
	 *
	 * @throws IllegalArgumentException Попытка пометить клетку вне поля.
	 * @throws IllegalStateException Попытка изменить поле во время окончания игры.
	 */
	public InteractResult markCell(int x, int y) {
		if (game.isGameWon() || game.isGameOver())
			throw new IllegalStateException("Game ends.");
		if (x < 0 || x >= game.getWidth() || y < 0 || y >= game.getHeight())
			throw new IllegalArgumentException("Cell must be at field!");
		return game.getCell(x, y).changeFlagSet();
	}

	/**
	 * @return Выиграна ли игра.
	 */
	public boolean isGameWon() {
		if (game.isGameWon())
			recordResult();
		return game.isGameWon();
	}

	/**
	 * @return Проиграна ли игра.
	 */
	public boolean isGameOver() {
		return game.isGameOver();
	}

	/**
	 * @return Таблица рекордов игроков.
	 */
	public Iterable<PlayerScore> getScoreTable() {
		return scores;
	}

	/**
	 * @return Ник игрока.
	 */
	public String getNick() {
		return nick;
	}

	/**
	 * Устанавливает ник игроку.
	 *
	 * @param nick Новый ник игрока.
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}

	/**
	 * Останавливает отсчет времени и записывает результат в таблицу рекордов.
	 */
	private void recordResult() {
		if (resultRecorded)
			return;
		resultRecorded = true;
		timer.stopTimer();
		// TODO - сортировка результатов
		scores.add(new PlayerScore(nick, game.getWidth() * game.getHeight(), timer.getTimePassed()));
		saveScores();
	}

	/**
	 * Сохраняет статистику игр в Json.
	 */
	private void saveScores() {
		try (Writer writer = new FileWriter(scoresFile)) {
			GSON.toJson(scores, new TypeToken<LinkedList<PlayerScore>>() {}.getType(), writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Загружает статистику игр из Json.
	 */
	private void loadScores() {
		if (!scoresFile.exists())
			return;
		try (Reader reader = new FileReader(scoresFile)) {
			scores = GSON.fromJson(reader, new TypeToken<LinkedList<PlayerScore>>() {}.getType());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static {
		GsonBuilder builder = new GsonBuilder();
		JsonDeserializer<PlayerScore> deserializer = (json, type, context) -> {
			JsonObject object = json.getAsJsonObject();
			String nick = object.get("nick").getAsString();
			int square = object.get("square").getAsInt();
			int time = object.get("time").getAsInt();
			return new PlayerScore(nick, square, time);
		};
		builder.registerTypeAdapter(PlayerScore.class, deserializer);
		builder.setPrettyPrinting();
		GSON = builder.create();
	}

}
