const { By, until, Key } = require('selenium-webdriver');
const { assert } = require('chai');

const login = require('../../helpers/pageObjects/login');
const getDriver = require('../../helpers/getDriver');
const getConfig = require('../../getConfig');
const { updateUsername } = require('../../helpers/userData');
const routes = require('../../helpers/routes');
const { testSuitRetry } = require('../shared/testRetrySettings');

const config = getConfig();

const navigateToProfilePage = async driver => {
  await login(driver);
  await driver.get(routes.settings.makeProfileRoute());
};

const clearInput = async input => {
  await input.sendKeys(Key.CONTROL + 'a');
  await input.sendKeys(Key.DELETE);
  return input;
};

describe('settings', function() {
  this.timeout(60000);

  describe('profile page', () => {
    describe('changing a username', () => {
      let driver;
  
      const newUsername = `${config.user.username}52`;
  
      beforeEach(async () => {
        driver = await getDriver();
        await navigateToProfilePage(driver);
      });
  
      afterEach(async () => {
        await driver.quit();
        await updateUsername({
          username: config.user.username,
          developerKey: config.user.developerKey,
          email: config.user.email,
        });
      });
  
      it('should change a username', async function() {
        this.retries(testSuitRetry);
  
        await driver.wait(
          until.elementLocated(By.css('[data-test=profile-page]')),
          10000,
          'should display profile page'
        );
  
        const usernameInput = await driver.wait(
          until.elementLocated(By.css('[data-test=username-input]')),
          10000,
          'should select usename input'
        );
  
        await clearInput(usernameInput);
        await usernameInput.sendKeys(newUsername);
  
        const changeUsernameButton = await driver.wait(
          until.elementLocated(By.css('[data-test=change-username-button]')),
          10000,
          'should change usename'
        );
        await driver.findElement(By.css('[data-test=profile-page]')).click(); // simulate blur
        await driver.findElement(By.css('[data-test=change-username-button]')).click();
  
        const confirmOkButton = await driver.wait(
          until.elementLocated(By.css('[data-test=confirm-ok-button]')),
          10000,
          'should click confirm ok button'
        );
        await confirmOkButton.click();
  
        await driver.wait(
          until.elementLocated(
            By.css('[data-test=updating-user-success-notification]'),
            30000,
            'should show success notification if a username was updated'
          )
        );
        assert.equal(
          await usernameInput.getAttribute('value'),
          newUsername,
          'should display the new username'
        );
      });
    });
  });
});
