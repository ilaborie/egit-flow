package org.ilaborie.jgit.flow;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.lib.RepositoryCache;
import org.eclipse.jgit.util.FS;
import org.ilaborie.jgit.flow.feature.FeatureCheckoutCommand;
import org.ilaborie.jgit.flow.feature.FeatureFinishCommand;
import org.ilaborie.jgit.flow.feature.FeatureListCommand;
import org.ilaborie.jgit.flow.feature.FeaturePublishCommand;
import org.ilaborie.jgit.flow.feature.FeatureStartCommand;
import org.ilaborie.jgit.flow.feature.FeatureTrackCommand;
import org.ilaborie.jgit.flow.hotfix.HotfixFinishCommand;
import org.ilaborie.jgit.flow.hotfix.HotfixListCommand;
import org.ilaborie.jgit.flow.hotfix.HotfixStartCommand;
import org.ilaborie.jgit.flow.init.InitCommand;
import org.ilaborie.jgit.flow.release.ReleaseFinishCommand;
import org.ilaborie.jgit.flow.release.ReleaseListCommand;
import org.ilaborie.jgit.flow.release.ReleasePublishCommand;
import org.ilaborie.jgit.flow.release.ReleaseStartCommand;
import org.ilaborie.jgit.flow.release.ReleaseTrackCommand;
import org.ilaborie.jgit.flow.repository.GitFlowRepository;
import org.ilaborie.jgit.flow.support.SupportListCommand;
import org.ilaborie.jgit.flow.support.SupportStartCommand;

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
	 * To git.
	 * 
	 * @return the git
	 */
	public Git toGit() {
		return Git.wrap(this.repo.getRepository());
	}

	/**
	 * Gets the git-flow repository.
	 * 
	 * @return the git-flow repository
	 */
	public GitFlowRepository getRepository() {
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
	 * git-flow feature finish.
	 * 
	 * @return the feature finish command
	 */
	public FeatureFinishCommand featureFinish() {
		return new FeatureFinishCommand(this.repo);
	}

	/**
	 * git-flow feature list.
	 * 
	 * @return the feature list command
	 */
	public FeatureListCommand featureList() {
		return new FeatureListCommand(this.repo);
	}

	/**
	 * git-flow feature checkout.
	 * 
	 * @return the feature checkout command
	 */
	public FeatureCheckoutCommand featureCheckout() {
		return new FeatureCheckoutCommand(this.repo);
	}

	/**
	 * git-flow feature publish.
	 * 
	 * @return the feature publish command
	 */
	public FeaturePublishCommand featurePublish() {
		return new FeaturePublishCommand(this.repo);
	}

	/**
	 * git-flow feature track.
	 * 
	 * @return the feature track command
	 */
	public FeatureTrackCommand featureTrack() {
		return new FeatureTrackCommand(this.repo);
	}

	/**
	 * git-flow release start.
	 * 
	 * @return the release start command
	 */
	public ReleaseStartCommand releaseStart() {
		return new ReleaseStartCommand(this.repo);
	}

	/**
	 * git-flow release list.
	 * 
	 * @return the release list command
	 */
	public ReleaseListCommand releaseList() {
		return new ReleaseListCommand(this.repo);
	}

	/**
	 * git-flow release finish.
	 * 
	 * @return the release finish command
	 */
	public ReleaseFinishCommand releaseFinish() {
		return new ReleaseFinishCommand(this.repo);
	}

	/**
	 * git-flow release publish.
	 * 
	 * @return the release publish command
	 */
	public ReleasePublishCommand releasePublish() {
		return new ReleasePublishCommand(this.repo);
	}

	/**
	 * git-flow release track.
	 * 
	 * @return the release track command
	 */
	public ReleaseTrackCommand releaseTrack() {
		return new ReleaseTrackCommand(this.repo);
	}

	/**
	 * git-flow hotfix start.
	 * 
	 * @return the hotfix start command
	 */
	public HotfixStartCommand hotfixStart() {
		return new HotfixStartCommand(this.repo);
	}

	/**
	 * git-flow hotfix list.
	 * 
	 * @return the hotfix list command
	 */
	public HotfixListCommand hotfixList() {
		return new HotfixListCommand(this.repo);
	}

	/**
	 * git-flow hotfix finish.
	 * 
	 * @return the hotfix finish command
	 */
	public HotfixFinishCommand hotfixFinish() {
		return new HotfixFinishCommand(this.repo);
	}

	/**
	 * git-flow support start.
	 * 
	 * @return the support start command
	 */
	public SupportStartCommand supportStart() {
		return new SupportStartCommand(this.repo);
	}

	/**
	 * git-flow support list.
	 * 
	 * @return the support list command
	 */
	public SupportListCommand supportList() {
		return new SupportListCommand(this.repo);
	}

}
