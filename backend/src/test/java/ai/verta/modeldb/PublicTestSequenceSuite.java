package ai.verta.modeldb;

import ai.verta.modeldb.lineage.LineageServiceImplNegativeTest;
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
  HydratedServiceTest.class,
  LineageTest.class,
  LineageServiceImplNegativeTest.class,
  FindProjectEntitiesTest.class,
  FindDatasetEntitiesTest.class,
  RepositoryTest.class
})
public class PublicTestSequenceSuite {}
