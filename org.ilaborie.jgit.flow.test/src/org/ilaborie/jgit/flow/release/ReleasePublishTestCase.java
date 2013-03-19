package org.ilaborie.jgit.flow.release;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.StoredConfig;
import org.ilaborie.jgit.flow.GitFlow;
import org.ilaborie.jgit.flow.TestUtils;
import org.ilaborie.jgit.flow.repository.GitFlowRepository;
import org.junit.AfterClass;
import org.junit.Test;

import com.google.common.io.Files;

/**
 * git-flow feature publish test case.
 */
public class ReleasePublishTestCase {

	/**
	 * Clean temp repository.
	 */
	@AfterClass
	public static void clean() {
		TestUtils.purge();
	}

	/**
	 * Test feature publish
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

		// Add Remote
		File remoteTempDir = TestUtils.createGitFlowRepositoryAlt();
		StoredConfig config = repository.getRepository().getConfig();
		config.setString("remote", Constants.DEFAULT_REMOTE_NAME, "url",
				remoteTempDir.getAbsolutePath());
		config.setString("remote", Constants.DEFAULT_REMOTE_NAME, "fetch",
				"+refs/heads/*:refs/remotes/origin/*");

		// Publish
		Ref ref = gitFlow.releasePublish().setVersion(releaseName).call();

		// check
		assertNotNull(ref);
		assertTrue(ref.getName().endsWith(releaseName));
		
		// FIXME check remote repository
	}

}
