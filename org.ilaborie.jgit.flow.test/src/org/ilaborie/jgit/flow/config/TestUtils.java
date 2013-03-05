package org.ilaborie.jgit.flow.config;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.NoMessageException;
import org.eclipse.jgit.api.errors.UnmergedPathsException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Repository;
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
	public static Repository createGitFlowRepository() throws GitAPIException,
			IOException {
		Repository repository = createRepositoryWithACommit();

		// Initialize
		GitFlowConfig config = new GitFlowConfig(); // Default config
		new GitFlowRepository(repository).init(config);
		return repository;
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
		Files.touch(new File(tempDir, Constants.GITIGNORE_FILENAME));
		Git.wrap(repository).add().addFilepattern(Constants.GITIGNORE_FILENAME)
				.call();
		Git.wrap(repository).commit().setMessage("Initial commit").call();
	}

	/**
	 * Creates the repository with a commit.
	 * 
	 * @return the repository
	 * @throws GitAPIException
	 * @throws IOException
	 */
	public static Repository createGitFlowRepositoryNotClean()
			throws GitAPIException, IOException {
		File tempDir = Files.createTempDir();
		File repoDir = new File(tempDir, "JGit-Flow");
		dirToPurge.add(tempDir);

		Repository repository = createRepository(repoDir);
		createFirstCommit(tempDir, repository);

		// Add a file into staging
		Files.touch(new File(repoDir, "plop-" + System.currentTimeMillis()
				+ "txt"));
		Git.wrap(repository).add().addFilepattern(Constants.GITIGNORE_FILENAME)
				.call();
		return repository;
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