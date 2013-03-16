package org.ilaborie.jgit.flow.hotfix;

import org.ilaborie.jgit.flow.AbstractListCommand;
import org.ilaborie.jgit.flow.repository.GitFlowRepository;

/**
 * The git-flow hotfix list command
 */
public class HotfixListCommand extends AbstractListCommand {

	/**
	 * Instantiates a new hotfix release list command.
	 * 
	 * @param repo
	 *            the repository
	 */
	public HotfixListCommand(GitFlowRepository repo) {
		super(repo);
	}

	/**
	 * Gets the prefix.
	 *
	 * @return the prefix
	 */
	@Override
	protected String getPrefix() {
		return this.getConfig().getHotfixPrefix();
	}
}
