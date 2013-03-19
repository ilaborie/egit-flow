package org.ilaborie.jgit.flow.feature;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.ilaborie.jgit.flow.GitFlow;
import org.ilaborie.jgit.flow.TestUtils;
import org.ilaborie.jgit.flow.repository.GitFlowRepository;
import org.junit.AfterClass;
import org.junit.Test;

import com.google.common.io.Files;

/**
 * git-flow feature finish test case.
 */
public class FeatureFinishTestCase {

	/**
	 * Clean temp repository.
	 */
	@AfterClass
	public static void clean() {
		TestUtils.purge();
	}

	/**
	 * Test feature start
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void test() throws Exception {
		File tempDir = TestUtils.createGitFlowRepositoryAlt();
		GitFlow gitFlow = GitFlow.open(tempDir);
		GitFlowRepository repository = gitFlow.getRepository();

		// Create Feature A
		String feature = "feature_A";
		gitFlow.featureStart().setName(feature).call();

		// Work on feature A
		String featureFile = "Feature_test.txt";
		Files.touch(new File(tempDir, featureFile));
		gitFlow.toGit().add().addFilepattern(featureFile).call();
		gitFlow.toGit().commit().setMessage("Add a file").call();

		// Move to develop
		String develop = repository.getDevelopBranch();
		gitFlow.toGit().checkout().setName(develop).call();

		// Create a commit on develop branch
		String developFile = "Develop_test.txt";
		Files.touch(new File(tempDir, developFile));
		gitFlow.toGit().add().addFilepattern(developFile).call();
		gitFlow.toGit().commit().setMessage("Add another file").call();

		// Checkout to feature
		gitFlow.featureCheckout().setName(feature).call();

		// feature finish
		MergeResult mergeResult = gitFlow.featureFinish().setName(feature)
				.call();

		// check merge
		assertTrue(mergeResult.getMergeStatus().isSuccessful());

		// check branch develop checkout
		assertEquals(develop, repository.getRepository().getBranch());
	}

	/**
	 * Test feature start with no branch
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test(expected = NullPointerException.class)
	public void testNoName() throws Exception {
		GitFlow gitFlow = TestUtils.createGitFlowRepository();

		gitFlow.featureFinish().call();
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

		String feature = "feature_A";
		gitFlow.featureFinish().setName(feature).call();
	}

	/**
	 * Test feature start
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

		// feature checkout
		gitFlow.featureFinish().setName("test0").call();

	}

}
