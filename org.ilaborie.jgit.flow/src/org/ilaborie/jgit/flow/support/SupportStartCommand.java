package org.ilaborie.jgit.flow.support;

import static com.google.common.base.Preconditions.checkNotNull;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.ilaborie.jgit.flow.GitFlowCommand;
import org.ilaborie.jgit.flow.repository.GitFlowRepository;

/**
 * The git-flow support start command
 */
public class SupportStartCommand extends GitFlowCommand<Ref> {

	/** The name. */
	private String name;

	/** The base. */
	private String base;

	/**
	 * Instantiates a new git-flow support start command.
	 * 
	 * @param repo
	 *            the repository
	 */
	public SupportStartCommand(GitFlowRepository repo) {
		super(repo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jgit.api.GitCommand#call()
	 */
	@Override
	public Ref call() throws GitAPIException {
		checkNotNull(this.name);
		checkNotNull(this.base);
		this.requireTagExists(this.base);
		this.requireGitFlowInitialized();

		// Branch name
		String prefix = this.getConfig().getSupportPrefix();
		String branch = prefix + this.name;

		// Check
		this.requireBranchNotExists(branch);

		// Checkout to base
		Repository repository = this.getRepository();
		Ref baseRef = repository.getTags().get(base);
		String commit = baseRef.getObjectId().name();
		Ref ref = git.checkout().setStartPoint(commit).setCreateBranch(true)
				.setName(branch).call();
		return ref;
	}

	/**
	 * Gets the base.
	 * 
	 * @return the base
	 */
	public String getBase() {
		return base;
	}

	/**
	 * Sets the base.
	 * 
	 * @param base
	 *            the new base
	 * @return the command
	 */
	public SupportStartCommand setBase(String base) {
		this.base = base;
		return this;
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
	 *            the new name
	 * @return the command
	 */
	public SupportStartCommand setName(String name) {
		this.name = name;
		return this;
	}
}
