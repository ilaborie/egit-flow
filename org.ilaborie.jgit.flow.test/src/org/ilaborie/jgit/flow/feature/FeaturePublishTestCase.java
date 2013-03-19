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
 * git-flow feature publish test case.
 */
public class FeaturePublishTestCase {

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

		// Create Feature A
		String feature = "feature_A";
		gitFlow.featureStart().setName(feature).call();

		// Work on feature A
		String featureFile = "Feature_test.txt";
		Files.touch(new File(tempDir, featureFile));
		gitFlow.toGit().add().addFilepattern(featureFile).call();
		gitFlow.toGit().commit().setMessage("Add a file").call();

		// Add Remote
		File remoteTempDir = TestUtils.createGitFlowRepositoryAlt();
		StoredConfig config = repository.getRepository().getConfig();
		config.setString("remote", Constants.DEFAULT_REMOTE_NAME, "url",
				remoteTempDir.getAbsolutePath());
		config.setString("remote", Constants.DEFAULT_REMOTE_NAME, "fetch",
				"+refs/heads/*:refs/remotes/origin/*");

		// Publish
		Ref ref = gitFlow.featurePublish().setName(feature).call();

		// check
		assertNotNull(ref);
		assertTrue(ref.getName().endsWith(feature));

		// FIXME check remote repository
	}

}
