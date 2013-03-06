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
import org.eclipse.jgit.lib.Repository;
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

	/**
	 * Instantiates a new git-flow command.
	 * 
	 * @param repository
	 *            the repository
	 */
	protected GitFlowCommand(GitFlowRepository repository) {
		super(repository.getRepository());
		this.gitFlowRepo = repository;
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
	 * Checkout branch.
	 * 
	 * @param repository
	 *            the repository
	 * @param branch
	 *            the branch
	 * @return true, if successful
	 * @throws GitAPIException
	 */
	protected boolean checkoutTo(Repository repository, String branch)
			throws GitAPIException {
		Ref ref = Git.wrap(repository).checkout().setName(branch).call();
		return (ref != null);
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
	protected void checkOrCreateBranch(Repository repository,
			String... branches) throws GitAPIException {
		try {
			for (String branch : branches) {
				if (repository.getRef(branch) == null) {
					this.createBranch(repository, branch);
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
	 * @return true, if successful
	 * @throws GitAPIException
	 */
	protected void createBranch(Repository repository, String branch)
			throws GitAPIException {
		checkNotNull(branch, "Branch null !");
		CreateBranchCommand branchCreate = Git.wrap(this.getRepository())
				.branchCreate();
		branchCreate.setName(branch).call();
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
	protected boolean containsBranch(Repository repository, String branch)
			throws WrongRepositoryStateException {
		try {
			return (repository.getRef(branch) != null);
		} catch (IOException e) {
			throw new WrongRepositoryStateException("Cannot read branch", e);
		}
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
			Git git = Git.wrap(this.getRepository());
			diffs = git.diff().setCached(false).call();
			if (!diffs.isEmpty()) {
				throw new IllegalStateException(
						"fatal: Working tree contains unstaged changes.");
			}
			diffs = git.diff().setCached(true).call();
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

}
