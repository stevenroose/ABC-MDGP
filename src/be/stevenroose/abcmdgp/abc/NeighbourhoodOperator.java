package be.stevenroose.abcmdgp.abc;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;

public interface NeighbourhoodOperator<S extends Solution<I>, I extends Instance> {
	
	S getNeighbour(S solution);
	
}
