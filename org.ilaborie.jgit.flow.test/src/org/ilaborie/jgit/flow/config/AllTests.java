package org.ilaborie.jgit.flow.config;

import org.ilaborie.jgit.flow.config.feature.FeatureCheckoutTestCase;
import org.ilaborie.jgit.flow.config.feature.FeatureFinishTestCase;
import org.ilaborie.jgit.flow.config.feature.FeatureListTestCase;
import org.ilaborie.jgit.flow.config.feature.FeatureStartTestCase;
import org.ilaborie.jgit.flow.config.hotfix.HotfixFinishTestCase;
import org.ilaborie.jgit.flow.config.hotfix.HotfixListTestCase;
import org.ilaborie.jgit.flow.config.hotfix.HotfixStartTestCase;
import org.ilaborie.jgit.flow.config.init.InitCommandTestCase;
import org.ilaborie.jgit.flow.config.release.ReleaseFinishTestCase;
import org.ilaborie.jgit.flow.config.release.ReleaseListTestCase;
import org.ilaborie.jgit.flow.config.release.ReleaseStartTestCase;
import org.ilaborie.jgit.flow.config.repository.GitFlowRepositoryTestCase;
import org.ilaborie.jgit.flow.config.support.SupportListTestCase;
import org.ilaborie.jgit.flow.config.support.SupportStartTestCase;
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
		
		// Releases commands
		ReleaseStartTestCase.class, //
		ReleaseListTestCase.class, //
		ReleaseFinishTestCase.class, //
		
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
