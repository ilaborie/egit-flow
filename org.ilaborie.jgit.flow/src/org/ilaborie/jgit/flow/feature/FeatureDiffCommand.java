package org.ilaborie.jgit.flow.feature;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.List;

import org.eclipse.jgit.api.DiffCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.ilaborie.jgit.flow.GitFlowCommand;
import org.ilaborie.jgit.flow.repository.GitFlowRepository;

/**
 * The git-flow feature diff command
 */
public class FeatureDiffCommand extends GitFlowCommand<List<DiffEntry>> {

	/** The feature name */
	private String name;

	/**
	 * Instantiates a new git-flow feature diff command.
	 * 
	 * @param repo
	 *            the repository
	 */
	public FeatureDiffCommand(GitFlowRepository repo) {
		super(repo);
	}

	/**
	 * Sets the feature name.
	 * 
	 * @param name
	 *            the feature name
	 * @return the command
	 */
	public FeatureDiffCommand setName(String name) {
		this.name = checkNotNull(name);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jgit.api.GitCommand#call()
	 */
	@Override
	public List<DiffEntry> call() throws GitAPIException {
		checkNotNull(this.name);
		this.requireGitFlowInitialized();

		String develop = this.getGitFlowRepository().getDevelopBranch();

		// Branch name
		String prefix = this.getConfig().getFeaturePrefix();
		String branch = prefix + this.name;

		// Check
		this.requireBranchExists(branch);

		// Diff
		AbstractTreeIterator newTree = this.getTreeIterator(develop);
		AbstractTreeIterator oldTree = this.getTreeIterator(branch);
		DiffCommand command = this.git.diff().setNewTree(newTree)
				.setOldTree(oldTree);
		return command.call();
	}

	/**
	 * Gets the tree iterator.
	 * 
	 * @param name
	 *            the name
	 * @return the tree iterator
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private AbstractTreeIterator getTreeIterator(String name)
			throws GitAPIException {
		try {
			Repository repository = this.getRepository();
			final ObjectId id = repository.resolve(name);
			if (id == null)
				throw new IllegalArgumentException(name);
			final CanonicalTreeParser p = new CanonicalTreeParser();
			final ObjectReader or = repository.newObjectReader();
			try {
				p.reset(or, new RevWalk(repository).parseTree(id));
				return p;
			} finally {
				or.release();
			}
		} catch (IOException e) {
			throw new WrongRepositoryStateException(
					"Cannot retrieve tree iterator", e);
		}
	}
}
