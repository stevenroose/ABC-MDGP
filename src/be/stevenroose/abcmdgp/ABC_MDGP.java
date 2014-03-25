package be.stevenroose.abcmdgp;

import be.stevenroose.abcmdgp.abc.ABC;
import be.stevenroose.abcmdgp.abc.NeighbourhoodOperator;
import be.stevenroose.abcmdgp.mdgp.LocalImprovementMethod;
import be.stevenroose.abcmdgp.mdgp.NO1;
import es.optsicom.problem.mdgp.MDGPInstance;
import es.optsicom.problem.mdgp.MDGPSolution;
import es.optsicom.problem.mdgp.constructive.C1DSConstructive;


public class ABC_MDGP extends ABC<MDGPSolution, MDGPInstance> {
	
	public ABC_MDGP() {
		this(0, 0, 1);
	}
	
	public ABC_MDGP(NeighbourhoodOperator<MDGPSolution, MDGPInstance> no) {
		this(0, 0, 1, no);
	}
	
	public ABC_MDGP(int limit, int np, float pl) {
		this(limit, np, pl, newNO1());
	}
	
	public ABC_MDGP(int limit, int np, float pl, NeighbourhoodOperator<MDGPSolution, MDGPInstance> no) {
		super(new C1DSConstructive(), new LocalImprovementMethod(), no, limit, np, pl);
	}
	
	private static NeighbourhoodOperator<MDGPSolution, MDGPInstance> newNO1() {
		return new NO1(0.1F);
	}

}
