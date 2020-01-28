const login = require('../../helpers/pageObjects/login');
const entitiesTests = require('../shared/entitiesTests');
const testEntityDescriptionUpdating = require('../shared/testEntityDescriptionUpdating');
const testEntityTagsCRUD = require('../shared/testEntityTagsCRUD');
const testEntityCollaboration = require('../shared/testEntityCollaboration');
const testWorkspaces = require('../shared/testWorkspaces');
const { createProjects, deleteAllProjects, deleteProjectsByIds, getProjects } = require('../../helpers/userData');
const { createProjectWithCollaborator } = require('./mocks');
const { testSuitRetry } = require('../shared/testRetrySettings');
const routes = require('../../helpers/routes');

const navigateToProjectsPage = async (driver) => {
  await login(driver);
  await driver.get(routes.projectsRoutes.makeProjectsRoute());
};

describe('projects', function() {
  this.timeout(60000);
  this.retries(testSuitRetry);

  beforeEach(async () => {
    await deleteAllProjects();
  });

  entitiesTests.testEntitiesDisplaying({
    entityName: 'project',
    createEntities: createProjects,
    navigateToPage: navigateToProjectsPage,
  });

  entitiesTests.testWidgetEntitiesDeleting({
    entityName: 'project',
    createEntities: createProjects,
    navigateToPage: navigateToProjectsPage,
  });

  entitiesTests.testEntitiesFiltering({
    entityName: 'project',
    createEntities: createProjects,
    navigateToPage: navigateToProjectsPage,
  });

  entitiesTests.testEntitiesPagination({
    entityName: 'project',
    pageSize: 10,
    createEntities: createProjects,
    navigateToPage: navigateToProjectsPage,
  });

  testEntityDescriptionUpdating({
    entityName: 'project',
    createEntities: createProjects,
    navigateToPage: navigateToProjectsPage,
  });
  
  testEntityTagsCRUD({
    entityName: 'project',
    createEntities: createProjects,
    navigateToPage: navigateToProjectsPage,
  });

  testEntityCollaboration({
    entityName: 'project',
    createEntityWithCollaborator: createProjectWithCollaborator,
    createEntityWithoutCollaborator: project => createProjects([project]),
    navigateToPage: navigateToProjectsPage,
  });

  testWorkspaces({
    entityName: 'project',
    createEntities: createProjects,
    deleteEntitiesByIds: deleteProjectsByIds,
    getAllEntities: getProjects,
    getExpectedRouteAfterChangingWorkspace: (workspaceName) => routes.projectsRoutes.makeProjectsRoute({ workspaceName }),
    navigateToPage: navigateToProjectsPage,
  });
});
