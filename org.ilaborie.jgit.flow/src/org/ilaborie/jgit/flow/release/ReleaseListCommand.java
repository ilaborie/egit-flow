package org.ilaborie.jgit.flow.release;

import org.ilaborie.jgit.flow.AbstractListCommand;
import org.ilaborie.jgit.flow.repository.GitFlowRepository;

/**
 * The git-flow feature list command
 */
public class ReleaseListCommand extends AbstractListCommand {

	/**
	 * Instantiates a new git-flow release list command.
	 * 
	 * @param repo
	 *            the repository
	 */
	public ReleaseListCommand(GitFlowRepository repo) {
		super(repo);
	}

	/**
	 * Gets the prefix.
	 *
	 * @return the prefix
	 */
	@Override
	protected String getPrefix() {
		return this.getConfig().getReleasePrefix();
	}
}
