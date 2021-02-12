package me.tludwig.chess.game;

public enum Step {
	UP(0, -1),
	DOWN(0, 1),
	LEFT(-1, 0),
	RIGHT(1, 0),
	UP_LEFT(-1, -1),
	UP_RIGHT(1, -1),
	DOWN_LEFT(-1, 1),
	DOWN_RIGHT(1, 1),
	LEFT_LEFT_UP(-2, -1),
	LEFT_LEFT_DOWN(-2, 1),
	RIGHT_RIGHT_UP(2, -1),
	RIGHT_RIGHT_DOWN(2, 1),
	LEFT_UP_UP(-1, -2),
	LEFT_DOWN_DOWN(-1, 2),
	RIGHT_UP_UP(1, -2),
	RIGHT_DOWN_DOWN(1, 2);

	public static final Step[] ROOK = {UP, DOWN, LEFT, RIGHT};
	public static final Step[] BISHOP = {UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT};
	public static final Step[] KING = {UP, DOWN, LEFT, RIGHT, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT};
	public static final Step[] QUEEN = KING;
	public static final Step[] KNIGHT = {LEFT_LEFT_UP, LEFT_LEFT_DOWN, RIGHT_RIGHT_UP, RIGHT_RIGHT_DOWN, LEFT_UP_UP, LEFT_DOWN_DOWN, RIGHT_UP_UP, RIGHT_DOWN_DOWN};

	public final int dx, dy;

	Step(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
	}
}
