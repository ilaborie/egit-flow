package org.ilaborie.jgit.flow.release;

import org.ilaborie.jgit.flow.AbstractStartCommand;
import org.ilaborie.jgit.flow.config.GitFlowConfig;
import org.ilaborie.jgit.flow.repository.GitFlowRepository;

import com.google.common.base.Strings;

/**
 * The git-flow feature start command
 */
public class ReleaseStartCommand extends AbstractStartCommand {

	/** The version */
	private String version;

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
	 * @param version the version
	 * @return the release start command
	 */
	public ReleaseStartCommand setVersion(String version) {
		this.version = version;
		return this;
	}

	/* (non-Javadoc)
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
		return this.getConfig().getDevelopBranch();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.jgit.flow.AbstractStartCommand#getPrefix()
	 */
	@Override
	protected String getPrefix() {
		GitFlowConfig config = this.getConfig();
		String prefix;
		String versionTagPrefix = config.getVersionTagPrefix();
		if (!Strings.isNullOrEmpty(versionTagPrefix)) {
			prefix = config.getReleasePrefix() + versionTagPrefix;
		} else {
			prefix = config.getReleasePrefix();
		}
		return prefix;
	}
}
