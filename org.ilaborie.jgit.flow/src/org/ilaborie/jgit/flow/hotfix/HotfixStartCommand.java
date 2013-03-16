package org.ilaborie.jgit.flow.hotfix;

import org.ilaborie.jgit.flow.AbstractStartCommand;
import org.ilaborie.jgit.flow.repository.GitFlowRepository;

/**
 * The git-flow hotfix start command
 */
public class HotfixStartCommand extends AbstractStartCommand {

	/**
	 * Instantiates a new git-flow hotfix start command.
	 * 
	 * @param repo
	 *            the repository
	 */
	public HotfixStartCommand(GitFlowRepository repo) {
		super(repo);
	}
	
	/* (non-Javadoc)
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
