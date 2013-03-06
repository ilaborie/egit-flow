package org.ilaborie.jgit.flow.init;

import static com.google.common.base.Preconditions.checkNotNull;
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

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.lib.StoredConfig;
import org.ilaborie.jgit.flow.GitFlow;
import org.ilaborie.jgit.flow.GitFlowCommand;
import org.ilaborie.jgit.flow.config.GitFlowConfig;
import org.ilaborie.jgit.flow.repository.GitFlowRepository;

/**
 * The git-flow init command
 */
public class InitCommand extends GitFlowCommand<GitFlow> {

	/** The git-flow config */
	private GitFlowConfig gitFlowConfig;

	/**
	 * Instantiates a new git-flow init the command.
	 * 
	 * @param repo
	 *            the repository
	 */
	public InitCommand(GitFlowRepository repo) {
		super(repo);
		this.gitFlowConfig = new GitFlowConfig();
	}

	/**
	 * Sets the git-flow config.
	 * 
	 * @param config
	 *            the config
	 * @return the init the command
	 */
	public InitCommand setConfig(GitFlowConfig config) {
		this.gitFlowConfig = checkNotNull(config);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jgit.api.GitCommand#call()
	 */
	@Override
	public GitFlow call() throws GitAPIException {
		this.requireHasHead();
		this.requireCleanWorkingTree();

		StoredConfig config = this.getRepository().getConfig();

		String masterBranch = gitFlowConfig.getMasterBranch();
		String developBranch = gitFlowConfig.getDevelopBranch();

		// Config Branches
		config.setString(CONFIG_GITFLOW, CONFIG_GITFLOW_BRANCH,
				CONFIG_GITFLOW_BRANCH_MASTER, masterBranch);
		config.setString(CONFIG_GITFLOW, CONFIG_GITFLOW_BRANCH,
				CONFIG_GITFLOW_BRANCH_DEVELOP, developBranch);
		// Config Prefix
		config.setString(CONFIG_GITFLOW, CONFIG_GITFLOW_PREFIX,
				CONFIG_GITFLOW_PREFIX_FEATURE, gitFlowConfig.getFeaturePrefix());
		config.setString(CONFIG_GITFLOW, CONFIG_GITFLOW_PREFIX,
				CONFIG_GITFLOW_PREFIX_RELEASE, gitFlowConfig.getReleasePrefix());
		config.setString(CONFIG_GITFLOW, CONFIG_GITFLOW_PREFIX,
				CONFIG_GITFLOW_PREFIX_HOTFIX, gitFlowConfig.getHotfixPrefix());
		config.setString(CONFIG_GITFLOW, CONFIG_GITFLOW_PREFIX,
				CONFIG_GITFLOW_PREFIX_SUPPORT, gitFlowConfig.getSupportPrefix());
		config.setString(CONFIG_GITFLOW, CONFIG_GITFLOW_PREFIX,
				CONFIG_GITFLOW_PREFIX_VERSION_TAG,
				gitFlowConfig.getVersionTagPrefix());

		try {
			// Save updated configuration
			config.save();

			// Check branch master and develop
			this.checkOrCreateBranch(masterBranch, developBranch);

			// Checkout to Develop
			this.checkoutTo(developBranch);
		} catch (IOException e) {
			throw new WrongRepositoryStateException(String.format(
					"Cannot save git-flow config in %s", e,
					this.getRepository()), e);
		}
		return GitFlow.wrap(this.getGitFlowRepository());
	}

}
