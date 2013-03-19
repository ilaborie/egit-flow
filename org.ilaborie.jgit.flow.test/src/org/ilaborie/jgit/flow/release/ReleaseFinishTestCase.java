package org.ilaborie.jgit.flow.release;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.ilaborie.jgit.flow.GitFlow;
import org.ilaborie.jgit.flow.TestUtils;
import org.ilaborie.jgit.flow.repository.GitFlowRepository;
import org.junit.AfterClass;
import org.junit.Test;

import com.google.common.io.Files;

/**
 * git-flow release finish test case.
 */
public class ReleaseFinishTestCase {

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
		File tempDir = TestUtils.createGitFlowRepositoryAlt();
		GitFlow gitFlow = GitFlow.open(tempDir);
		GitFlowRepository repository = gitFlow.getRepository();

		String develop = repository.getDevelopBranch();

		// Move to develop
		gitFlow.toGit().checkout().setName(develop).call();

		// Work on Develop
		String developFile = "develop_test.txt";
		Files.touch(new File(tempDir, developFile));
		gitFlow.toGit().add().addFilepattern(developFile).call();
		gitFlow.toGit().commit().setMessage("Add a file").call();

		// Create a release
		String releaseName = "v1.0.0";
		gitFlow.releaseStart().setVersion(releaseName).call();

		// Create a commit on release branch
		String releaseFile = String.format("ReleaseNotes-%s.txt", releaseName);
		Files.touch(new File(tempDir, releaseFile));
		gitFlow.toGit().add().addFilepattern(releaseFile).call();
		gitFlow.toGit().commit().setMessage("Add release notes").call();

		// release finish
		MergeResult mergeResult = gitFlow.releaseFinish()
				.setVersion(releaseName).call();

		// check merge
		assertTrue(mergeResult.getMergeStatus().isSuccessful());

		// check branch develop checkout
		assertEquals(develop, repository.getRepository().getBranch());

		// check Tag
		Ref tagRef = repository.getRepository().getTags().get(releaseName);
		assertNotNull(tagRef);
	}

	/**
	 * Test release start
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testWithMessage() throws Exception {
		File tempDir = TestUtils.createGitFlowRepositoryAlt();
		GitFlow gitFlow = GitFlow.open(tempDir);
		GitFlowRepository repository = gitFlow.getRepository();

		String develop = repository.getDevelopBranch();

		// Move to develop
		gitFlow.toGit().checkout().setName(develop).call();

		// Work on Develop
		String developFile = "develop_test.txt";
		Files.touch(new File(tempDir, developFile));
		gitFlow.toGit().add().addFilepattern(developFile).call();
		gitFlow.toGit().commit().setMessage("Add a file").call();

		// Create a release
		String releaseName = "v1.0.0";
		gitFlow.releaseStart().setVersion(releaseName).call();

		// Create a commit on release branch
		String releaseFile = String.format("ReleaseNotes-%s.txt", releaseName);
		Files.touch(new File(tempDir, releaseFile));
		gitFlow.toGit().add().addFilepattern(releaseFile).call();
		gitFlow.toGit().commit().setMessage("Add release notes").call();

		// release finish
		String message = "Lorem ipsum dolor sit amet";
		MergeResult mergeResult = gitFlow.releaseFinish()
				.setVersion(releaseName).setTagMessage(message).call();

		// check merge
		assertTrue(mergeResult.getMergeStatus().isSuccessful());

		// check branch develop checkout
		assertEquals(develop, repository.getRepository().getBranch());

		// check Tag
		Ref tagRef = repository.getRepository().getTags().get(releaseName);
		assertNotNull(tagRef);
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

		gitFlow.releaseFinish().call();
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

		String release = "release_A";
		gitFlow.releaseFinish().setVersion(release).call();
	}

	/**
	 * Test release start
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

		// release checkout
		gitFlow.releaseFinish().setVersion("test0").call();

	}

}
