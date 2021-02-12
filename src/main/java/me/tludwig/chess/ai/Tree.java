package me.tludwig.chess.ai;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.ToDoubleFunction;

public class Tree<T> {
	protected final T element;
	private final List<Tree<T>> subTrees = new LinkedList<>();

	public Tree(T element) {
		this.element = element;
	}

	public Tree<T> getSubTree(int index) {
		return subTrees.get(index);
	}

	public void addSubTree(Tree<T> tree) {
		subTrees.add(tree);
	}

	public T getElement() {
		return element;
	}

	public EvalPair minimax(ToDoubleFunction<T> evaluation, int depth, boolean evenTurn) {
		if (evenTurn) return max(evaluation, depth);
		return min(evaluation, depth);
	}

	public EvalPair max(ToDoubleFunction<T> evaluation, int depth) {
		return max(evaluation, depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
	}

	public EvalPair min(ToDoubleFunction<T> evaluation, int depth) {
		return min(evaluation, depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
	}

	protected EvalPair max(ToDoubleFunction<T> eval, int depth, double alpha, double beta) {
		if (subTrees.isEmpty() || depth == 0) {
			return new EvalPair(element, eval.applyAsDouble(element));
		}

		double maxScore = Double.NEGATIVE_INFINITY;
		Tree<T> maxSubTree = null;

		List<Tree<T>> sortedSubTrees = new ArrayList<>(subTrees);
		sortedSubTrees.sort(Comparator.comparingDouble(tree -> eval.applyAsDouble(tree.element)));

		for (Tree<T> subTree : sortedSubTrees) {
			double score = subTree.min(eval, depth - 1, alpha, beta).eval;

			alpha = Math.max(alpha, score);
			if (score >= maxScore) {
				maxScore = score;
				maxSubTree = subTree;
			}

			if (beta <= alpha) {
				break;
			}
		}

		assert maxSubTree != null;
		return new EvalPair(maxSubTree.element, maxScore);
	}

	protected EvalPair min(ToDoubleFunction<T> eval, int depth, double alpha, double beta) {
		if (subTrees.isEmpty() || depth == 0) {
			return new EvalPair(element, eval.applyAsDouble(element));
		}

		double minScore = Double.POSITIVE_INFINITY;
		Tree<T> minSubTree = null;

		List<Tree<T>> sortedSubTrees = new ArrayList<>(subTrees);
		sortedSubTrees.sort(Comparator.comparingDouble(tree -> eval.applyAsDouble(tree.element)));

		for (Tree<T> subTree : sortedSubTrees) {
			double score = subTree.max(eval, depth - 1, alpha, beta).eval;

			beta = Math.min(beta, score);
			if (score <= minScore) {
				minScore = score;
				minSubTree = subTree;
			}

			if (beta <= alpha) {
				break;
			}
		}

		assert minSubTree != null;
		return new EvalPair(minSubTree.element, minScore);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		return toString(sb).toString();
	}

	private StringBuilder toString(StringBuilder sb) {
		sb.append(element);

		for (Tree<T> subtree : subTrees)
			subtree.toString(sb).append("\n");

		return sb;
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
