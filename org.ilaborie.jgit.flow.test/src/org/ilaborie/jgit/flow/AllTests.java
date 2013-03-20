package org.ilaborie.jgit.flow;

import org.ilaborie.jgit.flow.feature.FeatureCheckoutTestCase;
import org.ilaborie.jgit.flow.feature.FeatureDiffTestCase;
import org.ilaborie.jgit.flow.feature.FeatureFinishTestCase;
import org.ilaborie.jgit.flow.feature.FeatureListTestCase;
import org.ilaborie.jgit.flow.feature.FeaturePublishTestCase;
import org.ilaborie.jgit.flow.feature.FeatureStartTestCase;
import org.ilaborie.jgit.flow.feature.FeatureTrackTestCase;
import org.ilaborie.jgit.flow.hotfix.HotfixFinishTestCase;
import org.ilaborie.jgit.flow.hotfix.HotfixListTestCase;
import org.ilaborie.jgit.flow.hotfix.HotfixStartTestCase;
import org.ilaborie.jgit.flow.init.InitCommandTestCase;
import org.ilaborie.jgit.flow.release.ReleaseFinishTestCase;
import org.ilaborie.jgit.flow.release.ReleaseListTestCase;
import org.ilaborie.jgit.flow.release.ReleasePublishTestCase;
import org.ilaborie.jgit.flow.release.ReleaseStartTestCase;
import org.ilaborie.jgit.flow.release.ReleaseTrackTestCase;
import org.ilaborie.jgit.flow.repository.GitFlowRepositoryTestCase;
import org.ilaborie.jgit.flow.support.SupportListTestCase;
import org.ilaborie.jgit.flow.support.SupportStartTestCase;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ //
// General commands
		GitFlowRepositoryTestCase.class, //
		InitCommandTestCase.class, //

		// Features commands
		FeatureStartTestCase.class, //
		FeatureListTestCase.class, //
		FeatureCheckoutTestCase.class, //
		FeatureFinishTestCase.class, //
		FeaturePublishTestCase.class, //
		FeatureTrackTestCase.class, //
		FeatureDiffTestCase.class, //

		// Releases commands
		ReleaseStartTestCase.class, //
		ReleaseListTestCase.class, //
		ReleaseFinishTestCase.class, //
		ReleasePublishTestCase.class, //
		ReleaseTrackTestCase.class, //

		// Hotfixes commands
		HotfixStartTestCase.class, //
		HotfixListTestCase.class, //
		HotfixFinishTestCase.class, //

		// Supports commands
		SupportStartTestCase.class, //
		SupportListTestCase.class //
})
public class AllTests {

}
