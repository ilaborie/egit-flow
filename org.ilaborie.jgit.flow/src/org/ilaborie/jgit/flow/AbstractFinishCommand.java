package org.ilaborie.jgit.flow;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;

import org.eclipse.jgit.api.MergeCommand;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.MergeCommand.FastForwardMode;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.lib.Ref;
import org.ilaborie.jgit.flow.GitFlowCommand;
import org.ilaborie.jgit.flow.repository.GitFlowRepository;

/**
 * The git-flow X finish command
 */
public abstract class AbstractFinishCommand extends GitFlowCommand<MergeResult> {

	/** The name */
	private String name;

	/**
	 * Instantiates a new git-flow X finish command.
	 * 
	 * @param repo
	 *            the repository
	 */
	public AbstractFinishCommand(GitFlowRepository repo) {
		super(repo);
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the name
	 * @return the command
	 */
	public AbstractFinishCommand setName(String name) {
		this.name = checkNotNull(name);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jgit.api.GitCommand#call()
	 */
	@Override
	public MergeResult call() throws GitAPIException {
		checkNotNull(this.name);
		this.requireGitFlowInitialized();
		this.requireCleanWorkingTree();

		// Branch name
		String prefix = this.getPrefix();
		String branch = prefix + this.name;

		// Checkout to target
		String target = getTargetBranch();
		this.checkoutTo(target);

		try {
			// Merge branch to develop
			MergeCommand mergeCmd = this.git.merge().setFastForward(
					FastForwardMode.NO_FF);
			Ref featureRef = this.getRepository().getRef(branch);
			mergeCmd.include(featureRef);

			return mergeCmd.call();
		} catch (IOException e) {
			throw new WrongRepositoryStateException("Cannot find branch", e);
		}
	}

	/**
	 * Gets the prefix.
	 * 
	 * @return the prefix
	 */
	protected abstract String getPrefix();

	/**
	 * Gets the target branch.
	 * 
	 * @return the target branch
	 */
	protected abstract String getTargetBranch();
}
