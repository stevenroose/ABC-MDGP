package be.stevenroose.abcmdgp.abc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.approx.AbstractApproxMethod;
import es.optsicom.lib.approx.constructive.Constructive;
import es.optsicom.lib.approx.improvement.ImprovementMethod;
import es.optsicom.lib.util.Id;
import es.optsicom.lib.util.RandomManager;
import es.optsicom.lib.util.description.DescriptiveHelper;
import es.optsicom.lib.util.description.Properties;
import es.optsicom.problem.mdgp.MDGPInstance;

public class ABC<S extends Solution<I>, I extends Instance> extends
		AbstractApproxMethod<S, I> {

	private Constructive<S, I> constructive;
	private ImprovementMethod<S, I> improvement;
	private NeighbourhoodOperator<S, I> neighbourhood;
	private int iterationsPerformed = 0;
	private int iterations = 1;
	
	// other params
	private int limit;
	private int np;
	private float pl;
	
	// runtime variables
	private long finishTime;
	private List<S> solutions;
	private List<Integer> solutionsChange;
	private Random random;

	public ABC(Constructive<S, I> constructive, ImprovementMethod<S, I> improvement, NeighbourhoodOperator<S, I> neighbourhood,
			int limit, int np, float pl) {
		this.constructive = constructive;
		this.improvement = improvement;
		this.neighbourhood = neighbourhood;
		this.limit = limit;
		this.np = np;
		this.pl = pl;
		this.random = RandomManager.getRandom();
	}

	@Override
	protected void internalCalculateSolution(long duration) {
		this.iterationsPerformed = 0;
		this.finishTime = System.currentTimeMillis() + duration;
		this.constructive.initSolutionCreationByTime(duration);
		
		this.limit = ((MDGPInstance)getInstance()).getM();
		this.np = 20;
		this.solutions = new ArrayList<S>(this.np);
		this.solutionsChange = new ArrayList<Integer>(this.np);
		
		initializationPhase();
		while(hasRemainingTime()) {
			employedBeesPhase();
			onlookerBeesPhase();
			scoutBeesPhase();
			nextIteration();
		}
	}
	
	private void initializationPhase() {
		for(int i = 0 ; i < this.np ; i++) {
			if(!hasRemainingTime())
				break;
			S solution = constructive.createSolution();
			maybeLocalImproveSolution(solution);
			setIfBestSolution(solution);
			solutions.add(solution);
			solutionsChange.add(0);
		}
	}
	
	private void employedBeesPhase() {
		for(int i = 0 ; i < this.np ; i++) {
			if(!hasRemainingTime())
				break;
			S sol = solutions.get(i);
			S nb = generateNeighbour(sol);
			maybeLocalImproveSolution(nb);
			updateSolutionIfBetter(i, nb, sol);
		}
	}
	
	private void onlookerBeesPhase() {
		for(int i = 0 ; i < this.np ; i++) {
			if(!hasRemainingTime())
				break;
			int j = binaryTournament();
			S sj = solutions.get(j);
			S nb = generateNeighbour(sj);
			maybeLocalImproveSolution(nb);
			updateSolutionIfBetter(j, nb, sj);
		}
	}
	
	private void scoutBeesPhase() {
		for(int i = 0 ; i < this.np ; i++) {
			if(!hasRemainingTime())
				break;
			if(solutionsChange.get(i) < limit)
				continue;
			S solution = constructive.createSolution();
			maybeLocalImproveSolution(solution);
			updateSolution(i, solution);
		}
	}
	
	private S generateNeighbour(S solution) {
		return neighbourhood.getNeighbour(solution);
	}
	
	private int binaryTournament() {
		int i1 = random.nextInt(this.np);
		int i2 = random.nextInt(this.np);
		if(solutions.get(i1).isBetterThan(solutions.get(i2)))
			return i1;
		else
			return i2;
	}
	
	private void maybeLocalImproveSolution(S solution) {
		if(!hasRemainingTime())
			return;
		if(random.nextFloat() > this.pl)
			return;
		if(improvement == null)
			return;
		improvement.improveSolution(solution, finishTime - System.currentTimeMillis());
	}
	
	private void updateSolutionIfBetter(int index, S solution, S oldSolution) {
		if(solution.isBetterThan(oldSolution))
			updateSolution(index, solution);
	}
	
	private void updateSolution(int index, S solution) {
		solutions.set(index, solution);
		solutionsChange.set(index, 0);
		setIfBestSolution(solution);
	}
	
	private void nextIteration() {
		for(int i = 0 ; i < this.np ; i++)
			solutionsChange.set(i, solutionsChange.get(i) + 1);
		iterationsPerformed++;
	}

	@Id
	public Constructive<S, I> getConstructive() {
		return constructive;
	}

	public void setConstructive(Constructive<S, I> constructive) {
		this.constructive = constructive;
	}

	@Id
	public ImprovementMethod<S, I> getImprovement() {
		return improvement;
	}

	public void setImprovement(ImprovementMethod<S, I> improvement) {
		this.improvement = improvement;
	}

	public Properties getProperties() {
		return DescriptiveHelper.createProperties(this);
	}

	public I getInstance() {
		return this.constructive.getInstance();
	}

	public void setInstance(I instance) {
		super.setInstance(instance);
		this.constructive.setInstance(instance);
	}

	public void removeInstance() {
		super.removeInstance();
		this.constructive.removeInstance();
	}

	@Id
	public int getMaxConstructions() {
		return this.iterations;
	}

	public void setMaxConstructions(int maxConstructions) {
		this.iterations = maxConstructions;
	}
	
	public long getRemainingTime() {
		return finishTime - System.currentTimeMillis();
	}
	
	public boolean hasRemainingTime() {
		return getRemainingTime() > 0;
	}

}
