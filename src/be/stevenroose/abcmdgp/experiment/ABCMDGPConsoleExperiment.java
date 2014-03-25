package be.stevenroose.abcmdgp.experiment;

import be.stevenroose.abcmdgp.ABC_MDGP;
import be.stevenroose.abcmdgp.abc.NeighbourhoodOperator;
import be.stevenroose.abcmdgp.abc.StochasticNO;
import be.stevenroose.abcmdgp.mdgp.NO1;
import be.stevenroose.abcmdgp.mdgp.NO2a;
import be.stevenroose.abcmdgp.mdgp.NO2b;
import be.stevenroose.abcmdgp.mdgp.NO3;

import com.sun.star.lang.IllegalArgumentException;

import es.optsicom.lib.experiment.ConsoleExperiment;
import es.optsicom.problem.mdgp.MDGPInstance;
import es.optsicom.problem.mdgp.MDGPInstanceLoader;
import es.optsicom.problem.mdgp.MDGPSolution;
import es.optsicom.problem.mdgp.ma.LSGA;
import es.optsicom.problem.mdgp.method.C1_LCW_I_T;
import es.optsicom.problem.mdgp.method.R_LCW;
import es.optsicom.problem.mdgp.method.SO;

public class ABCMDGPConsoleExperiment {
	
	/**
	 * Use this method just like the main method of mdgp_jors_2011.jar.
	 */
	public static void main(String[] args) {
		ConsoleExperiment expExec = new ConsoleExperiment(
				"abc_mdgp.jar",
				"http://www.optsicom.es/mdgp/instance_format.txt",
				"http://www.optsicom.es/mdgp",
				"Maximally Diverse Grouping Problem with Artificial Bee Colony Algorithm",
				new MDGPInstanceLoader(),
				"The solution will be represented as an integer array. Each array position represents the group number of the element corresponding to position index.");

		expExec.putMethod("SO", new SO());
		expExec.putMethod("T-LCW", new C1_LCW_I_T());
		expExec.putMethod("LCW", new R_LCW());
		expExec.putMethod("LSGA", new LSGA());
		expExec.putMethod("ABC-NO1", new ABC_MDGP(new NO1(0.1)));
		expExec.putMethod("ABC-NO2b", new ABC_MDGP(new NO2b(0.1)));
		expExec.putMethod("ABC-NO3", new ABC_MDGP(new NO3(0.1)));
		expExec.putMethod("ABC-NO1NO2b", new ABC_MDGP(combo(new NO1(0.1), new NO2b(0.1))));
		expExec.putMethod("ABC-NO1NO3", new ABC_MDGP(combo(new NO1(0.1), new NO3(0.1))));
		expExec.putMethod("ABC-NO2bNO3", new ABC_MDGP(combo(new NO2b(0.1), new NO3(0.1))));
		expExec.putMethod("ABC-NO1NO2bNO3", new ABC_MDGP(combo(new NO1(0.1), new NO2b(0.1), new NO3(0.1))));
		expExec.putMethod("ABC-NO2a", new ABC_MDGP(new NO2a(0.1)));
		expExec.putMethod("ABC-NO1NO2a", new ABC_MDGP(combo(new NO1(0.1), new NO2a(0.1))));
		expExec.putMethod("ABC-NO2aNO3", new ABC_MDGP(combo(new NO2a(0.1), new NO3(0.1))));
		expExec.putMethod("ABC-NO1NO2aNO3", new ABC_MDGP(combo(new NO1(0.1), new NO2a(0.1), new NO3(0.1))));
		
		expExec.execMethodWithArgs(args);
	}
	
	@SafeVarargs
	static private NeighbourhoodOperator<MDGPSolution, MDGPInstance> 
	combo(NeighbourhoodOperator<MDGPSolution, MDGPInstance>... elems) {
		try {
			return new StochasticNO<MDGPSolution, MDGPInstance>(elems);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return null;
	}
}