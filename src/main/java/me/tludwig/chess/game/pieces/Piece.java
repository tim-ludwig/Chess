package me.tludwig.chess.game.pieces;

import me.tludwig.chess.graphics.Sprites;

import java.awt.*;

public class Piece {
	public final PieceType type;
	public final Alliance alliance;

	public Piece(PieceType type, Alliance alliance) {
		this.type = type;
		this.alliance = alliance;
	}

	public int value() {
		return alliance == Alliance.WHITE ? type.value : -type.value;
	}

	public char unicodeFigurine() {
		return type.unicodeFigurine(alliance);
	}

	public void draw(Graphics g, int x, int y) {
		g.drawImage(Sprites.getSprite(type, alliance), x, y, null);
	}

	@Override
	public String toString() {
		return type.symbol;
	}
}
