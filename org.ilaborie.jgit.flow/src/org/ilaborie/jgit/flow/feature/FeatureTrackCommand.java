package org.ilaborie.jgit.flow.feature;

import org.ilaborie.jgit.flow.AbstractTrackCommand;
import org.ilaborie.jgit.flow.repository.GitFlowRepository;

/**
 * The git-flow feature track command
 */
public class FeatureTrackCommand extends AbstractTrackCommand {

	/** The name */
	private String name;

	/**
	 * Instantiates a new git-flow track start command.
	 * 
	 * @param repo
	 *            the repository
	 */
	public FeatureTrackCommand(GitFlowRepository repo) {
		super(repo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.jgit.flow.AbstractStartCommand#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the name
	 * @return the feature start command
	 */
	public FeatureTrackCommand setName(String name) {
		this.name = name;
		return this;
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
