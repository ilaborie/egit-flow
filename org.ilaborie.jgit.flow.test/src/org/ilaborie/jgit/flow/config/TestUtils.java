package org.ilaborie.jgit.flow.config;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.ilaborie.jgit.flow.GitFlow;
import org.ilaborie.jgit.flow.repository.GitFlowRepository;

import com.google.common.collect.Lists;
import com.google.common.io.Files;

/**
 * Utils for tests.
 */
public final class TestUtils {

	/** Store directory to purge. */
	private static List<File> dirToPurge = Lists.newArrayList();

	/**
	 * Omit creation
	 */
	private TestUtils() {
		super();
	}

	/**
	 * Creates an repository.
	 * 
	 * @return the created repository
	 * @throws GitAPIException
	 *             if creation failed
	 */
	public static Repository createEmptyRepository() throws GitAPIException {
		File tempDir = Files.createTempDir();
		File repoDir = new File(tempDir, "JGit-Flow");
		dirToPurge.add(tempDir);

		// Create a repository
		Repository repository = createRepository(repoDir);

		return repository;
	}

	/**
	 * Creates the repository.
	 * 
	 * @param repoDir
	 *            the repository directory
	 * @return the repository
	 * @throws GitAPIException
	 */
	private static Repository createRepository(File repoDir)
			throws GitAPIException {
		Git git = Git.init().setDirectory(repoDir).setBare(false).call();
		Repository repository = git.getRepository();
		return repository;
	}

	/**
	 * Creates an initialize git-flow repository.
	 * 
	 * @return the repository
	 * @throws GitAPIException
	 * @throws IOException
	 */
	public static GitFlow createGitFlowRepository() throws GitAPIException,
			IOException {
		Repository repository = createRepositoryWithACommit();

		// Initialize
		return GitFlow.wrap(repository).init().call();
	}

	/**
	 * Creates the git flow repository (alternate).
	 * 
	 * @return the folder
	 * @throws GitAPIException
	 *             the git api exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static File createGitFlowRepositoryAlt() throws GitAPIException,
			IOException {
		File tempDir = Files.createTempDir();
		File repoDir = new File(tempDir, "JGit-Flow");
		dirToPurge.add(tempDir);

		Repository repository = createRepository(repoDir);

		// Create a first commit
		createFirstCommit(repoDir, repository);

		// Initialize
		GitFlow.wrap(repository).init().call();

		return repoDir;
	}

	/**
	 * Creates the git flow repository with a release.
	 * 
	 * @return the git flow
	 * @throws GitAPIException
	 *             the git api exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static File createGitFlowFeatureRelease() throws GitAPIException,
			IOException {
		File tempDir = createGitFlowRepositoryAlt();
		GitFlow gitFlow = GitFlow.open(tempDir);
		GitFlowRepository repository = gitFlow.getRepository();

		String develop = repository.getDevelopBranch();

		// Move to develop
		gitFlow.toGit().checkout().setName(develop).call();

		// Work on Develop
		String developFile = "develop_test.txt";
		Files.touch(new File(tempDir, developFile));
		gitFlow.toGit().add().addFilepattern(developFile).call();
		gitFlow.toGit().commit().setMessage("Add a file").call();

		// Work on Feature A
		String featureName = "FeatureA";
		String featureFile = featureName + ".txt";
		gitFlow.featureStart().setName(featureName).call();
		Files.touch(new File(tempDir, featureFile));
		gitFlow.toGit().add().addFilepattern(featureFile).call();
		gitFlow.toGit().commit().setMessage("Implements " + featureName).call();

		// Finish Feature A
		gitFlow.featureFinish().setName(featureName).call();

		// Create a release
		String releaseName = "v1.0.0";
		gitFlow.releaseStart().setName(releaseName).call();

		// Create a commit on release branch
		String releaseFile = String.format("ReleaseNotes-%s.txt", releaseName);
		Files.touch(new File(tempDir, releaseFile));
		gitFlow.toGit().add().addFilepattern(releaseFile).call();
		gitFlow.toGit().commit().setMessage("Add release notes").call();

		// release finish
		gitFlow.releaseFinish().setVersion(releaseName).call();

		return tempDir;
	}

	/**
	 * Creates the repository with a commit.
	 * 
	 * @return the repository
	 * @throws GitAPIException
	 * @throws IOException
	 */
	public static Repository createRepositoryWithACommit()
			throws GitAPIException, IOException {
		File tempDir = Files.createTempDir();
		File repoDir = new File(tempDir, "JGit-Flow");
		dirToPurge.add(tempDir);

		Repository repository = createRepository(repoDir);

		// Create a first commit
		createFirstCommit(repoDir, repository);
		return repository;
	}

	/**
	 * Creates the first commit.
	 * 
	 * @param tempDir
	 *            the temp dir
	 * @param repository
	 *            the repository
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws GitAPIException
	 *             the git api exception
	 */
	private static void createFirstCommit(File tempDir, Repository repository)
			throws IOException, GitAPIException {
		Git.wrap(repository).commit().setMessage("Initial commit").call();
	}

	/**
	 * Creates the repository with a commit.
	 * 
	 * @return the repository
	 * @throws GitAPIException
	 * @throws IOException
	 */
	public static GitFlow createGitFlowRepositoryNotClean()
			throws GitAPIException, IOException {
		File tempDir = Files.createTempDir();
		File repoDir = new File(tempDir, "JGit-Flow");
		dirToPurge.add(tempDir);

		Repository repository = createRepository(repoDir);
		createFirstCommit(tempDir, repository);

		// Add a file into staging
		String file = String.format("plop-%s.txt", System.currentTimeMillis());
		Files.touch(new File(repoDir, file));
		return GitFlow.wrap(repository);
	}

	/**
	 * Creates the repository with a commit.
	 * 
	 * @return the repository
	 * @throws GitAPIException
	 * @throws IOException
	 */
	public static GitFlow createGitFlowRepositoryNotCleanAlt()
			throws GitAPIException, IOException {
		File tempDir = Files.createTempDir();
		File repoDir = new File(tempDir, "JGit-Flow");
		dirToPurge.add(tempDir);

		Repository repository = createRepository(repoDir);
		createFirstCommit(tempDir, repository);

		// Add a file into staging
		String file = String.format("plop-%s.txt", System.currentTimeMillis());
		Files.touch(new File(repoDir, file));
		Git.wrap(repository).add().addFilepattern(file).call();
		return GitFlow.wrap(repository);
	}

	/**
	 * Purge directory
	 */
	public static void purge() {
		for (Iterator<File> iterator = dirToPurge.iterator(); iterator
				.hasNext();) {
			if (deleteFile(iterator.next())) {
				iterator.remove();
			}
		}
	}

	/**
	 * Delete a file.
	 * 
	 * @param file
	 *            the file
	 * @return <code>true</code>, if successful
	 */
	private static boolean deleteFile(File file) {
		assert file != null;
		if (file.isDirectory()) {
			for (File child : file.listFiles()) {
				deleteFile(child);
			}
		}
		return file.delete();
	}

}
