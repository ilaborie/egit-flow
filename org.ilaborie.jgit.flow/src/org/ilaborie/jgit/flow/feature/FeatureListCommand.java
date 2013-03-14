package org.ilaborie.jgit.flow.feature;

import org.ilaborie.jgit.flow.AbstractListCommand;
import org.ilaborie.jgit.flow.repository.GitFlowRepository;

/**
 * The git-flow feature list command
 */
public class FeatureListCommand extends AbstractListCommand {

	/**
	 * Instantiates a new git-flow feature list command.
	 * 
	 * @param repo
	 *            the repository
	 */
	public FeatureListCommand(GitFlowRepository repo) {
		super(repo);
	}

	/* (non-Javadoc)
	 * @see org.ilaborie.jgit.flow.AbstractListCommand#getPrefix()
	 */
	@Override
	protected String getPrefix() {
		return this.getConfig().getFeaturePrefix();
	}
}
