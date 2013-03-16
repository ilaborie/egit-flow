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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.jgit.flow.AbstractStartCommand#getSourceBranch()
	 */
	@Override
	protected String getSourceBranch() {
		return this.getConfig().getDevelopBranch();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.jgit.flow.AbstractStartCommand#getPrefix()
	 */
	@Override
	protected String getPrefix() {
		return this.getConfig().getFeaturePrefix();
	}
}
