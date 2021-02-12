package me.tludwig.chess.game;

import me.tludwig.chess.game.pieces.Alliance;

import java.awt.*;

public class Position {
	private final int file, rank;

	public Position(int file, int rank) {
		this.file = file;
		this.rank = rank;
	}

	public static Position fromString(String pos) {
		if (pos.length() != 2) {
			throw new IllegalArgumentException(); // TODO change Exception Type.
		}

		pos = pos.toLowerCase();

		return new Position(fileFromChar(pos.charAt(0)), rankFromChar(pos.charAt(1)));
	}

	public static Position fromPoint(Point p, int squareSize, Alliance perspective) {
		return new Position((int) p.getX() / squareSize, perspective == Alliance.WHITE ? 7 - (int) p.getY() / squareSize : (int) p.getY() / squareSize);
	}

	public static boolean isFile(char file) {
		return ('a' <= file && file <= 'h');
	}

	public static int fileFromChar(char file) {
		if (!isFile(file)) {
			throw new IllegalArgumentException();
		}

		return file - 'a';
	}

	public static boolean isRank(char rank) {
		return ('1' <= rank && rank <= '8');
	}

	public static int rankFromChar(char rank) {
		if (!isRank(rank)) {
			throw new IllegalArgumentException();
		}

		return rank - '1';
	}

	public Position offset(Step step) {
		return new Position(file + step.dx, rank + step.dy);
	}

	public boolean isInBounds() {
		return 0 <= file && file < 8 && 0 <= rank && rank < 8;
	}

	public int rank() {
		return rank;
	}

	public int file() {
		return file;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Position other = (Position) obj;
		if (file != other.file) {
			return false;
		}
		if (rank != other.rank) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return (char) (file + 'a') + "" + (char) (rank + '1');
	}
}
