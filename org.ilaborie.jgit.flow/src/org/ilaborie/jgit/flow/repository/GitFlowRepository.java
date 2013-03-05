package org.ilaborie.jgit.flow.repository;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.ilaborie.jgit.flow.config.GitFlowConfig;

;

/**
 * A Repository wrapper for git-flow.
 */
public class GitFlowRepository {

	/** The logger. */
	private static Logger LOGGER = Logger.getLogger(GitFlowRepository.class
			.getSimpleName());

	/** The Constant CONFIG_GITFLOW. */
	static final String CONFIG_GITFLOW = "gitflow";

	/** The Constant CONFIG_GITFLOW_BRANCH. */
	static final String CONFIG_GITFLOW_BRANCH = "branch";

	/** The Constant CONFIG_GITFLOW_BRANCH_MASTER. */
	static final String CONFIG_GITFLOW_BRANCH_MASTER = "master";

	/** The Constant CONFIG_GITFLOW_BRANCH_DEVELOP. */
	static final String CONFIG_GITFLOW_BRANCH_DEVELOP = "develop";

	/** The Constant CONFIG_GITFLOW_PREFIX. */
	static final String CONFIG_GITFLOW_PREFIX = "prefix";

	/** The Constant CONFIG_GITFLOW_PREFIX_FEATURE. */
	static final String CONFIG_GITFLOW_PREFIX_FEATURE = "feature";

	/** The Constant CONFIG_GITFLOW_PREFIX_RELEASE. */
	static final String CONFIG_GITFLOW_PREFIX_RELEASE = "release";

	/** The Constant CONFIG_GITFLOW_PREFIX_HOTFIX. */
	static final String CONFIG_GITFLOW_PREFIX_HOTFIX = "hotfix";

	/** The Constant CONFIG_GITFLOW_PREFIX_SUPPORT. */
	static final String CONFIG_GITFLOW_PREFIX_SUPPORT = "support";

	/** The Constant CONFIG_GITFLOW_PREFIX_VERSION_TAG. */
	static final String CONFIG_GITFLOW_PREFIX_VERSION_TAG = "versiontag";

	/** The wrapped repository. */
	private final Repository repository;

	private GitFlowConfig config;

	public GitFlowRepository(Repository repository) {
		super();
		this.repository = checkNotNull(repository, "No repository !");
	}

	/**
	 * Gets the repository.
	 * 
	 * @return the repository
	 */
	public Repository getRepository() {
		return repository;
	}

	/**
	 * Gets the git flow config.
	 * 
	 * @param repository
	 *            the repository
	 * @return the git flow config
	 * @throws IllegalStateException
	 *             if not initialied
	 */
	public GitFlowConfig getGitFlowConfig() {
		if (config == null) {
			// Lazy loading
			StoredConfig config = this.repository.getConfig();
			String masterBranch = config.getString(CONFIG_GITFLOW,
					CONFIG_GITFLOW_BRANCH, CONFIG_GITFLOW_BRANCH_MASTER);
			String developBranch = config.getString(CONFIG_GITFLOW,
					CONFIG_GITFLOW_BRANCH, CONFIG_GITFLOW_BRANCH_DEVELOP);
			String featurePrefix = config.getString(CONFIG_GITFLOW,
					CONFIG_GITFLOW_PREFIX, CONFIG_GITFLOW_PREFIX_FEATURE);
			String releasePrefix = config.getString(CONFIG_GITFLOW,
					CONFIG_GITFLOW_PREFIX, CONFIG_GITFLOW_PREFIX_RELEASE);
			String hotfixPrefix = config.getString(CONFIG_GITFLOW,
					CONFIG_GITFLOW_PREFIX, CONFIG_GITFLOW_PREFIX_HOTFIX);
			String supportPrefix = config.getString(CONFIG_GITFLOW,
					CONFIG_GITFLOW_PREFIX, CONFIG_GITFLOW_PREFIX_SUPPORT);
			String versionTagPrefix = config.getString(CONFIG_GITFLOW,
					CONFIG_GITFLOW_PREFIX, CONFIG_GITFLOW_PREFIX_VERSION_TAG);

			try {
				GitFlowConfig flowConfig = new GitFlowConfig();
				flowConfig.setMasterBranch(masterBranch);
				flowConfig.setDevelopBranch(developBranch);
				flowConfig.setFeaturePrefix(featurePrefix);
				flowConfig.setReleasePrefix(releasePrefix);
				flowConfig.setHotfixPrefix(hotfixPrefix);
				flowConfig.setSupportPrefix(supportPrefix);
				flowConfig.setVersionTagPrefix(versionTagPrefix);
				this.config = flowConfig;
			} catch (NullPointerException e) {
				throw new IllegalStateException(e);
			}
		}
		return this.config;
	}

