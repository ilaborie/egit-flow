package org.ilaborie.jgit.flow.feature;

import org.ilaborie.jgit.flow.AbstractFinishCommand;
import org.ilaborie.jgit.flow.repository.GitFlowRepository;

/**
 * The git-flow feature finish command
 */
public class FeatureFinishCommand extends AbstractFinishCommand {

	/**
	 * Instantiates a new git-flow feature finish command.
	 * 
	 * @param repo
	 *            the repository
	 */
	public FeatureFinishCommand(GitFlowRepository repo) {
		super(repo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.jgit.flow.AbstractFinishCommand#getPrefix()
	 */
	@Override
	protected String getPrefix() {
		return this.getConfig().getFeaturePrefix();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.jgit.flow.AbstractFinishCommand#getTargetBranch()
	 */
	@Override
	protected String getTargetBranch() {
		return this.getConfig().getDevelopBranch();
	}

}
