package org.ilaborie.jgit.flow.feature;

import static com.google.common.base.Preconditions.checkNotNull;

import org.eclipse.jgit.api.RebaseCommand;
import org.eclipse.jgit.api.RebaseResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.ilaborie.jgit.flow.GitFlowCommand;
import org.ilaborie.jgit.flow.repository.GitFlowRepository;

/**
 * The git-flow feature rebase command
 */
public class FeatureRebaseCommand extends GitFlowCommand<RebaseResult> {

	/** The feature name */
	private String name;

	/**
	 * Instantiates a new git-flow feature checkout command.
	 * 
	 * @param repo
	 *            the repository
	 */
	public FeatureRebaseCommand(GitFlowRepository repo) {
		super(repo);
	}

	/**
	 * Sets the feature name.
	 * 
	 * @param name
	 *            the feature name
	 * @return the command
	 */
	public FeatureRebaseCommand setName(String name) {
		this.name = checkNotNull(name);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jgit.api.GitCommand#call()
	 */
	@Override
	public RebaseResult call() throws GitAPIException {
		checkNotNull(this.name);
		this.requireGitFlowInitialized();
		this.requireCleanWorkingTree();

		String develop = this.getGitFlowRepository().getDevelopBranch();

		// Branch name
		String prefix = this.getConfig().getFeaturePrefix();
		String branch = prefix + this.name;
		this.requireBranchExists(branch);
		this.checkoutTo(branch);

		// Rebase
		RebaseCommand command = this.git.rebase().setUpstream(develop);
		return command.call();
	}

}
