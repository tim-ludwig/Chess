package me.tludwig.chess.ai;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;

public class Minimax<T> {
	private final ToDoubleFunction<T> eval;
	private final Function<T, List<T>> step;
	private final Comparator<T> maxElem, minElem;

	public Minimax(ToDoubleFunction<T> eval, Function<T, List<T>> step) {
		this.eval = eval;
		this.step = step;

		maxElem = Comparator.comparingDouble(eval);
		minElem = maxElem.reversed();
	}

	public EvalPair minimax(T element, int depth, boolean evenTurn) {
		if (evenTurn) return max(element, depth);
		return min(element, depth);
	}

	public EvalPair max(T element, int depth) {
		return max(element, depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
	}

	public EvalPair min(T element, int depth) {
		return min(element, depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
	}

	private EvalPair max(T element, int depth, double alpha, double beta) {
		List<T> children;
		if (depth == 0 || (children = step.apply(element)).isEmpty()) {
			return new EvalPair(element, eval.applyAsDouble(element));
		}

		double maxScore = Double.NEGATIVE_INFINITY;
		T maxChild = null;
		children.sort(maxElem);

		for (T child : children) {
			double score = min(child, depth - 1, alpha, beta).eval;

			alpha = Math.max(alpha, score);
			if (score >= maxScore) {
				maxScore = score;
				maxChild = child;
			}

			if (beta <= alpha) {
				break;
			}
		}

		return new EvalPair(maxChild, maxScore);
	}

	private EvalPair min(T element, int depth, double alpha, double beta) {
		List<T> children;
		if (depth == 0 || (children = step.apply(element)).isEmpty()) {
			return new EvalPair(element, eval.applyAsDouble(element));
		}

		double minScore = Double.POSITIVE_INFINITY;
		T minChild = null;
		children.sort(minElem);

		for (T child : children) {
			double score = max(child, depth - 1, alpha, beta).eval;

			beta = Math.min(beta, score);
			if (score <= minScore) {
				minScore = score;
				minChild = child;
			}

			if (beta <= alpha) {
				break;
			}
		}

		return new EvalPair(minChild, minScore);
	}

	public class EvalPair {
		public final T element;
		public final double eval;

		public EvalPair(T element, double eval) {
			this.element = element;
			this.eval = eval;
		}
	}
}
