package be.stevenroose.abcmdgp.mdgp;

import java.util.List;

import es.optsicom.lib.graph.Node;
import es.optsicom.lib.util.Weighed;
import es.optsicom.problem.mdgp.Group;



public class NO2a extends DestructiveConstructiveOperator {

	public NO2a(int nd) {
		super(nd);
	}
	
	public NO2a(double fractionalNd) {
		super(fractionalNd);
	}

	@Override
	public void removeComponents(int n) {
		List<Group> groups = solution.getGroups();
		double worstWeight;
		Node worstNode;
		Group worstGroup;
		for(int i = 0 ; i < n ; i++) {
			worstWeight = Double.MAX_VALUE;
			worstNode = null;
			worstGroup = null;
			for(Group g : groups) {
				if(g.getNumNodes() == 0)
					continue;
				Weighed<Node> worst = g.getWorstNode();
				double weight = worst.getWeight();
				if(weight < worstWeight) {
					worstNode = worst.getElement();
					worstWeight = weight;
					worstGroup = g;
				}
			}
			worstGroup.removeNode(worstNode);
			removedNodes.add(worstNode.getIndex());
		}
	}

}
