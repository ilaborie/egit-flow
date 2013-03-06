package org.ilaborie.jgit.flow.config.feature;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.ilaborie.jgit.flow.GitFlow;
import org.ilaborie.jgit.flow.config.TestUtils;
import org.junit.AfterClass;
import org.junit.Test;

/**
 * git-flow feature start test case.
 */
public class FeatureStartTestCase {

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
		GitFlow gitFlow = TestUtils.createGitFlowRepository();

		String feature = "feature_A";
		Ref ref = gitFlow.featureStart().setName(feature).call();

		assertNotNull(ref);
		assertTrue(ref.getName().endsWith(feature));
	}

	/**
	 * Test feature start
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test(expected = GitAPIException.class)
	public void testBranchExists() throws Exception {
		GitFlow gitFlow = TestUtils.createGitFlowRepository();

		String feature = "feature_A";
		gitFlow.featureStart().setName(feature).call();

		gitFlow.featureStart().setName(feature).call();
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

		gitFlow.featureStart().call();
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
		gitFlow.featureStart().setName(feature).call();
	}

}
