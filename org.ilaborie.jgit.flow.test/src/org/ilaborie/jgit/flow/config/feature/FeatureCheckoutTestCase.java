package org.ilaborie.jgit.flow.config.feature;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.ilaborie.jgit.flow.GitFlow;
import org.ilaborie.jgit.flow.config.TestUtils;
import org.ilaborie.jgit.flow.repository.GitFlowRepository;
import org.junit.AfterClass;
import org.junit.Test;

/**
 * git-flow feature start test case.
 */
public class FeatureCheckoutTestCase {

	/**
	 * Clean temp repository.
	 */
	@AfterClass
	public static void clean() {
		TestUtils.purge();
	}

	/**
	 * Test feature checkout
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void test() throws Exception {
		GitFlow gitFlow = TestUtils.createGitFlowRepository();

		// Create branches
		List<String> branches = Arrays.asList("test0", "test1");
		for (String branch : branches) {
			gitFlow.featureStart().setName(branch).call();
		}

		// Checkout develop
		GitFlowRepository repository = gitFlow.getRepository();
		gitFlow.toGit().checkout().setName(repository.getDevelopBranch())
				.call();

		// feature checkout
		Ref ref;
		for (String branch : branches) {
			ref = gitFlow.featureCheckout().setName(branch).call();

			// Checks
			assertTrue(ref.getName().endsWith(branch));
		}
	}

	/**
	 * Test feature start
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test(expected = GitAPIException.class)
	public void testBranchNotExists() throws Exception {
		GitFlow gitFlow = TestUtils.createGitFlowRepository();

		// Checkout develop
		GitFlowRepository repository = gitFlow.getRepository();
		gitFlow.toGit().checkout().setName(repository.getDevelopBranch())
				.call();

		// feature checkout
		gitFlow.featureCheckout().setName("test0").call();

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

		gitFlow.featureCheckout().call();
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

		gitFlow.featureCheckout().setName("test").call();
	}

}
