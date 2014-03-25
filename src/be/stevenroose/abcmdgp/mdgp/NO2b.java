package be.stevenroose.abcmdgp.mdgp;

import java.util.List;
import java.util.Random;

import es.optsicom.lib.graph.Node;
import es.optsicom.lib.util.RandomManager;
import es.optsicom.problem.mdgp.Group;



public class NO2b extends DestructiveConstructiveOperator {

	public NO2b(int nd) {
		super(nd);
	}
	
	public NO2b(double fractionalNd) {
		super(fractionalNd);
	}

	@Override
	public void removeComponents(int n) {
		List<Group> groups = solution.getGroups();
		Random r = RandomManager.getRandom();
		Group g;
		for(int i = 0 ; i < n ; i++) {
			do {
				g = groups.get(r.nextInt(groups.size()));
			} while(g.getNumNodes() == 0);
			Node worst = g.getWorstNode().getElement();
			g.removeNode(worst);
			removedNodes.add(worst.getIndex());
		}
	}

}
