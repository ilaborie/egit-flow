package org.ilaborie.jgit.flow.feature;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.List;

import org.eclipse.jgit.diff.DiffEntry;
import org.ilaborie.jgit.flow.GitFlow;
import org.ilaborie.jgit.flow.TestUtils;
import org.ilaborie.jgit.flow.repository.GitFlowRepository;
import org.junit.AfterClass;
import org.junit.Test;

import com.google.common.io.Files;

/**
 * git-flow feature finish test case.
 */
public class FeatureDiffTestCase {

	/**
	 * Clean temp repository.
	 */
	@AfterClass
	public static void clean() {
		TestUtils.purge();
	}

	/**
	 * Test feature diff
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

		// Move to develop
		String develop = repository.getDevelopBranch();
		gitFlow.toGit().checkout().setName(develop).call();

		// Create a commit on develop branch
		String developFile = "Develop_test.txt";
		Files.touch(new File(tempDir, developFile));
		gitFlow.toGit().add().addFilepattern(developFile).call();
		gitFlow.toGit().commit().setMessage("Add another file").call();

		// feature diff
		List<DiffEntry> diff = gitFlow.featureDiff().setName(feature).call();

		// check diff
		assertNotNull(diff);
		assertFalse(diff.isEmpty());
	}

}
