const { By, until } = require('selenium-webdriver');
const { assert } = require('chai');

const getDriver = require('../../helpers/getDriver');
const {
  handle2FA,
  handleReauthorization,
} = require('../../helpers/gitLoginHelpers');
const getConfig = require('../../getConfig');
const login = require('../../helpers/pageObjects/login');
const { testSuitRetry, testCaseRetry } = require('../shared/testRetrySettings');
const routes = require('../../helpers/routes');

const config = getConfig();
const baseURL = config.baseURL;
const loginPageUrl = `${baseURL}/login`;

describe('authorization tests', function() {
  let driver = null;
  this.timeout(60000);
  this.retries(testSuitRetry);

  beforeEach(async () => {
    driver = await getDriver();
  });

  afterEach(async () => driver.quit());

  it('should login in the app by auth0 service', async function() {
    this.retries(testCaseRetry);

    await driver.get(baseURL);

    await driver.wait(
      until.urlIs(loginPageUrl),
      15000,
      'should open login page'
    );

    const loginByGithubButtonElem = await driver.wait(
      until.elementLocated(By.css('[data-test=login-by-github-button]')),
      15000,
      'should display login button'
    );
    await loginByGithubButtonElem.click();

    await driver
      .wait(until.elementLocated(By.id('login_field')), 15000)
      .sendKeys(config.user.email);
    await driver.findElement(By.id('password')).sendKeys(config.user.password);
    await driver.findElement(By.css('input[type=submit]')).click();

    await driver.sleep(1000);
    handleReauthorization(driver);
    await driver.sleep(1000);
    await handle2FA(driver);

    await driver.wait(
      until.elementLocated(By.id('root')),
      15000,
      'after login should redirect on the app'
    );

    const projectsPageUrl = routes.projectsRoutes.makeProjectsRoute();
    assert.equal(await driver.getCurrentUrl(), projectsPageUrl);
  });

  it('should logout from the app', async function() {
    this.retries(testCaseRetry);

    await login(driver);

    await driver.wait(until.elementLocated(By.id('user-menu')), 15000);
    const userMenu = await driver.findElement(By.id('user-menu'));
    assert.exists(userMenu, 'user menu does not exist!');

    await userMenu.click();

    const logoutButton = await driver.findElement(By.id('logout-button'));
    assert.exists(logoutButton, 'logout button does not exist');

    await logoutButton.click();

    await driver.wait(
      until.urlIs(loginPageUrl),
      15000,
      'should redirect on the login page'
    );
    assert.equal(await driver.getCurrentUrl(), loginPageUrl);
  });
});
