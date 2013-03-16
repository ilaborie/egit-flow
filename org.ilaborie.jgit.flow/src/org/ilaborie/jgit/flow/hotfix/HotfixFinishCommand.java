package org.ilaborie.jgit.flow.hotfix;

import static com.google.common.base.Preconditions.checkNotNull;

import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.TagCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.ilaborie.jgit.flow.AbstractFinishCommand;
import org.ilaborie.jgit.flow.repository.GitFlowRepository;

import com.google.common.base.Strings;

/**
 * The git-flow hotfix finish command
 */
public class HotfixFinishCommand extends AbstractFinishCommand {

	private String tagMessage;

	/**
	 * Instantiates a new git-flow hotfix finish command.
	 * 
	 * @param repo
	 *            the repository
	 */
	public HotfixFinishCommand(GitFlowRepository repo) {
		super(repo);
	}

	/**
	 * Sets the tag message.
	 * 
	 * @param tagMessage
	 *            the new tag message
	 */
	public void setTagMessage(String tagMessage) {
		this.tagMessage = tagMessage;
	}

	/**
	 * Gets the tag message.
	 * 
	 * @return the tag message
	 */
	public String getTagMessage() {
		return tagMessage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.jgit.flow.AbstractFinishCommand#call()
	 */
	@Override
	public MergeResult call() throws GitAPIException {
		checkNotNull(this.getName());
		this.requireTagNotExists(this.getName());
		MergeResult result = super.call();
		if (result.getMergeStatus().isSuccessful()) {
			// Create Tag on Master
			this.checkoutTo(this.getTargetBranch());
			TagCommand tagCmd = this.git.tag().setName(this.getName());
			if (!Strings.isNullOrEmpty(this.getTagMessage())) {
				tagCmd.setMessage(tagMessage);
			}
			tagCmd.call();

			// Checkout to develop
			this.checkoutTo(this.getConfig().getDevelopBranch());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ilaborie.jgit.flow.AbstractFinishCommand#getPrefix()
	 */
	@Override
	protected String getPrefix() {
		return this.getConfig().getHotfixPrefix();
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
