package me.tludwig.chess;

import me.tludwig.chess.game.Board;

import java.util.stream.Stream;

public class TreeTest {
	public static void main(String[] args) {
		for (int n = 0; n <= 5; n++) {
			Stream<Board> states = Stream.of(new Board());

			for (int i = 0; i < n; i++) {
				states = step(states);
			}

			long time = System.currentTimeMillis();
			long count = states.count();
			time = System.currentTimeMillis() - time;

			System.out.printf("%2d: %15d %9.2fs%n", n, count, time / 1_000d);
		}
	}

	public static Stream<Board> step(Stream<Board> states) {
		return states.flatMap(board -> board.allPossibleMoves().map(move -> board.copy().apply(move)));
	}
}
