package org.ilaborie.jgit.flow.release;

import static com.google.common.base.Preconditions.checkNotNull;

import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.TagCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.ilaborie.jgit.flow.AbstractFinishCommand;
import org.ilaborie.jgit.flow.config.GitFlowConfig;
import org.ilaborie.jgit.flow.repository.GitFlowRepository;

import com.google.common.base.Strings;

/**
 * The git-flow release finish command
 */
public class ReleaseFinishCommand extends AbstractFinishCommand {

	/** The tag message. */
	private String tagMessage;

	/** The version. */
	private String version;

	/**
	 * Instantiates a new git-flow release finish command.
	 * 
	 * @param repo
	 *            the repository
	 */
	public ReleaseFinishCommand(GitFlowRepository repo) {
		super(repo);
	}

	/**
	 * Sets the tag message.
	 * 
	 * @param tagMessage
	 *            the new tag message
	 * @return the command
	 */
	public ReleaseFinishCommand setTagMessage(String tagMessage) {
		this.tagMessage = tagMessage;
		return this;
	}

	/**
	 * Gets the tag message.
	 * 
	 * @return the tag message
	 */
	public String getTagMessage() {
		return tagMessage;
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
	 *            the new version
	 * @return the command
	 */
	public ReleaseFinishCommand setVersion(String version) {
		this.version = version;
		return this;
	}

	@Override
	public MergeResult call() throws GitAPIException {
		this.requireGitFlowInitialized();
		checkNotNull(this.getVersion());
		String tag = this.getTagName();
		this.requireTagNotExists(tag);
		
		MergeResult result = super.call();
		if (result.getMergeStatus().isSuccessful()) {
			// Create Tag on Master
			this.checkoutTo(this.getTargetBranch());
			TagCommand tagCmd = this.git.tag().setName(tag);
			if (!Strings.isNullOrEmpty(this.getTagMessage())) {
				tagCmd.setMessage(this.getTagMessage());
			}
			tagCmd.call();

			// Checkout to develop
			this.checkoutTo(this.getConfig().getDevelopBranch());
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.ilaborie.jgit.flow.AbstractFinishCommand#getName()
	 */
	@Override
	protected String getName() {
		return this.getVersion();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.jgit.flow.AbstractFinishCommand#getPrefix()
	 */
	@Override
	protected String getPrefix() {
		return this.getConfig().getReleasePrefix();
	}

	/**
	 * Gets the tag name.
	 * 
	 * @return the tag name
	 */
	private String getTagName() {
		String tagName;
		GitFlowConfig config = this.getConfig();
		String versionTagPrefix = config.getVersionTagPrefix();
		if (!Strings.isNullOrEmpty(versionTagPrefix)) {
			tagName = versionTagPrefix + this.getVersion();
		} else {
			tagName = this.getVersion();
		}
		return tagName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.jgit.flow.AbstractFinishCommand#getTargetBranch()
	 */
	@Override
	protected String getTargetBranch() {
		return this.getConfig().getMasterBranch();
	}

}
