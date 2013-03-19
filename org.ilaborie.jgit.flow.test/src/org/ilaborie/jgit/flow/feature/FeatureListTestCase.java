package org.ilaborie.jgit.flow.feature;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.ilaborie.jgit.flow.GitFlow;
import org.ilaborie.jgit.flow.TestUtils;
import org.junit.AfterClass;
import org.junit.Test;

/**
 * git-flow feature list test case.
 */
public class FeatureListTestCase {

	/**
	 * Clean temp repository.
	 */
	@AfterClass
	public static void clean() {
		TestUtils.purge();
	}

	/**
	 * Test feature list
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

		List<String> features = gitFlow.featureList().call();

		assertNotNull(features);
		assertEquals(branches.size(), features.size());
		for (String branch : branches) {
			assertTrue(branches.contains(branch));
		}
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

		gitFlow.featureList().call();
	}

}
