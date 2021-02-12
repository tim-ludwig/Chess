package me.tludwig.chess.graphics;

import me.tludwig.chess.game.pieces.Alliance;
import me.tludwig.chess.game.pieces.PieceType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Sprites {
	private static final BufferedImage[][] PIECE_SPRITES;

	static {
		PIECE_SPRITES = new BufferedImage[PieceType.values().length][Alliance.values().length];

		for (PieceType type : PieceType.values()) {
			for (Alliance alliance : Alliance.values()) {
				try {
					PIECE_SPRITES[type.ordinal()][alliance.ordinal()] = ImageIO.read(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("graphics/pieces/" + alliance.name() + "_" + type.name() + ".png")));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static BufferedImage getSprite(PieceType type, Alliance alliance) {
		return PIECE_SPRITES[type.ordinal()][alliance.ordinal()];
	}
}
