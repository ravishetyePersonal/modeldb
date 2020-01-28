const { By, until, Key } = require('selenium-webdriver');
const { assert } = require('chai');

const getDriver = require('../../helpers/getDriver');
const { deleteAllOrganizations, createOrganizations, deleteAllEntitiesFromAllWorkspaces } = require('../../helpers/userData');
const { testSuitRetry } = require('../shared/testRetrySettings');
const getConfig = require('../../getConfig');

const config = getConfig();

const testWorkspaces = ({
    entityName,
    createEntities,
    navigateToPage,
    deleteEntitiesByIds,
    getAllEntities,
    getExpectedRouteAfterChangingWorkspace,
}) => {
    describe('workspaces', function () {
        this.timeout(60000);
        this.retries(testSuitRetry);

        const mockOrganizationsSettings = [{ name: 'org-1-12f3zcv' }, { name: 'org-2-1s23faf' }];
        const organizationWorkspaces = mockOrganizationsSettings.map(({ name }) => name);
        const userWorkspaces = {
            user: config.user.workspace,
            organizations: organizationWorkspaces
        };
        const displayedUserWorkspaceNames = ['Personal', ...organizationWorkspaces];

        const openUserMenu = async (driver) => {
            const userMenuElem = await driver.wait(until.elementLocated(By.css('[data-test=user-menu]')), 15000, 'should display user menu');
            await userMenuElem.click();
        };

        const getDisplayedWorkspacesInfo = async (driver) => {
            const displayedUserWorkspaceElements = await driver.wait(until.elementsLocated(By.css('[data-test=workspace]')), 15000, 'should display user workspaces');
            const displayedUserWorkspaces = await Promise.all(displayedUserWorkspaceElements.map(async elem => await elem.getText()));
            return displayedUserWorkspaceElements.map((elem, i) => ({ elem, name: displayedUserWorkspaces[i] }));
        };

        const changeWorkspace = async (driver, workspace) => {
            await openUserMenu(driver);
            const organizationWorkspaceInfo = (await getDisplayedWorkspacesInfo(driver)).find(({ name }) => workspace === name);
            await organizationWorkspaceInfo.elem.click();
        };

        beforeEach(async () => {
            await deleteAllEntitiesFromAllWorkspaces({ getAllEntities, deleteEntitiesByIds });
            await deleteAllOrganizations();

            await createOrganizations(mockOrganizationsSettings);
        });

        afterEach(async () => driver.quit());

        describe('displaying workspaces', () => {
            let driver = null;

            beforeEach(async () => {
                driver = await getDriver();
                await navigateToPage(driver);
            })

            it('should display user workspace and organizations workspaces in user menu', async () => {
                await openUserMenu(driver);
                const displayedUserWorkspaces = (await getDisplayedWorkspacesInfo(driver)).map(({ name }) => name);

                assert.deepEqual(displayedUserWorkspaces.slice().sort(), displayedUserWorkspaceNames.slice().sort());
            });

            it('should change a user workspace', async () => {
                const newWorkspace = organizationWorkspaces[0];

                await changeWorkspace(driver, newWorkspace);

                await driver.wait(
                    until.urlIs(getExpectedRouteAfterChangingWorkspace(newWorkspace)),
                    30000,
                    `should redirect on the ${entityName}s page with a selected organization workspaces`
                );
            });
        });


        describe(`loading ${entityName}s by a workspace`, () => {
            const userWorkspaceEntities = [
                { name: 'user-entity-adfadfzcv', workspace_name: userWorkspaces.user },
                { name: 'user-entity-adfadf', workspace_name: userWorkspaces.user }                
            ];
            const organizationWorkspaceEntities = [
                { name: 'organization-entity-dfazcv', workspace_name: userWorkspaces.organizations[0] },
                { name: 'organization-entity-qeradf', workspace_name: userWorkspaces.organizations[0] },
            ];

            beforeEach(async () => {
                await createEntities(userWorkspaceEntities);
                await createEntities(organizationWorkspaceEntities);
            });

            const checkDisplayingEntitiesByUserWorkspace = async ({ expectedEntitieNames, workspaceInfo }) => {
                const driver = await getDriver();
                await navigateToPage(driver);
                await changeWorkspace(driver, workspaceInfo.type === 'userWorkspace' ? displayedUserWorkspaceNames[0] : workspaceInfo.name);

                const entityElems = await driver.wait(
                    until.elementsLocated(By.css(`[data-test=${entityName}]`)),
                    30000,
                    `should display ${entityName}s`
                );
                const displayedEntityNames = await Promise.all(
                    entityElems.map(async (elem) => (await elem.findElement(By.css(`[data-test=${entityName}-name]`))).getText())
                );

                assert.deepEqual(displayedEntityNames.slice().sort(), expectedEntitieNames.slice().sort());
            };

            it(`should display all entities from all workspaces for an user workspace`, async () => {
                await checkDisplayingEntitiesByUserWorkspace({
                    expectedEntitieNames: userWorkspaceEntities.map(({ name }) => name),
                    workspaceInfo: {
                        type: 'userWorkspace'
                    },
                });
            });

            it(`should display ${entityName}s only for an organization workspace`, async () => {
                await checkDisplayingEntitiesByUserWorkspace({
                    expectedEntitieNames: organizationWorkspaceEntities.map(({ name }) => name),
                    workspaceInfo: { type: 'organizationWorkspace', name: userWorkspaces.organizations[0] },
                });
            });
        });
    });
};

module.exports = testWorkspaces;
