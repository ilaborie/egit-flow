package org.ilaborie.jgit.flow.feature;

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
 * git-flow feature pull test case.
 */
public class FeaturePullTestCase {

	/**
	 * Clean temp repository.
	 */
	@AfterClass
	public static void clean() {
		TestUtils.purge();
	}

	/**
	 * Test feature pull
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void test() throws Exception {
		File tempDir = TestUtils.createGitFlowRepositoryAlt();
		GitFlow gitFlow = GitFlow.open(tempDir);
		GitFlowRepository repository = gitFlow.getRepository();

		// Add Remote
		File remoteTempDir = TestUtils.createGitFlowRepositoryAlt();
		StoredConfig config = repository.getRepository().getConfig();
		config.setString("remote", Constants.DEFAULT_REMOTE_NAME, "url",
				remoteTempDir.getAbsolutePath());
		config.setString("remote", Constants.DEFAULT_REMOTE_NAME, "fetch",
				"+refs/heads/*:refs/remotes/origin/*");

		// Work On Remote
		GitFlow gitFlowRemote = GitFlow.open(remoteTempDir);
		// Create Feature A
		String feature = "feature_A";
		gitFlowRemote.featureStart().setName(feature).call();

		// Work on feature A
		String featureFile = "Feature_test.txt";
		Files.touch(new File(remoteTempDir, featureFile));
		gitFlowRemote.toGit().add().addFilepattern(featureFile).call();
		gitFlowRemote.toGit().commit().setMessage("Add a file").call();

		// Track
		Ref ref = gitFlow.featurePull().setName(feature).call();

		// check
		assertNotNull(ref);
		assertTrue(ref.getName().endsWith(feature));
	}

}
