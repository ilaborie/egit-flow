package org.ilaborie.jgit.flow.feature;

import org.ilaborie.jgit.flow.AbstractStartCommand;
import org.ilaborie.jgit.flow.repository.GitFlowRepository;

/**
 * The git-flow feature start command
 */
public class FeatureStartCommand extends AbstractStartCommand {

	/**
	 * Instantiates a new git-flow feature start command.
	 * 
	 * @param repo
	 *            the repository
	 */
	public FeatureStartCommand(GitFlowRepository repo) {
		super(repo);
	}

	/**
	 * Gets the prefix.
	 *
	 * @return the prefix
	 */
	@Override
	protected String getPrefix() {
		return this.getConfig().getFeaturePrefix();
	}
}
