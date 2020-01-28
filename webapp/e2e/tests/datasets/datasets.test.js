const { By, until } = require('selenium-webdriver');

const login = require('../../helpers/pageObjects/login');
const entitiesTests = require('../shared/entitiesTests');
const testEntityDescriptionUpdating = require('../shared/testEntityDescriptionUpdating');
const testWorkspaces = require('../shared/testWorkspaces');
const testEntityTagsCRUD = require('../shared/testEntityTagsCRUD');
const testEntityCollaboration = require('../shared/testEntityCollaboration');
const { createDatasets, deleteAllDatasets, deleteDatasetsByIds, getDatasets } = require('../../helpers/userData');
const { createDatasetWithCollaborator } = require('./mocks');
const { testSuitRetry } = require('../shared/testRetrySettings');
const routes = require('../../helpers/routes');

const navigateToDatasetsPage = async driver => {
  await login(driver);
  await driver.get(routes.datasetsRoutes.makeDatasetsRoute());
};

const navigateToDatasetsPageWithWaitingLoadingEntities = async (
  driver,
  projectId
) => {
  await navigateToDatasetsPage(driver, projectId);
  await driver.wait(
    until.elementsLocated(By.css('[data-test=dataset]')),
    30000
  );
};

describe('datasets', function() {
  this.timeout(60000);
  this.retries(testSuitRetry);

  beforeEach(async () => {
    await deleteAllDatasets();
  });

  entitiesTests.testEntitiesDisplaying({
    entityName: 'dataset',
    createEntities: createDatasets,
    navigateToPage: navigateToDatasetsPage,
  });

  entitiesTests.testWidgetEntitiesDeleting({
    entityName: 'dataset',
    createEntities: createDatasets,
    navigateToPage: navigateToDatasetsPageWithWaitingLoadingEntities,
  });

  entitiesTests.testEntitiesFiltering({
    entityName: 'dataset',
    createEntities: createDatasets,
    navigateToPage: navigateToDatasetsPageWithWaitingLoadingEntities,
  });

  entitiesTests.testEntitiesPagination({
    entityName: 'dataset',
    pageSize: 5,
    createEntities: createDatasets,
    navigateToPage: navigateToDatasetsPageWithWaitingLoadingEntities,
  });

  testEntityDescriptionUpdating({
    entityName: 'dataset',
    createEntities: createDatasets,
    navigateToPage: navigateToDatasetsPageWithWaitingLoadingEntities,
  });

  testEntityTagsCRUD({
    entityName: 'dataset',
    createEntities: createDatasets,
    navigateToPage: navigateToDatasetsPageWithWaitingLoadingEntities,
  });

  testEntityCollaboration({
    entityName: 'dataset',
    createEntityWithCollaborator: createDatasetWithCollaborator,
    createEntityWithoutCollaborator: dataset => createDatasets([dataset]),
    navigateToPage: navigateToDatasetsPageWithWaitingLoadingEntities,
  });

  testWorkspaces({
    entityName: 'dataset',
    createEntities: createDatasets,
    deleteAllEntities: deleteDatasetsByIds,
    getAllEntities: getDatasets,
    getExpectedRouteAfterChangingWorkspace: (workspaceName) => routes.datasetsRoutes.makeDatasetsRoute({ workspaceName }),
    navigateToPage: navigateToDatasetsPageWithWaitingLoadingEntities,
    deleteEntitiesByIds: getDatasets,
  });
});
