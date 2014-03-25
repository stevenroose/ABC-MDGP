package be.stevenroose.abcmdgp.mdgp;

import java.util.Random;

import be.stevenroose.abcmdgp.abc.NeighbourhoodOperator;
import es.optsicom.lib.util.RandomManager;
import es.optsicom.problem.mdgp.MDGPInstance;
import es.optsicom.problem.mdgp.MDGPSolution;

public class NO3 implements NeighbourhoodOperator<MDGPSolution, MDGPInstance> {
	
	private int p;
	private double fracP;
	
	private MDGPSolution solution;
	private Random random;
	
	public NO3(int p) {
		this.p = p;
	}
	
	public NO3(double fractionalP) {
		p = 0;
		fracP = fractionalP;
	}
	
	private void ensureP() {
		if(p == 0) {
			p = (int) (fracP * solution.getInstance().getM());
		}
	}

	@Override
	public MDGPSolution getNeighbour(MDGPSolution solution) {
		this.solution = solution;
		ensureP();
		this.random = RandomManager.getRandom();
		swapSolutions(random.nextInt(this.p));
		MDGPSolution result = this.solution;
		this.solution = null;
		return result;
	}
	
	private void swapSolutions(int n) {
		int numNodes = solution.getInstance().getM();
		for(int i = 0 ; i < n ; i++) {
			int nodeIndex1 = random.nextInt(numNodes);
			int nodeIndex2 = random.nextInt(numNodes);
			int groupIndex1 = solution.getGroupOfNode(nodeIndex1);
			int groupIndex2 = solution.getGroupOfNode(nodeIndex2);
			solution.changeGroupNode(nodeIndex1, groupIndex1, groupIndex2);
			solution.changeGroupNode(nodeIndex2, groupIndex2, groupIndex1);
		}
	}

}
