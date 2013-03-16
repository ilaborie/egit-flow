package org.ilaborie.jgit.flow.support;

import org.ilaborie.jgit.flow.AbstractListCommand;
import org.ilaborie.jgit.flow.repository.GitFlowRepository;

/**
 * The git-flow support list command
 */
public class SupportListCommand extends AbstractListCommand {

	/**
	 * Instantiates a new hotfix support list command.
	 * 
	 * @param repo
	 *            the repository
	 */
	public SupportListCommand(GitFlowRepository repo) {
		super(repo);
	}

	/**
	 * Gets the prefix.
	 *
	 * @return the prefix
	 */
	@Override
	protected String getPrefix() {
		return this.getConfig().getSupportPrefix();
	}
}
