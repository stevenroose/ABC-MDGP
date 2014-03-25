package be.stevenroose.abcmdgp.mdgp;

import java.util.ArrayList;
import java.util.List;

import es.optsicom.lib.util.RandomList;

public class NO1 extends DestructiveConstructiveOperator {

	public NO1(int nd) {
		super(nd);
	}

	public NO1(double fractionalNd) {
		super(fractionalNd);
	}

	@Override
	public void removeComponents(int n) {
		for(int index : getRandomIntList(n)) {
			solution.removeNodeFromGroup(index);
			removedNodes.add(index);
		}
	}

	private RandomList<Integer> getRandomIntList(int n) {
		List<Integer> ret = new ArrayList<Integer>(n);
		for(int i = 0; i < n; i++, ret.add(i));
		return RandomList.create(ret);
	}

}
