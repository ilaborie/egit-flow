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

	/**
	 * Instantiates a new git-flow X start command.
	 * 
	 * @param repo
	 *            the repository
	 */
	public AbstractStartCommand(GitFlowRepository repo) {
		super(repo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jgit.api.GitCommand#call()
	 */
	@Override
	public Ref call() throws GitAPIException {
		checkNotNull(this.getName());
		this.requireGitFlowInitialized();

		// Branch name
		String prefix = this.getPrefix();
		String branch = prefix + this.getName();

		// Checkout to source
		String source = this.getSourceBranch();
		this.checkoutTo(source);
		
		// Check
		this.requireBranchNotExists(branch);

		// Create branch
		this.createBranch(branch);

		// Checkout
		return this.checkoutTo(branch);
	}
	
	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	protected abstract String getName();

	/**
	 * Gets the source branch.
	 * 
	 * @return the source branch
	 */
	protected abstract String getSourceBranch();

	/**
	 * Gets the prefix.
	 * 
	 * @return the prefix
	 */
	protected abstract String getPrefix();
}
