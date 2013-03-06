package org.ilaborie.jgit.flow.config.init;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.jgit.lib.Repository;
import org.ilaborie.jgit.flow.GitFlow;
import org.ilaborie.jgit.flow.config.GitFlowConfig;
import org.ilaborie.jgit.flow.config.TestUtils;
import org.ilaborie.jgit.flow.repository.GitFlowRepository;
import org.junit.AfterClass;
import org.junit.Test;

/**
 * InitCommand test case.
 */
public class InitCommandTestCase {

	/**
	 * Clean temp repository.
	 */
	@AfterClass
	public static void clean() {
		TestUtils.purge();
	}

	/**
	 * Test init with a not clean working set.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test(expected = IllegalStateException.class)
	public void testInitNotCleanWorkingSet() throws Exception {
		GitFlow gitFlow = TestUtils.createGitFlowRepositoryNotClean();

		gitFlow.init().call();
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
		GitFlow gitFlow = GitFlow.wrap(repo);
		
		gitFlow.init().call();
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
		GitFlow gitFlow = GitFlow.wrap(repo);

		gitFlow.init().call();

		String master = gitFlowRepository.getMasterBranch();
		// check has branch master
		assertNotNull(repo.getRef(master));

		String develop = gitFlowRepository.getDevelopBranch();
		// check has branch develop
		assertNotNull(repo.getRef(develop));
		// check branch develop checkout
		assertEquals(develop, repo.getBranch());
	}

	/**
	 * Test init with custom config.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testInitConfig() throws Exception {
		Repository repo = TestUtils.createRepositoryWithACommit();
		GitFlowRepository gitFlowRepository = new GitFlowRepository(repo);
		GitFlow gitFlow = GitFlow.wrap(repo);

		String masterBranch = "prod";
		String developBranch = "intergration";
		// Create config
		GitFlowConfig config = new GitFlowConfig();
		config.setMasterBranch(masterBranch);
		config.setDevelopBranch(developBranch);

		// Init
		gitFlow.init().setConfig(config).call();

		String master = gitFlowRepository.getMasterBranch();
		// check has branch master
		assertEquals(masterBranch, master);
		assertNotNull(repo.getRef(master));

		String develop = gitFlowRepository.getDevelopBranch();
		// check has branch develop
		assertEquals(developBranch, develop);
		assertNotNull(repo.getRef(develop));
		// check branch develop checkout
		assertEquals(develop, repo.getBranch());
	}

}
