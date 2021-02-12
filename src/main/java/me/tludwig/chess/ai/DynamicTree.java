package me.tludwig.chess.ai;

import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.stream.Stream;

public class DynamicTree<T> extends Tree<T> {
	private final Function<T, Stream<T>> expansion;

	public DynamicTree(T element, Function<T, Stream<T>> expansion) {
		super(element);
		this.expansion = expansion;
	}

	private void expand() {
		expansion.apply(element).forEach(elem -> addSubTree(new DynamicTree<>(elem, expansion)));
	}

	@Override
	protected EvalPair min(ToDoubleFunction<T> eval, int depth, double alpha, double beta) {
		expand();

		return super.min(eval, depth, alpha, beta);
	}

	@Override
	protected EvalPair max(ToDoubleFunction<T> eval, int depth, double alpha, double beta) {
		expand();

		return super.max(eval, depth, alpha, beta);
	}
}
