package be.stevenroose.abcmdgp.mdgp;

import java.util.List;
import java.util.Random;

import es.optsicom.lib.approx.improvement.movement.MovementManager;
import es.optsicom.lib.graph.Node;
import es.optsicom.lib.util.RandomManager;
import es.optsicom.problem.mdgp.Group;
import es.optsicom.problem.mdgp.MDGPSolution;
import es.optsicom.problem.mdgp.improvement.movement.ExchangeMovementGen;

public class SwapGenerator extends ExchangeMovementGen {

	@Override
	public void generateMovements(MovementManager mm) {
		Random r = RandomManager.getRandom();
		List<Group> groups = solution.getGroups();
		if(groups.size() <= 1)
			return;
		
		int node1 = r.nextInt(solution.getInstance().getM());
		int groupNum = solution.getGroupOfNode(node1);
		Group node1group = groups.get(groupNum);

		int node2;
		Group node2group;
		do {
			node2 = r.nextInt(solution.getInstance().getM());
			groupNum = solution.getGroupOfNode(node2);
			node2group = groups.get(groupNum);
		} while(node1group.equals(node2group));
		double increment = calculateIncrement(solution, node1, node2);
		int[] movement = new int[] {0, node1, node2};
		mm.testMovement(increment, movement);
	}
	
	protected static double calculateIncrement(MDGPSolution solution, int node1, int node2) {
		Group node1group = solution.getGroups().get(solution.getGroupOfNode(node1));
		Group node2group = solution.getGroups().get(solution.getGroupOfNode(node2));
		Node node1_ = solution.getInstance().getNode(node1);
		Node node2_ = solution.getInstance().getNode(node2);
		double a = node1group.calculateContributionWithoutNode(node1_, node2_);
		double b = node1group.calculateContributionWithoutNode(node1_, node1_);
		double c = node2group.calculateContributionWithoutNode(node2_, node1_);
		double d = node2group.calculateContributionWithoutNode(node2_, node2_);
		return (a + c) - (b + d);
	}

}
