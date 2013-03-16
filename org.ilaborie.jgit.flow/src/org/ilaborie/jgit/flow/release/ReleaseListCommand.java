package org.ilaborie.jgit.flow.release;

import org.ilaborie.jgit.flow.AbstractListCommand;
import org.ilaborie.jgit.flow.config.GitFlowConfig;
import org.ilaborie.jgit.flow.repository.GitFlowRepository;

import com.google.common.base.Strings;

/**
 * The git-flow release list command
 */
public class ReleaseListCommand extends AbstractListCommand {

	/**
	 * Instantiates a new git-flow release list command.
	 * 
	 * @param repo
	 *            the repository
	 */
	public ReleaseListCommand(GitFlowRepository repo) {
		super(repo);
	}

	/**
	 * Gets the prefix.
	 *
	 * @return the prefix
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
