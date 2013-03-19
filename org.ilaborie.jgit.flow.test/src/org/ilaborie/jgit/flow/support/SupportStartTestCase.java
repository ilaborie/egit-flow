package org.ilaborie.jgit.flow.support;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.ilaborie.jgit.flow.GitFlow;
import org.ilaborie.jgit.flow.TestUtils;
import org.junit.AfterClass;
import org.junit.Test;

/**
 * git-flow support start test case.
 */
public class SupportStartTestCase {

	/**
	 * Clean temp repository.
	 */
	@AfterClass
	public static void clean() {
		TestUtils.purge();
	}

	/**
	 * Test support start
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void test() throws Exception {
		File tempFile = TestUtils.createGitFlowFeatureRelease();
		GitFlow gitFlow = GitFlow.open(tempFile);

		String base = "v1.0.0";
		String support = "v1.0.x";
		Ref ref = gitFlow.supportStart().setName(support).setBase(base).call();

		assertNotNull(ref);
		assertTrue(ref.getName().endsWith(support));
	}

	/**
	 * Test support start
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test(expected = GitAPIException.class)
	public void testBranchExists() throws Exception {
		File tempFile = TestUtils.createGitFlowFeatureRelease();
		GitFlow gitFlow = GitFlow.open(tempFile);

		String base = "v1.0.0";
		String support = "v1.1.x";
		gitFlow.supportStart().setName(support).setBase(base).call();

		gitFlow.supportStart().setName(support).setBase(base).call();
	}

	/**
	 * Test support start with no base
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test(expected = NullPointerException.class)
	public void testNoBase() throws Exception {
		File tempFile = TestUtils.createGitFlowFeatureRelease();
		GitFlow gitFlow = GitFlow.open(tempFile);

		String support = "v1.0.x";

		gitFlow.supportStart().setName(support).call();
	}

	/**
	 * Test support start with no name
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test(expected = NullPointerException.class)
	public void testNoName() throws Exception {
		File tempFile = TestUtils.createGitFlowFeatureRelease();
		GitFlow gitFlow = GitFlow.open(tempFile);

		String base = "v1.0.0";

		gitFlow.supportStart().setBase(base).call();
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

		String base = "v1.0.0";
		String support = "v1.1.x";
		gitFlow.supportStart().setBase(base).setName(support).call();
	}

}
