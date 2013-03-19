package org.ilaborie.jgit.flow.release;

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
 * git-flow release start test case.
 */
public class ReleaseStartTestCase {

	/**
	 * Clean temp repository.
	 */
	@AfterClass
	public static void clean() {
		TestUtils.purge();
	}

	/**
	 * Test release start
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void test() throws Exception {
		GitFlow gitFlow = TestUtils.createGitFlowRepository();

		String release = "v1.0.0";
		Ref ref = gitFlow.releaseStart().setVersion(release).call();

		assertNotNull(ref);
		assertTrue(ref.getName().endsWith(release));
	}

	/**
	 * Test release start
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test(expected = GitAPIException.class)
	public void testBranchExists() throws Exception {
		GitFlow gitFlow = TestUtils.createGitFlowRepository();

		String release = "v1.0.0";
		gitFlow.releaseStart().setVersion(release).call();

		gitFlow.releaseStart().setVersion(release).call();
	}

	/**
	 * Test release start with no branch
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test(expected = NullPointerException.class)
	public void testNoName() throws Exception {
		GitFlow gitFlow = TestUtils.createGitFlowRepository();

		gitFlow.releaseStart().call();
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

		String release = "v1.0.0";
		gitFlow.releaseStart().setVersion(release).call();
	}

}
