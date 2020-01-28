package ai.verta.modeldb;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
  JobTest.class,
  ProjectTest.class,
  ExperimentTest.class,
  ExperimentRunTest.class,
  CommentTest.class,
  DatasetTest.class,
  DatasetVersionTest.class,
  HydratedServiceTest.class
})
public class PublicTestSequenceSuite {}