	/**
	 * Initialize the repository.
	 * 
	 * @param gitFlowconfig
	 *            the git-flow configuration
	 * @return true, if successful
	 * @throws IllegalStateException
	 *             the illegal state exception
	 * @throws GitAPIException
	 *             the git api exception
	 */
	public boolean init(GitFlowConfig gitFlowconfig)
			throws IllegalStateException, GitAPIException {
		checkNotNull(gitFlowconfig);
		this.requireHasHead();
		this.requireCleanWorkingTree();

		boolean result = true;
		StoredConfig config = repository.getConfig();

		String masterBranch = gitFlowconfig.getMasterBranch();
		String developBranch = gitFlowconfig.getDevelopBranch();

		// Config Branches
		config.setString(CONFIG_GITFLOW, CONFIG_GITFLOW_BRANCH,
				CONFIG_GITFLOW_BRANCH_MASTER, masterBranch);
		config.setString(CONFIG_GITFLOW, CONFIG_GITFLOW_BRANCH,
				CONFIG_GITFLOW_BRANCH_DEVELOP, developBranch);
		// Config Prefix
		config.setString(CONFIG_GITFLOW, CONFIG_GITFLOW_PREFIX,
				CONFIG_GITFLOW_PREFIX_FEATURE, gitFlowconfig.getFeaturePrefix());
		config.setString(CONFIG_GITFLOW, CONFIG_GITFLOW_PREFIX,
				CONFIG_GITFLOW_PREFIX_RELEASE, gitFlowconfig.getReleasePrefix());
		config.setString(CONFIG_GITFLOW, CONFIG_GITFLOW_PREFIX,
				CONFIG_GITFLOW_PREFIX_HOTFIX, gitFlowconfig.getHotfixPrefix());
		config.setString(CONFIG_GITFLOW, CONFIG_GITFLOW_PREFIX,
				CONFIG_GITFLOW_PREFIX_SUPPORT, gitFlowconfig.getSupportPrefix());
		config.setString(CONFIG_GITFLOW, CONFIG_GITFLOW_PREFIX,
				CONFIG_GITFLOW_PREFIX_VERSION_TAG,
				gitFlowconfig.getVersionTagPrefix());

		try {
			// Save updated configuration
			config.save();
			this.config = gitFlowconfig;

			// Check branch master and develop
			result = this.checkOrCreateBranch(repository, masterBranch,
					developBranch);

			// Checkout to Develop
			result &= this.checkoutTo(repository, developBranch);
		} catch (IOException e) {
			LOGGER.severe(String.format("Cannot save git-flow config in %s", e,
					repository));
			result = false;
		}

		if (LOGGER.isLoggable(Level.FINER)) {
			LOGGER.finer(String.format("Initialize %s => %s", repository,
					result));
		}
		return result;
	}
	

	/**
	 * Gets the master branch.
	 *
	 * @return the master branch
	 */
	public String getMasterBranch() {
		return this.getGitFlowConfig().getMasterBranch();
	}

	/**
	 * Gets the develop branch.
	 *
	 * @return the develop branch
	 */
	public String getDevelopBranch() {
		return this.getGitFlowConfig().getDevelopBranch();
	}

	/**
	 * Checkout branch.
	 * 
	 * @param repository
	 *            the repository
	 * @param branch
	 *            the branch
	 * @return true, if successful
	 * @throws GitAPIException
	 */
	public boolean checkoutTo(Repository repository, String branch)
			throws GitAPIException {
		boolean result;
		Ref ref = Git.wrap(repository).checkout().setName(branch).call();
		result = (ref != null);
		if (result && LOGGER.isLoggable(Level.FINEST)) {
			LOGGER.finest(String.format("Checkout branch '%s' into %s",
					ref.getName(), repository));
		}
		return result;
	}

	/**
	 * Check or create branch.
	 * 
	 * @param repository
	 *            the repository
	 * @param branches
	 *            the branches
	 * @return <code>true</code>, if successful
	 * @throws GitAPIException
	 */
	private boolean checkOrCreateBranch(Repository repository,
			String... branches) throws GitAPIException {
		boolean result;
		try {
			result = true;
			for (String branch : branches) {
				if (repository.getRef(branch) == null) {
					result &= this.createBranch(repository, branch);
				}
			}
		} catch (IOException e) {
			LOGGER.severe(String.format("Cannot list branch into %s", e,
					repository));
			result = false;
		}

		return result;
	}

