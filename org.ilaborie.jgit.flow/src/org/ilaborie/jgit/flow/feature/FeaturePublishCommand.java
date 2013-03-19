package org.ilaborie.jgit.flow.feature;

import org.ilaborie.jgit.flow.AbstractPublishCommand;
import org.ilaborie.jgit.flow.repository.GitFlowRepository;

/**
 * The git-flow feature publish command
 */
public class FeaturePublishCommand extends AbstractPublishCommand {

	/** The name */
	private String name;

	/**
	 * Instantiates a new git-flow publish start command.
	 * 
	 * @param repo
	 *            the repository
	 */
	public FeaturePublishCommand(GitFlowRepository repo) {
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
	public FeaturePublishCommand setName(String name) {
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
