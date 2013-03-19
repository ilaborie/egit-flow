package org.ilaborie.jgit.flow;

import static com.google.common.base.Preconditions.checkNotNull;

import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.ilaborie.jgit.flow.repository.GitFlowRepository;

/**
 * The git-flow X publish command
 */
public abstract class AbstractTrackCommand extends GitFlowCommand<Ref> {

	/** The remote. */
	private String remote = Constants.DEFAULT_REMOTE_NAME;

	/**
	 * Instantiates a new git-flow X publish command.
	 * 
	 * @param repo
	 *            the repository
	 */
	public AbstractTrackCommand(GitFlowRepository repo) {
		super(repo);
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	protected abstract String getName();

	/**
	 * Gets the remote.
	 * 
	 * @return the remote
	 */
	public String getRemote() {
		return remote;
	}

	/**
	 * Sets the remote.
	 * 
	 * @param remote
	 *            the new remote
	 */
	public void setRemote(String remote) {
		this.remote = remote;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jgit.api.GitCommand#call()
	 */
	@Override
	public Ref call() throws GitAPIException {
		checkNotNull(this.getName());
		checkNotNull(this.remote);
		this.requireGitFlowInitialized();
		this.requireCleanWorkingTree();

		// Local branch
		String prefix = this.getPrefix();
		String localBranch = prefix + this.getName();
		this.requireBranchNotExists(localBranch);

		// Remove branch name
		String remoteBranch = this.getRemote() + '/' + prefix + this.getName();
		this.git.fetch().setRemote(this.getRemote()).call();
		this.requireBranchExists(remoteBranch);

		// Create tracking Branch
		this.checkoutTo(remoteBranch);
		CheckoutCommand command = this.git.checkout().setCreateBranch(true)
				.setName(localBranch);
		return command.call();
	}

	/**
	 * Gets the prefix.
	 * 
	 * @return the prefix
	 */
	protected abstract String getPrefix();
}