	/**
	 * Creates the branch.
	 * 
	 * @param repository
	 *            the repository
	 * @param branch
	 *            the branch
	 * @return true, if successful
	 * @throws GitAPIException
	 */
	private boolean createBranch(Repository repository, String branch)
			throws GitAPIException {
		checkNotNull(branch, "Branch null !");
		boolean result;
		Ref ref = Git.wrap(this.repository).branchCreate().setName(branch)
				.call();
		LOGGER.finest(String.format("Created branch '%s' into %s",
				ref.getName(), repository));
		result = true;
		return result;
	}

	/**
	 * Checks if is initialize.
	 * 
	 * @return <code>true</code>, if is initialize
	 * @throws IllegalStateException
	 */
	public boolean isInitialize() throws IllegalStateException {
		boolean result = true;
		if (this.config == null) {
			StoredConfig config = repository.getConfig();

			String masterBranch = config.getString(CONFIG_GITFLOW,
					CONFIG_GITFLOW_BRANCH, CONFIG_GITFLOW_BRANCH_MASTER);
			String developBranch = config.getString(CONFIG_GITFLOW,
					CONFIG_GITFLOW_BRANCH, CONFIG_GITFLOW_BRANCH_DEVELOP);
			String featurePrefix = config.getString(CONFIG_GITFLOW,
					CONFIG_GITFLOW_PREFIX, CONFIG_GITFLOW_PREFIX_FEATURE);
			String releasePrefix = config.getString(CONFIG_GITFLOW,
					CONFIG_GITFLOW_PREFIX, CONFIG_GITFLOW_PREFIX_RELEASE);
			String hotfixPrefix = config.getString(CONFIG_GITFLOW,
					CONFIG_GITFLOW_PREFIX, CONFIG_GITFLOW_PREFIX_HOTFIX);
			String supportPrefix = config.getString(CONFIG_GITFLOW,
					CONFIG_GITFLOW_PREFIX, CONFIG_GITFLOW_PREFIX_SUPPORT);

			result = !(isNullOrEmpty(masterBranch)
					|| isNullOrEmpty(developBranch)
					|| !this.containsBranch(repository, masterBranch)
					|| !this.containsBranch(repository, developBranch)
					|| isNullOrEmpty(featurePrefix)
					|| isNullOrEmpty(releasePrefix)
					|| isNullOrEmpty(hotfixPrefix) || isNullOrEmpty(supportPrefix));

			if (result) {
				// Create the configuration
				String versionTagPrefix = config.getString(CONFIG_GITFLOW,
						CONFIG_GITFLOW_PREFIX,
						CONFIG_GITFLOW_PREFIX_VERSION_TAG);

				GitFlowConfig flowConfig = new GitFlowConfig();
				flowConfig.setMasterBranch(masterBranch);
				flowConfig.setDevelopBranch(developBranch);
				flowConfig.setFeaturePrefix(featurePrefix);
				flowConfig.setReleasePrefix(releasePrefix);
				flowConfig.setHotfixPrefix(hotfixPrefix);
				flowConfig.setSupportPrefix(supportPrefix);
				flowConfig.setVersionTagPrefix(versionTagPrefix);
				this.config = flowConfig;
			}
		}

		return result;
	}

	/**
	 * Contains branch.
	 * 
	 * @param repository
	 *            the repository
	 * @param branch
	 *            the branch
	 * @return <code>true</code>, if successful
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private boolean containsBranch(Repository repository, String branch)
			throws IllegalStateException {
		try {
			return (repository.getRef(branch) != null);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Require has head.
	 * 
	 * @param repository
	 *            the repository
	 * @throws IllegalStateException
	 *             the wrong repository state exception
	 */
	private void requireHasHead() throws IllegalStateException {
		try {
			if (this.repository.resolve(Constants.HEAD) == null) {
				throw new IllegalStateException("HEAD not found");
			}
		} catch (IOException e) {
			throw new IllegalStateException("Cannot query HEAD", e);
		}
	}

	/**
	 * Require clean working tree.
	 * 
	 * @param repository
	 *            the repository
	 * @throws IllegalStateException
	 *             the wrong repository state exception
	 * @throws GitAPIException
	 */
	private void requireCleanWorkingTree() throws IllegalStateException, GitAPIException {
		List<DiffEntry> diffs;
		try {
			Git git = Git.wrap(this.repository);
			diffs = git.diff().setCached(false).call();
			if (!diffs.isEmpty()) {
				throw new IllegalStateException(
						"fatal: Working tree contains unstaged changes.");
			}
			diffs = git.diff().setCached(true).call();
			if (!diffs.isEmpty()) {
				throw new IllegalStateException(
						"fatal: Working tree contains uncommited changes.");
			}
		} catch (NoHeadException nhe) {
			// OK
		} catch (GitAPIException e) {
			throw e;
		}
	}

}
