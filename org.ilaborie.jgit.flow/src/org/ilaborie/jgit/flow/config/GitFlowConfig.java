package org.ilaborie.jgit.flow.config;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.nullToEmpty;

/**
 * A POJO for git-flow configuration.
 */
public class GitFlowConfig {
	/** The Constant CONFIG_GITFLOW. */
	public static final String CONFIG_GITFLOW = "gitflow";

	/** The Constant CONFIG_GITFLOW_BRANCH. */
	public static final String CONFIG_GITFLOW_BRANCH = "branch";

	/** The Constant CONFIG_GITFLOW_BRANCH_MASTER. */
	public static final String CONFIG_GITFLOW_BRANCH_MASTER = "master";

	/** The Constant CONFIG_GITFLOW_BRANCH_DEVELOP. */
	public static final String CONFIG_GITFLOW_BRANCH_DEVELOP = "develop";

	/** The Constant CONFIG_GITFLOW_PREFIX. */
	public static final String CONFIG_GITFLOW_PREFIX = "prefix";

	/** The Constant CONFIG_GITFLOW_PREFIX_FEATURE. */
	public static final String CONFIG_GITFLOW_PREFIX_FEATURE = "feature";

	/** The Constant CONFIG_GITFLOW_PREFIX_RELEASE. */
	public static final String CONFIG_GITFLOW_PREFIX_RELEASE = "release";

	/** The Constant CONFIG_GITFLOW_PREFIX_HOTFIX. */
	public static final String CONFIG_GITFLOW_PREFIX_HOTFIX = "hotfix";

	/** The Constant CONFIG_GITFLOW_PREFIX_SUPPORT. */
	public static final String CONFIG_GITFLOW_PREFIX_SUPPORT = "support";

	/** The Constant CONFIG_GITFLOW_PREFIX_VERSION_TAG. */
	public static final String CONFIG_GITFLOW_PREFIX_VERSION_TAG = "versiontag";

	/** The master branch. */
	private String masterBranch = "master";

	/** The develop branch. */
	private String developBranch = "develop";

	/** The feature prefix. */
	private String featurePrefix = "feature/";

	/** The release prefix. */
	private String releasePrefix = "release/";

	/** The hotfix prefix. */
	private String hotfixPrefix = "hotfix/";

	/** The support prefix. */
	private String supportPrefix = "support/";

	/** The version tag prefix. */
	private String versionTagPrefix = "";

	/**
	 * Instantiates a new git flow config.
	 */
	public GitFlowConfig() {
		super();
	}

	/**
	 * Gets the master branch.
	 * 
	 * @return the master branch
	 */
	public String getMasterBranch() {
		return masterBranch;
	}

	/**
	 * Sets the master branch.
	 * 
	 * @param masterBranch
	 *            the new master branch
	 */
	public void setMasterBranch(String masterBranch) {
		this.masterBranch = checkNotNull(masterBranch,
				"Master branch name should be defined");
	}

	/**
	 * Gets the develop branch.
	 * 
	 * @return the develop branch
	 */
	public String getDevelopBranch() {
		return developBranch;
	}

	/**
	 * Sets the develop branch.
	 * 
	 * @param developBranch
	 *            the new develop branch
	 */
	public void setDevelopBranch(String developBranch) {
		this.developBranch = checkNotNull(developBranch,
				"Develop branch name should be defined");
	}

	/**
	 * Gets the feature prefix.
	 * 
	 * @return the feature prefix
	 */
	public String getFeaturePrefix() {
		return featurePrefix;
	}

	/**
	 * Sets the feature prefix.
	 * 
	 * @param featurePrefix
	 *            the new feature prefix
	 */
	public void setFeaturePrefix(String featurePrefix) {
		this.featurePrefix = checkNotNull(featurePrefix,
				"Features branchs prefix should be defined");
	}

	/**
	 * Gets the release prefix.
	 * 
	 * @return the release prefix
	 */
	public String getReleasePrefix() {
		return releasePrefix;
	}

	/**
	 * Sets the release prefix.
	 * 
	 * @param releasePrefix
	 *            the new release prefix
	 */
	public void setReleasePrefix(String releasePrefix) {
		this.releasePrefix = checkNotNull(releasePrefix,
				"Releases branchs prefix should be defined");
	}

	/**
	 * Gets the hotfix prefix.
	 * 
	 * @return the hotfix prefix
	 */
	public String getHotfixPrefix() {
		return hotfixPrefix;
	}

	/**
	 * Sets the hotfix prefix.
	 * 
	 * @param hotfixPrefix
	 *            the new hotfix prefix
	 */
	public void setHotfixPrefix(String hotfixPrefix) {
		this.hotfixPrefix = checkNotNull(hotfixPrefix,
				"Hot Fixes branchs prefix should be defined");
	}

	/**
	 * Gets the support prefix.
	 * 
	 * @return the support prefix
	 */
	public String getSupportPrefix() {
		return supportPrefix;
	}

	/**
	 * Sets the support prefix.
	 * 
	 * @param supportPrefix
	 *            the new support prefix
	 */
	public void setSupportPrefix(String supportPrefix) {
		this.supportPrefix = checkNotNull(supportPrefix,
				"Supports branchs prefix should be defined");
	}

	/**
	 * Gets the version tag prefix.
	 * 
	 * @return the version tag prefix
	 */
	public String getVersionTagPrefix() {
		return versionTagPrefix;
	}

	/**
	 * Sets the version tag prefix.
	 * 
	 * @param versionTagPrefix
	 *            the new version tag prefix
	 */
	public void setVersionTagPrefix(String versionTagPrefix) {
		this.versionTagPrefix = nullToEmpty(versionTagPrefix);
	}

}