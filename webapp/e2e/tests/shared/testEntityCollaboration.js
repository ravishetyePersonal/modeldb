const { By, until } = require('selenium-webdriver');
const { assert } = require('chai');

const getDriver = require('../../helpers/getDriver');
const getConfig = require('../../getConfig');
const { testSuitRetry, testCaseRetry } = require('./testRetrySettings');

const config = getConfig();

const testEntityCollaboration = ({
  entityName,
  createEntityWithCollaborator,
  createEntityWithoutCollaborator,
  navigateToPage,
}) => {
  describe(`${entityName} collaboration`, function() {
    this.timeout(60000);

    const UserAccess = {
      Read: 0,
      Write: 1,
    };
    const mockEntity = {
      name: `${entityName}-dklqer`,
    };
    const mockCollaboratorInfo = {
      username: config.anotherUser.username,
      email: config.anotherUser.email,
      access: UserAccess.Read,
    };

    const openCollaboratorsPopupAndCollaboratorsTab = async (driver) => {
      const entity = await driver.wait(
        until.elementLocated(By.css(`[data-test=${entityName}]`)),
        30000
      );
      
      await entity
          .findElement(By.css('[data-test=show-collaborators-manager-button]'))
          .click();
      await openCollaboratorsTabInPopup(driver);
    };
    const openCollaboratorsPopupAndShareTab = async (driver) => {
      const entity = await driver.wait(
        until.elementLocated(By.css(`[data-test=${entityName}]`)),
        30000
      );

      await entity
          .findElement(By.css('[data-test=show-collaborators-manager-button]'))
          .click();
      await openShareTabInPopup(driver);
    };
    const openCollaboratorsTabInPopup = async (driver) => {
      const collaboratorsPopupButtonTabs = await driver.wait(
        until.elementsLocated(By.css(`[data-test=tab]`)),
        1000,
        'should display "Collaborators" popup with "tabs"'
      );
      await collaboratorsPopupButtonTabs[0].click();
    };
    const openShareTabInPopup = async (driver) => {
      const collaboratorsPopupButtonTabs = await driver.wait(
        until.elementsLocated(By.css(`[data-test=tab]`)),
        1000,
        'should display "Collaborators" popup with "tabs"'
      );
      await collaboratorsPopupButtonTabs[1].click()
    };

    const findCollaborators = async (driver) => {
      return await driver.wait(
        until.elementsLocated(By.css('[data-test=collaborator-item]')),
        4000,
        'should display collaborators'
      );
    };

    const changeCollaboratorAccess = async (collaboratorElem, access) => {
      const accessSelect = await collaboratorElem.findElement(
        By.css('[data-test=access-select-root]')
      );
      await accessSelect.click();
      await collaboratorElem.getDriver().wait(
        until.elementsLocated(By.css(`[data-test=access-select-option]`)),
        4000,
        'should display access options of the access select'
      );
      await (await accessSelect.findElement(By.css(`[data-value="${access}"]`))).click();

      await driver
        .findElement(By.css('[data-test=confirm-ok-button]'))
        .click();

      await driver.sleep(5000);
    };

    const getCollaboratorAccess = async (collaboratorItem) => {
      return Number(await collaboratorItem.findElement(
        By.css('[data-test=access-select-root]')
      ).getAttribute('data-selected-value'))
    };

    describe('displaying collaborators and owner', function() {
      this.retries(testSuitRetry);
      let driver;

      beforeEach(async () => {
        await createEntityWithCollaborator(mockEntity, mockCollaboratorInfo);

        driver = await getDriver();
        await navigateToPage(driver);
      });

      afterEach(async () => {
        driver.quit();
      });

      it('should display owner and collaborators in popup', async function() {
        this.retries(testCaseRetry);

        await openCollaboratorsPopupAndCollaboratorsTab(driver);

        const collaboratorsItems = await findCollaborators(driver);
        assert.equal(
          collaboratorsItems.length,
          2,
          'should display owner and collaborators'
        );

        const [ownerItem, collaboratorItem] = collaboratorsItems;
        assert.include(
          await ownerItem.getText(),
          config.user.username,
          'should display owner first'
        );

        assert.include(
          await collaboratorItem.getText(),
          mockCollaboratorInfo.username,
          'should display collaborator'
        );
        assert.equal(
          await getCollaboratorAccess(collaboratorItem),
          mockCollaboratorInfo.access,
          'should display correct collaborator access'
        );
      });
    });

    describe('adding collaborator', function() {
      this.retries(testSuitRetry);
      let driver;

      beforeEach(async () => {
        await createEntityWithoutCollaborator(mockEntity);

        driver = await getDriver();
        await navigateToPage(driver);
      });

      afterEach(async () => {
        driver.quit();
      });

      it(`should add collaborator in ${entityName}`, async function() {
        this.retries(testCaseRetry);

        await openCollaboratorsPopupAndShareTab(driver);

        const shareTab = await driver.findElement(
          By.css('[data-test=share-tab]')
        );

        const userCollaboratorEmailInput = await shareTab.findElement(
          By.css('[data-test=new-user-collaborator-email-input]')
        );
        await userCollaboratorEmailInput.click();
        await userCollaboratorEmailInput.sendKeys(mockCollaboratorInfo.email);
        const addUserCollaboratorButton = await shareTab.findElement(
          By.css('[data-test=add-user-collaborator-button]')
        );
        await addUserCollaboratorButton.click();

        await shareTab
          .getDriver()
          .wait(
            until.elementLocated(By.css('[data-test=share-tab-success]')),
            30000,
            'should show messsage that a collaborator was added'
          );

        await openCollaboratorsTabInPopup(driver);

        const collaboratorsItems = await findCollaborators(driver);
        assert.equal(
          collaboratorsItems.length,
          2,
          'should display owner and new collaborator'
        );
        const newCollaborator =
          collaboratorsItems[collaboratorsItems.length - 1];
        assert.include(
          await newCollaborator.getText(),
          mockCollaboratorInfo.username,
          'should display new collaborator'
        );
      });
    });

    describe('changing collaborators', function() {
      this.retries(testSuitRetry);
      let driver;

      beforeEach(async () => {
        await createEntityWithCollaborator(mockEntity, mockCollaboratorInfo);

        driver = await getDriver();
        await navigateToPage(driver);
      });

      afterEach(async () => {
        driver.quit();
      });

      it('should delete collaborator', async function() {
        this.retries(testCaseRetry);

        await openCollaboratorsPopupAndCollaboratorsTab(driver);

        const collaboratorsItems = await findCollaborators(driver);
        const collaboratorItem = collaboratorsItems[1];
        await collaboratorItem
          .findElement(By.css('[data-test=try-delete-collaborator]'))
          .click();
        await collaboratorItem
          .findElement(By.css('[data-test=confirm-ok-button]'))
          .click();

        await driver.wait(
          until.stalenessOf(collaboratorItem),
          15000,
          'should delete collaborator'
        );

        assert.equal(
          (await findCollaborators(driver)).length,
          1,
          'should display only owner'
        );
        assert.include(
          await collaboratorsItems[0].getText(),
          config.user.username,
          'should display only owner'
        );
      });

      it('should change collaborator access', async function() {
        this.retries(testCaseRetry);

        await openCollaboratorsPopupAndCollaboratorsTab(driver);

        const collaboratorsTab = await driver.wait(
          until.elementLocated(By.css('[data-test=collaborators-tab]')),
          4000,
          'should show collaborators tab'
        );

        const [_, collaboratorItem] = await collaboratorsTab.findElements(
          By.css('[data-test=collaborator-item]')
        );

        await changeCollaboratorAccess(collaboratorItem, UserAccess.Write);

        assert.equal(await getCollaboratorAccess(collaboratorItem), UserAccess.Write);
      });
    });
  });
};

module.exports = testEntityCollaboration;
