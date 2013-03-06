package org.ilaborie.jgit.flow;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.List;

import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.GitCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.ilaborie.jgit.flow.config.GitFlowConfig;
import org.ilaborie.jgit.flow.repository.GitFlowRepository;

/**
 * Common superclass of all git-flow commands
 * 
 * @param <T>
 *            the command result type
 */
public abstract class GitFlowCommand<T> extends GitCommand<T> {

	/** The git-flow repository. */
	private final GitFlowRepository gitFlowRepo;

	/** Git */
	protected final Git git;

	/**
	 * Instantiates a new git-flow command.
	 * 
	 * @param repository
	 *            the repository
	 */
	protected GitFlowCommand(GitFlowRepository repository) {
		super(repository.getRepository());
		this.gitFlowRepo = repository;
		this.git = Git.wrap(repository.getRepository());
	}

	/**
	 * Gets the git-flow repository.
	 * 
	 * @return the git-flow repository
	 */
	public GitFlowRepository getGitFlowRepository() {
		return gitFlowRepo;
	}
	
	/**
	 * Gets the git-flow configuration.
	 *
	 * @return the configuration
	 */
	public GitFlowConfig getConfig() {
		return this.gitFlowRepo.getGitFlowConfig();
	}

	/**
	 * Checkout branch.
	 * 
	 * @param repository
	 *            the repository
	 * @param branch
	 *            the branch
	 * @return the branch
	 * @throws GitAPIException
	 */
	protected Ref checkoutTo(String branch) throws GitAPIException {
		return this.git.checkout().setName(branch).call();
	}

	/**
	 * Check or create branch.
	 * 
	 * @param repository
	 *            the repository
	 * @param branches
	 *            the branches
	 * @return <code>true</code>, if successful
	 * @throws GitAPIException
	 */
	protected void checkOrCreateBranch(String... branches)
			throws GitAPIException {
		try {
			for (String branch : branches) {
				if (this.repo.getRef(branch) == null) {
					this.createBranch(branch);
				}
			}
		} catch (IOException e) {
			throw new WrongRepositoryStateException("Cannot retrieve branch", e);
		}
	}

	/**
	 * Creates the branch.
	 * 
	 * @param repository
	 *            the repository
	 * @param branch
	 *            the branch
	 * @return the created branch
	 * @return true, if successful
	 * @throws GitAPIException
	 */
	protected Ref createBranch(String branch) throws GitAPIException {
		checkNotNull(branch, "Branch null !");
		CreateBranchCommand branchCreate = this.git.branchCreate();
		return branchCreate.setName(branch).call();
	}

	/**
	 * Contains branch.
	 * 
	 * @param repository
	 *            the repository
	 * @param branch
	 *            the branch
	 * @return <code>true</code>, if successful
	 * @throws WrongRepositoryStateException
	 */
	protected boolean containsBranch(String branch)
			throws WrongRepositoryStateException {
		try {
			return (this.getRepository().getRef(branch) != null);
		} catch (IOException e) {
			throw new WrongRepositoryStateException("Cannot read branch", e);
		}
	}

	/**
	 * Require branch not exists.
	 * 
	 * @param branch
	 *            the branch
	 * @throws WrongRepositoryStateException
	 *             the wrong repository state exception
	 */
	protected void requireBranchNotExists(String branch)
			throws WrongRepositoryStateException {
		if (this.containsBranch(branch))
			throw new WrongRepositoryStateException(String.format(
					"Branch %s already exists !", branch));
	}

	/**
	 * Require branch exists.
	 * 
	 * @param branch
	 *            the branch
	 * @throws WrongRepositoryStateException
	 *             the wrong repository state exception
	 */
	protected void requireBranchExists(String branch)
			throws WrongRepositoryStateException {
		if (!this.containsBranch(branch))
			throw new WrongRepositoryStateException(String.format(
					"Branch %s not exists !", branch));
	}

	/**
	 * Require has head.
	 * 
	 * @param repository
	 *            the repository
	 * @throws WrongRepositoryStateException
	 *             the wrong repository state exception
	 */
	protected void requireHasHead() throws WrongRepositoryStateException {
		try {
			if (this.getRepository().resolve(Constants.HEAD) == null) {
				throw new IllegalStateException("HEAD not found");
			}
		} catch (IOException e) {
			throw new WrongRepositoryStateException("Cannot query HEAD", e);
		}
	}

	/**
	 * Require clean working tree.
	 * 
	 * @param repository
	 *            the repository
	 * @throws GitAPIException
	 *             the wrong repository state exception
	 */
	protected void requireCleanWorkingTree() throws GitAPIException {
		List<DiffEntry> diffs;
		try {
			diffs = this.git.diff().setCached(false).call();
			if (!diffs.isEmpty()) {
				throw new IllegalStateException(
						"fatal: Working tree contains unstaged changes.");
			}
			diffs = this.git.diff().setCached(true).call();
			if (!diffs.isEmpty()) {
				throw new IllegalStateException(
						"fatal: Working tree contains uncommited changes.");
			}
		} catch (NoHeadException nhe) {
			// OK
		} catch (GitAPIException e) {
			throw e;
		}
	}

	/**
	 * Require git flow initialized.
	 * 
	 * @throws WrongRepositoryStateException
	 *             the wrong repository state exception
	 */
	protected void requireGitFlowInitialized()
			throws WrongRepositoryStateException {
		if (!this.getGitFlowRepository().isInitialize()) {
			throw new WrongRepositoryStateException(
					"fatal: Not a gitflow-enabled repository yet");
		}
	}

}
