package org.ilaborie.jgit.flow.repository;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;
import static org.ilaborie.jgit.flow.config.GitFlowConfig.CONFIG_GITFLOW;
import static org.ilaborie.jgit.flow.config.GitFlowConfig.CONFIG_GITFLOW_BRANCH;
import static org.ilaborie.jgit.flow.config.GitFlowConfig.CONFIG_GITFLOW_BRANCH_DEVELOP;
import static org.ilaborie.jgit.flow.config.GitFlowConfig.CONFIG_GITFLOW_BRANCH_MASTER;
import static org.ilaborie.jgit.flow.config.GitFlowConfig.CONFIG_GITFLOW_PREFIX;
import static org.ilaborie.jgit.flow.config.GitFlowConfig.CONFIG_GITFLOW_PREFIX_FEATURE;
import static org.ilaborie.jgit.flow.config.GitFlowConfig.CONFIG_GITFLOW_PREFIX_HOTFIX;
import static org.ilaborie.jgit.flow.config.GitFlowConfig.CONFIG_GITFLOW_PREFIX_RELEASE;
import static org.ilaborie.jgit.flow.config.GitFlowConfig.CONFIG_GITFLOW_PREFIX_SUPPORT;
import static org.ilaborie.jgit.flow.config.GitFlowConfig.CONFIG_GITFLOW_PREFIX_VERSION_TAG;

import java.io.IOException;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.ilaborie.jgit.flow.config.GitFlowConfig;


/**
 * A Repository wrapper for git-flow.
 */
public class GitFlowRepository {

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

}
