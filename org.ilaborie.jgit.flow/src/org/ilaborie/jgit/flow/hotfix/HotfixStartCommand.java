package org.ilaborie.jgit.flow.hotfix;

import org.ilaborie.jgit.flow.AbstractStartCommand;
import org.ilaborie.jgit.flow.repository.GitFlowRepository;

/**
 * The git-flow hotfix start command
 */
public class HotfixStartCommand extends AbstractStartCommand {

	/** The version. */
	private String version;

	/**
	 * Instantiates a new git-flow hotfix start command.
	 * 
	 * @param repo
	 *            the repository
	 */
	public HotfixStartCommand(GitFlowRepository repo) {
		super(repo);
	}

	/**
	 * Gets the version.
	 * 
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Sets the version.
	 * 
	 * @param version
	 *            the version
	 * @return the hotfix start command
	 */
	public HotfixStartCommand setVersion(String version) {
		this.version = version;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.jgit.flow.AbstractStartCommand#getName()
	 */
	@Override
	protected String getName() {
		return this.getVersion();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.jgit.flow.AbstractStartCommand#getSourceBranch()
	 */
	@Override
	protected String getSourceBranch() {
		return this.getConfig().getMasterBranch();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.jgit.flow.AbstractStartCommand#getPrefix()
	 */
	@Override
	protected String getPrefix() {
		return this.getConfig().getHotfixPrefix();
	}
}
