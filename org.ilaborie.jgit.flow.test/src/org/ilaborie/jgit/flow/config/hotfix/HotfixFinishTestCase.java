package org.ilaborie.jgit.flow.config.hotfix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.ilaborie.jgit.flow.GitFlow;
import org.ilaborie.jgit.flow.config.TestUtils;
import org.ilaborie.jgit.flow.repository.GitFlowRepository;
import org.junit.AfterClass;
import org.junit.Test;

import com.google.common.io.Files;

/**
 * git-flow hotfix finish test case.
 */
public class HotfixFinishTestCase {

	/**
	 * Clean temp repository.
	 */
	@AfterClass
	public static void clean() {
		TestUtils.purge();
	}

	/**
	 * Test hotfix start
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void test() throws Exception {
		File tempDir = TestUtils.createGitFlowFeatureRelease();
		GitFlow gitFlow = GitFlow.open(tempDir);
		GitFlowRepository repository = gitFlow.getRepository();

		// Create hotfix
		String hotfixName = "v1.0.1";
		gitFlow.hotfixStart().setName(hotfixName).call();

		// Create a commit on hotfix branch
		String hotfixFile = String.format("hotfixNotes-%s.txt", hotfixName);
		Files.touch(new File(tempDir, hotfixFile));
		gitFlow.toGit().add().addFilepattern(hotfixFile).call();
		gitFlow.toGit().commit().setMessage("Add hotfix notes").call();

		// hotfix finish
		MergeResult mergeResult = gitFlow.hotfixFinish().setVersion(hotfixName)
				.call();

		// check merge
		assertTrue(mergeResult.getMergeStatus().isSuccessful());

		// check branch develop checkout
		String developBranch = repository.getDevelopBranch();
		String branch = repository.getRepository().getBranch();
		assertEquals(developBranch, branch);

		// check Tag
		Ref tagRef = repository.getRepository().getTags().get(hotfixName);
		assertNotNull(tagRef);
	}

	/**
	 * Test hotfix start with no branch
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test(expected = NullPointerException.class)
	public void testNoName() throws Exception {
		GitFlow gitFlow = TestUtils.createGitFlowRepository();

		gitFlow.hotfixFinish().call();
	}

	/**
	 * Test with no Git Flow.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test(expected = GitAPIException.class)
	public void testNotGitFlow() throws Exception {
		Repository repo = TestUtils.createRepositoryWithACommit();
		GitFlow gitFlow = GitFlow.wrap(repo);

		String hotfix = "hotfix_A";
		gitFlow.hotfixFinish().setVersion(hotfix).call();
	}

	/**
	 * Test hotfix start
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test(expected = NullPointerException.class)
	public void testBranchNotExists() throws Exception {
		GitFlow gitFlow = TestUtils.createGitFlowRepository();

		// Checkout develop
		GitFlowRepository repository = gitFlow.getRepository();
		gitFlow.toGit().checkout().setName(repository.getDevelopBranch())
				.call();

		// hotfix checkout
		gitFlow.hotfixFinish().setVersion("test0").call();

	}

}
