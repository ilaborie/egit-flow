package org.ilaborie.jgit.flow.feature;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;

import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.ilaborie.jgit.flow.GitFlowCommand;
import org.ilaborie.jgit.flow.repository.GitFlowRepository;

/**
 * The git-flow feature pull command
 */
public class FeaturePullCommand extends GitFlowCommand<Ref> {

	/** The name */
	private String name;

	/** The remote. */
	private String remote = Constants.DEFAULT_REMOTE_NAME;

	/**
	 * Instantiates a new git-flow pull start command.
	 * 
	 * @param repo
	 *            the repository
	 */
	public FeaturePullCommand(GitFlowRepository repo) {
		super(repo);
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the name
	 * @return the feature start command
	 */
	public FeaturePullCommand setName(String name) {
		this.name = name;
		return this;
	}

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
	 *            the remote
	 * @return the feature pull command
	 */
	public FeaturePullCommand setRemote(String remote) {
		this.remote = remote;
		return this;
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
		String prefix = this.getConfig().getFeaturePrefix();
		String localBranch = prefix + this.getName();

		// Remote branch name
		String remoteBranch = this.getRemote() + '/' + prefix + this.getName();
		this.git.fetch().setRemote(this.getRemote()).call();
		this.requireBranchExists(remoteBranch);

		// Pull command
		Ref result;
		if (this.containsBranch(localBranch)) {
			try {
				if (!this.repo.getBranch().equals(localBranch)) {
					throw new WrongRepositoryStateException(
							"Too avoid accidentally merging different feature branches into each other the pull is aborted");
				}
			} catch (IOException e) {
				throw new WrongRepositoryStateException("Invalid repository", e);
			}
			this.git.pull().call();
			result = this.checkoutTo(localBranch);
		} else {
			this.checkoutTo(remoteBranch);
			CheckoutCommand command = this.git.checkout().setCreateBranch(true)
					.setName(localBranch);
			result = command.call();
		}
		return result;
	}
}