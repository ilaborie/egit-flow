package org.ilaborie.jgit.flow.config.repository;

import static org.junit.Assert.*;

import org.eclipse.jgit.lib.Repository;
import org.ilaborie.jgit.flow.config.GitFlowConfig;
import org.ilaborie.jgit.flow.config.TestUtils;
import org.ilaborie.jgit.flow.repository.GitFlowRepository;
import org.junit.AfterClass;
import org.junit.Test;

/**
 * TestCase for GitFlowRepository
 */
public class GitFlowRepositoryTestCase {

	/**
	 * Clean temp repository.
	 */
	@AfterClass
	public static void clean() {
		TestUtils.purge();
	}

	/**
	 * Test create git-flow repository with null.
	 */
	@Test(expected = NullPointerException.class)
	public void testCreateWithNull() {
		new GitFlowRepository(null);
	}

	/**
	 * Standard creation
	 * 
	 * @throws Exception
	 *             on failure
	 */
	@Test
	public void testCreate() throws Exception {
		Repository repo = TestUtils.createEmptyRepository();
		assertNotNull(repo);
		GitFlowRepository gitFlowRepository = new GitFlowRepository(repo);
		assertNotNull(gitFlowRepository);
	}

	/**
	 * Test get config on a not initialized repository
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test(expected = IllegalStateException.class)
	public void testGetConfigNotInitialized() throws Exception {
		Repository repo = TestUtils.createEmptyRepository();
		assertNotNull(repo);
		GitFlowRepository gitFlowRepository = new GitFlowRepository(repo);
		assertNotNull(gitFlowRepository);

		gitFlowRepository.getGitFlowConfig();
	}

	/**
	 * Test get config.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetConfig() throws Exception {
		Repository repo = TestUtils.createGitFlowRepository();
		GitFlowRepository gitFlowRepository = new GitFlowRepository(repo);

		GitFlowConfig config = gitFlowRepository.getGitFlowConfig();
		assertNotNull(config);
	}

	/**
	 * Test is initialize.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testIsInitialize() throws Exception {
		Repository repo;
		GitFlowRepository gitFlowRepository;

		// Not initialized
		repo = TestUtils.createEmptyRepository();
		gitFlowRepository = new GitFlowRepository(repo);
		assertFalse(gitFlowRepository.isInitialize());

		// Initialized
		repo = TestUtils.createGitFlowRepository();
		gitFlowRepository = new GitFlowRepository(repo);
		assertTrue(gitFlowRepository.isInitialize());
	}

	/**
	 * Test init with a not clean working set.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test(expected = IllegalStateException.class)
	public void testInitNotCleanWorkingSet() throws Exception {
		Repository repo = TestUtils.createGitFlowRepositoryNotClean();
		GitFlowRepository gitFlowRepository = new GitFlowRepository(repo);

		GitFlowConfig config = new GitFlowConfig();
		gitFlowRepository.init(config);
	}

	/**
	 * Test init with no head.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test(expected = IllegalStateException.class)
	public void testInitNoHead() throws Exception {
		Repository repo = TestUtils.createEmptyRepository();
		GitFlowRepository gitFlowRepository = new GitFlowRepository(repo);

		GitFlowConfig config = new GitFlowConfig();
		gitFlowRepository.init(config);
	}

	/**
	 * Test init.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testInit() throws Exception {
		Repository repo = TestUtils.createRepositoryWithACommit();
		GitFlowRepository gitFlowRepository = new GitFlowRepository(repo);

		GitFlowConfig config = new GitFlowConfig();
		gitFlowRepository.init(config);

		String master = gitFlowRepository.getMasterBranch();
		// check has branch master
		assertNotNull(repo.getRef(master));

		String develop = gitFlowRepository.getDevelopBranch();
		// check has branch develop
		assertNotNull(repo.getRef(develop));
		// check branch develop checkout
		assertEquals(develop, repo.getBranch());

	}

}
