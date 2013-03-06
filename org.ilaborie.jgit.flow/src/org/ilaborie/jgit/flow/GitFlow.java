package org.ilaborie.jgit.flow;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.lib.RepositoryCache;
import org.eclipse.jgit.util.FS;
import org.ilaborie.jgit.flow.feature.FeatureListCommand;
import org.ilaborie.jgit.flow.feature.FeatureStartCommand;
import org.ilaborie.jgit.flow.init.InitCommand;
import org.ilaborie.jgit.flow.repository.GitFlowRepository;

/**
 * Offer git-flow commands
 */
public class GitFlow {

	/** The wrapped repository. */
	private final GitFlowRepository repo;

	/**
	 * @param dir
	 *            the repository to open. May be either the GIT_DIR, or the
	 *            working tree directory that contains {@code .git}.
	 * @return a {@link Git} object for the existing git repository
	 * @throws IOException
	 */
	public static GitFlow open(File dir) throws IOException {
		return open(dir, FS.DETECTED);
	}

	/**
	 * @param dir
	 *            the repository to open. May be either the GIT_DIR, or the
	 *            working tree directory that contains {@code .git}.
	 * @param fs
	 *            filesystem abstraction to use when accessing the repository.
	 * @return a {@link Git} object for the existing git repository
	 * @throws IOException
	 */
	public static GitFlow open(File dir, FS fs) throws IOException {
		RepositoryCache.FileKey key;

		key = RepositoryCache.FileKey.lenient(dir, fs);
		return wrap(new RepositoryBuilder().setFS(fs).setGitDir(key.getFile())
				.setMustExist(true).build());
	}

	/**
	 * @param repo
	 *            the git repository this class is interacting with.
	 *            {@code null} is not allowed
	 * @return a {@link Git} object for the existing git repository
	 */
	public static GitFlow wrap(GitFlowRepository repo) {
		return new GitFlow(repo);
	}

	/**
	 * @param repo
	 *            the git repository this class is interacting with.
	 *            {@code null} is not allowed
	 * @return a {@link Git} object for the existing git repository
	 */
	public static GitFlow wrap(Repository repo) {
		return new GitFlow(new GitFlowRepository(repo));
	}

	/**
	 * Instantiates a new git flow.
	 * 
	 * @param repo
	 *            the repository
	 */
	private GitFlow(GitFlowRepository repo) {
		super();
		this.repo = checkNotNull(repo, "Null repository !");
	}

	/**
	 * Gets the git-flow repository.
	 * 
	 * @return the git-flow repository
	 */
	public GitFlowRepository getGitFlowRepository() {
		return repo;
	}

	/**
	 * git-flow init.
	 * 
	 * @return the initialize command
	 */
	public InitCommand init() {
		return new InitCommand(this.repo);
	}
	
	/**
	 * git-flow feature start.
	 *
	 * @return the feature start command
	 */
	public FeatureStartCommand featureStart() {
		return new FeatureStartCommand(this.repo);
	}
	
	/**
	 * git-flow feature list.
	 *
	 * @return the feature list command
	 */
	public FeatureListCommand featureList() {
		return new FeatureListCommand(this.repo);
	}

}
