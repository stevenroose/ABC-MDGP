package be.stevenroose.abcmdgp.mdgp;

import es.optsicom.lib.approx.improvement.AbstractImprovementMethod;
import es.optsicom.lib.approx.improvement.ImprovementMethod;
import es.optsicom.lib.approx.improvement.movement.BasicImprovementMethod;
import es.optsicom.lib.approx.improvement.movement.Mode;
import es.optsicom.lib.experiment.ExecLogger;
import es.optsicom.problem.mdgp.MDGPInstance;
import es.optsicom.problem.mdgp.MDGPSolution;
 
public class LocalImprovementMethod extends
		AbstractImprovementMethod<MDGPSolution, MDGPInstance> {

	private ImprovementMethod<MDGPSolution, MDGPInstance> movementMethod;
	private ImprovementMethod<MDGPSolution, MDGPInstance> swapMethod;

	public LocalImprovementMethod() {
		this.movementMethod = new BasicImprovementMethod<MDGPSolution, MDGPInstance>(new MovementGenerator(), Mode.FIRST);
		this.swapMethod = new BasicImprovementMethod<MDGPSolution, MDGPInstance>(new SwapGenerator(), Mode.FIRST);
	}

	@Override
	public boolean internalImproveSolution(MDGPSolution solution, long duration) {
		long finishTime = System.currentTimeMillis() + duration;
		
		boolean method1 = false;
		try {
			method1 = movementMethod.improveSolution(solution, getRemainigDuration());
		} catch(RuntimeException e) {
			ExecLogger.newSolutionFound(solution);
			method1 = true;
		}
		
		if(finishTime <= System.currentTimeMillis())
			return method1;

		boolean method2;
		try {
			method2 = swapMethod.improveSolution(solution, getRemainigDuration());
		} catch(RuntimeException e) {
			ExecLogger.newSolutionFound(solution);
			method2 = true;
		}
		
		return method1 || method2;
	}

}
