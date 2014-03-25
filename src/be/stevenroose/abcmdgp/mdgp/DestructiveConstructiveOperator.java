package be.stevenroose.abcmdgp.mdgp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import be.stevenroose.abcmdgp.abc.NeighbourhoodOperator;
import es.optsicom.lib.util.RandomList;
import es.optsicom.lib.util.RandomManager;
import es.optsicom.problem.mdgp.Group;
import es.optsicom.problem.mdgp.MDGPInstance;
import es.optsicom.problem.mdgp.MDGPSolution;

public abstract class DestructiveConstructiveOperator implements NeighbourhoodOperator<MDGPSolution, MDGPInstance> {

	private int nd;
	private double fracNd;

	protected MDGPSolution solution;
	protected List<Integer> removedNodes;

	public DestructiveConstructiveOperator(int nd) {
		this.nd = nd;
	}
	
	public DestructiveConstructiveOperator(double fractionalNd) {
		nd = 0;
		fracNd = fractionalNd;
	}
	
	private void ensureNd() {
		if(nd == 0) {
			nd = (int) (fracNd * solution.getInstance().getM());
		}
	}

	public MDGPSolution getNeighbour(MDGPSolution solutionx) {
		solution = solutionx.createCopy();
		ensureNd();
		int nbRemove = RandomManager.getRandom().nextInt(this.nd);
		removedNodes = new ArrayList<Integer>(nbRemove);
		removeComponents(nbRemove);
		addRemovedNodes();
		MDGPSolution result = solution;
		solution = null;
		return result;
	}

	private void addRemovedNodes() {
		Random r = RandomManager.getRandom();

		List<Group> groups = solution.getGroups();
		List<Group> infactibleGroups = new ArrayList<Group>();

		List<Integer> nodes = this.removedNodes;

		for (Group group : groups) {
			if (!group.isFactible())
				infactibleGroups.add(group);
		}
		double bestContribution;
		double contribution;
		Group bestContributionGroup;
		while (infactibleGroups.size() > 0) {
			int node = nodes.remove(r.nextInt(nodes.size()));

			bestContribution = -1 * Double.MAX_VALUE;

			bestContributionGroup = null;

			for (Group group : infactibleGroups) {
				contribution = group.calculateContributionWithNode(node) 
						/ (group.getNumNodes() + 1);

				if (contribution >= bestContribution) {
					bestContribution = contribution;
					bestContributionGroup = group;
				}
			}

			bestContributionGroup.addNode(node);
			if (bestContributionGroup.isFactible())
				infactibleGroups.remove(bestContributionGroup);

		}

		for (int node : RandomList.create(nodes)) {
			bestContribution = -1 * Double.MAX_VALUE;
			bestContributionGroup = null;

			for (Group group : groups) {
				if (!group.isPossibleToAddMoreNodes())
					continue;
				contribution = group
						.calculateContributionWithNode(node)
						/ (group.getNumNodes() + 1);
				if (contribution > bestContribution) {
					bestContribution = contribution;
					bestContributionGroup = group;
				}
			}

			bestContributionGroup.addNode(node);
		}

	}

	public abstract void removeComponents(int n);

}
