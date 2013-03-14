package org.ilaborie.jgit.flow.release;

import org.ilaborie.jgit.flow.AbstractStartCommand;
import org.ilaborie.jgit.flow.repository.GitFlowRepository;

/**
 * The git-flow feature start command
 */
public class ReleaseStartCommand extends AbstractStartCommand {

	/**
	 * Instantiates a new git-flow release start command.
	 * 
	 * @param repo
	 *            the repository
	 */
	public ReleaseStartCommand(GitFlowRepository repo) {
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
