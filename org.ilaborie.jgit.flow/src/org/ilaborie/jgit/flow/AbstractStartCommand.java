package org.ilaborie.jgit.flow;

import static com.google.common.base.Preconditions.checkNotNull;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.ilaborie.jgit.flow.GitFlowCommand;
import org.ilaborie.jgit.flow.repository.GitFlowRepository;

/**
 * The git-flow X start command
 */
public abstract class AbstractStartCommand extends GitFlowCommand<Ref> {

	/** The feature name */
	private String name;

	/**
	 * Instantiates a new git-flow X start command.
	 * 
	 * @param repo
	 *            the repository
	 */
	public AbstractStartCommand(GitFlowRepository repo) {
		super(repo);
	}

	/**
	 * Sets the feature name.
	 * 
	 * @param name
	 *            the feature name
	 * @return the command
	 */
	public AbstractStartCommand setName(String name) {
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
		String prefix = this.getPrefix();
		String branch = prefix + this.name;

		// Check
		this.requireBranchNotExists(branch);

		// Create branch
		this.createBranch(branch);

		// Checkout
		return this.checkoutTo(branch);
	}


	/**
	 * Gets the prefix.
	 *
	 * @return the prefix
	 */
	protected abstract String getPrefix() ;
}
