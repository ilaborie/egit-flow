package org.ilaborie.jgit.flow.hotfix;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.ilaborie.jgit.flow.GitFlow;
import org.ilaborie.jgit.flow.TestUtils;
import org.junit.AfterClass;
import org.junit.Test;

/**
 * git-flow hotfix start test case.
 */
public class HotfixStartTestCase {

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
		GitFlow gitFlow = TestUtils.createGitFlowRepository();

		String hotfix = "v1.0.1";
		Ref ref = gitFlow.hotfixStart().setVersion(hotfix).call();

		assertNotNull(ref);
		assertTrue(ref.getName().endsWith(hotfix));
	}

	/**
	 * Test hotfix start
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test(expected = GitAPIException.class)
	public void testBranchExists() throws Exception {
		GitFlow gitFlow = TestUtils.createGitFlowRepository();

		String hotfix = "v1.0.1";
		gitFlow.hotfixStart().setVersion(hotfix).call();

		gitFlow.hotfixStart().setVersion(hotfix).call();
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

		gitFlow.hotfixStart().call();
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

		String hotfix = "v1.0.1";
		gitFlow.hotfixStart().setVersion(hotfix).call();
	}

}
