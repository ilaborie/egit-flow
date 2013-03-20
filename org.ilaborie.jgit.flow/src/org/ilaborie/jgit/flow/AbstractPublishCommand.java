package org.ilaborie.jgit.flow;

import static com.google.common.base.Preconditions.checkNotNull;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.transport.RefSpec;
import org.ilaborie.jgit.flow.GitFlowCommand;
import org.ilaborie.jgit.flow.repository.GitFlowRepository;

/**
 * The git-flow X publish command
 */
public abstract class AbstractPublishCommand extends GitFlowCommand<Ref> {

	/** The remote. */
	private String remote = Constants.DEFAULT_REMOTE_NAME;

	/**
	 * Instantiates a new git-flow X publish command.
	 * 
	 * @param repo
	 *            the repository
	 */
	public AbstractPublishCommand(GitFlowRepository repo) {
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
		this.requireBranchExists(localBranch);

		// Remote branch name
		String remoteBranch = this.getRemote() + '/' + prefix + this.getName();
		this.requireBranchNotExists(remoteBranch);

		// Create remote branch
		String refSpec = String.format("%1$s:refs/heads/%1$s", localBranch);
		RefSpec specs = new RefSpec(refSpec);
		this.git.push().setRemote(this.getRemote()).setRefSpecs(specs).call();
		this.git.fetch().setRemote(this.getRemote()).call();

		// Config
		StoredConfig config = this.getRepository().getConfig();
		config.setString("remote", localBranch, "remote", this.getRemote());
		config.setString("remote", localBranch, "merge", "refs/heads/"
				+ localBranch);

		// Checkout
		return this.checkoutTo(localBranch);
	}

	/**
	 * Gets the prefix.
	 * 
	 * @return the prefix
	 */
	protected abstract String getPrefix();
}
