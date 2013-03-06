package org.ilaborie.jgit.flow.feature;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;

import java.util.List;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.ilaborie.jgit.flow.GitFlowCommand;
import org.ilaborie.jgit.flow.fun.PrefixPredicate;
import org.ilaborie.jgit.flow.fun.PrefixTruncateFunction;
import org.ilaborie.jgit.flow.fun.RefNameFunction;
import org.ilaborie.jgit.flow.repository.GitFlowRepository;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

/**
 * The git-flow feature list command
 */
public class FeatureListCommand extends GitFlowCommand<List<String>> {

	/**
	 * Instantiates a new git-flow init the command.
	 * 
	 * @param repo
	 *            the repository
	 */
	public FeatureListCommand(GitFlowRepository repo) {
		super(repo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jgit.api.GitCommand#call()
	 */
	@Override
	public List<String> call() throws GitAPIException {
		this.requireGitFlowInitialized();

		List<Ref> refs = this.git.branchList().call();
		List<String> branches = Lists.transform(refs, RefNameFunction.INSTANCE);

		Iterable<String> locaBranches = transform(
				filter(branches, PrefixPredicate.LOCAL_PREFIX),
				PrefixTruncateFunction.LOCAL_PREFIX);

		String prefix = this.getConfig().getFeaturePrefix();
		Predicate<String> predicate = new PrefixPredicate(prefix);
		PrefixTruncateFunction function = new PrefixTruncateFunction(prefix);

		return Lists.newArrayList(transform(filter(locaBranches, predicate),
				function));
	}
}
