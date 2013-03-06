package org.ilaborie.jgit.flow.config;

import org.ilaborie.jgit.flow.config.feature.FeatureCheckoutTestCase;
import org.ilaborie.jgit.flow.config.feature.FeatureListTestCase;
import org.ilaborie.jgit.flow.config.feature.FeatureStartTestCase;
import org.ilaborie.jgit.flow.config.init.InitCommandTestCase;
import org.ilaborie.jgit.flow.config.repository.GitFlowRepositoryTestCase;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ //
GitFlowRepositoryTestCase.class, //
		InitCommandTestCase.class, //
		FeatureStartTestCase.class, //
		FeatureListTestCase.class, //
		FeatureCheckoutTestCase.class //
})
public class AllTests {

}
