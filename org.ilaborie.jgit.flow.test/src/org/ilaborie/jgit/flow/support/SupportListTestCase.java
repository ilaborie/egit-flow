package org.ilaborie.jgit.flow.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.ilaborie.jgit.flow.GitFlow;
import org.ilaborie.jgit.flow.TestUtils;
import org.junit.AfterClass;
import org.junit.Test;

/**
 * git-flow support list test case.
 */
public class SupportListTestCase {

	/**
	 * Clean temp repository.
	 */
	@AfterClass
	public static void clean() {
		TestUtils.purge();
	}

	/**
	 * Test support list
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void test() throws Exception {
		File tempFile = TestUtils.createGitFlowFeatureRelease();
		GitFlow gitFlow = GitFlow.open(tempFile);

		String base = "v1.0.0";
		// Create branches
		List<String> branches = Arrays.asList("v1.1.x", "v1.2.x");
		for (String branch : branches) {
			gitFlow.supportStart().setBase(base).setName(branch).call();
		}

		List<String> supports = gitFlow.supportList().call();

		assertNotNull(supports);
		assertEquals(branches.size(), supports.size());
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

		gitFlow.supportList().call();
	}

}
