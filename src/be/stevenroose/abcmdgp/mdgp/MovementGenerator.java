package be.stevenroose.abcmdgp.mdgp;

import java.util.List;
import java.util.Random;

import es.optsicom.lib.approx.improvement.movement.MovementManager;
import es.optsicom.lib.graph.Node;
import es.optsicom.lib.util.RandomManager;
import es.optsicom.problem.mdgp.Group;
import es.optsicom.problem.mdgp.MDGPSolution;
import es.optsicom.problem.mdgp.improvement.movement.ExchangeMovementGen;

public class MovementGenerator extends ExchangeMovementGen {

	@Override
	public void generateMovements(MovementManager mm) {
		if(!movementPossible())
			return;
		
		Random r = RandomManager.getRandom();
		List<Group> groups = solution.getGroups();
		int numGroups = groups.size();
		
		int node;
		int removeGroupNum;
		Group removeGroup;
		do {
			node = r.nextInt(solution.getInstance().getM());
			removeGroupNum = solution.getGroupOfNode(node);
			removeGroup = groups.get(removeGroupNum);
		} while(removeGroup.getFewerAllowedNodesToRemainFactible() <= 0);
		
		int addGroupNum;
		Group addGroup;
		do {
			addGroupNum = r.nextInt(numGroups);
			addGroup = groups.get(addGroupNum);
		} while(!addGroup.isPossibleToAddMoreNodes() || removeGroupNum == addGroupNum);
		double increment = calculateIncrement(solution, node, removeGroupNum, addGroupNum);
		int[] movement = new int[] {1, node, addGroupNum};
		mm.testMovement(increment, movement);
	}
	
	protected static double calculateIncrement(MDGPSolution solution, int numNode, int removeGroupNum, int addGroupNum) {
		Node node = solution.getInstance().getNode(numNode);
		double old = solution.getGroups().
				get(removeGroupNum).calculateContributionWithoutNode(node, node);
		double newx = solution.getGroups().get(addGroupNum).calculateContributionWithNode(numNode);
		return newx - old;
	}
	
	private boolean movementPossible() {
		int delGroup = -2;
		int addGroup = -2;
		for(Group g : solution.getGroups()) {
			if(g.isPossibleToAddMoreNodes()) {
				if(addGroup == -2)
					addGroup = g.getNumGroup();
				else if(addGroup != -1 && addGroup != g.getNumGroup())
					addGroup = -1;
			}
			if(g.getFewerAllowedNodesToRemainFactible() > 0) {
				if(delGroup == -2)
					delGroup = g.getNumGroup();
				else if(delGroup != -1 && delGroup != g.getNumGroup())
					delGroup = -1;
			}
			if(delGroup == -1 && addGroup == -1)
				return true;
		}
		return false;
	}

}
