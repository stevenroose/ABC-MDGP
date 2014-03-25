package be.stevenroose.abcmdgp.abc;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.uno.RuntimeException;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.util.RandomManager;

public class StochasticNO<S extends Solution<I>, I extends Instance> 
implements NeighbourhoodOperator<S, I> {
	
	private List<NeighbourhoodOperator<S, I>> operators;
	private LinkedList<Integer> probabilities;
	
	private Random random;
	
	public StochasticNO(List<NeighbourhoodOperator<S, I>> operators, List<Integer> probabilities) throws IllegalArgumentException {
		if(operators.size() != probabilities.size()) {
			throw new IllegalArgumentException();
		}
		this.operators = operators;
		createProbabilityList(probabilities);
		this.random = RandomManager.getRandom();
	}
	
	public StochasticNO(NeighbourhoodOperator<S, I>[] operators, Integer[] probabilities) throws IllegalArgumentException {
		this(Arrays.asList(operators), Arrays.asList(probabilities));
	}
	
	public StochasticNO(NeighbourhoodOperator<S, I>[] operators) throws IllegalArgumentException {
		Integer[] probabilities = new Integer[operators.length];
		Arrays.fill(probabilities,new Integer(1));
		List<NeighbourhoodOperator<S, I>> opList = Arrays.asList(operators);
		List<Integer> prList = Arrays.asList(probabilities);
		if(opList.size() != prList.size()) {
			throw new IllegalArgumentException();
		}
		this.operators = opList;
		createProbabilityList(prList);
		this.random = RandomManager.getRandom();

	}
	
	private void createProbabilityList(List<Integer> probs) {
		probabilities = new LinkedList<Integer>();
		int cumm = 0;
		for(Integer i : probs) {
			probabilities.add(i + cumm);
			cumm += i;
		}
	}

	@Override
	public S getNeighbour(S solution) {
		int r = random.nextInt(probabilities.getLast()) + 1;
		Iterator<Integer> pIt = probabilities.iterator();
		for(int i = 0 ; i < probabilities.size() ; i++) {
			if(r <= pIt.next())
				return operators.get(i).getNeighbour(solution);
		}
		throw new RuntimeException("Should not happen");
	}

}
