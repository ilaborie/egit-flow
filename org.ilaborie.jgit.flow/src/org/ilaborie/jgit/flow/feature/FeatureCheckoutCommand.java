package org.ilaborie.jgit.flow.feature;

import static com.google.common.base.Preconditions.checkNotNull;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.ilaborie.jgit.flow.GitFlowCommand;
import org.ilaborie.jgit.flow.repository.GitFlowRepository;

/**
 * The git-flow feature checkout command
 */
public class FeatureCheckoutCommand extends GitFlowCommand<Ref> {

	/** The feature name */
	private String name;

	/**
	 * Instantiates a new git-flow init the command.
	 * 
	 * @param repo
	 *            the repository
	 */
	public FeatureCheckoutCommand(GitFlowRepository repo) {
		super(repo);
	}

	/**
	 * Sets the feature name.
	 * 
	 * @param name
	 *            the feature name
	 * @return the command
	 */
	public FeatureCheckoutCommand setName(String name) {
		this.name = checkNotNull(name);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jgit.api.GitCommand#call()
	 */
	@Override
	public Ref call() throws GitAPIException {
		checkNotNull(this.name);
		this.requireGitFlowInitialized();

		// Branch name
		String prefix = this.getConfig().getFeaturePrefix();
		String branch = prefix + this.name;

		// Check
		this.requireBranchExists(branch);

		// Checkout
		return this.checkoutTo(branch);
	}
}
