package org.ilaborie.jgit.flow.config.repository;

import static org.junit.Assert.*;

import org.eclipse.jgit.lib.Repository;
import org.ilaborie.jgit.flow.GitFlow;
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
		GitFlow gitFlow = TestUtils.createGitFlowRepository();
		GitFlowRepository gitFlowRepository = gitFlow.getRepository();

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
		GitFlow gitFlow = TestUtils.createGitFlowRepository();
		gitFlowRepository = gitFlow.getRepository();
		assertTrue(gitFlowRepository.isInitialize());
	}

}
