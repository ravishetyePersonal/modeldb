const { By, until } = require('selenium-webdriver');

const getConfig = require('../../getConfig');
const { handle2FA, handleReauthorization } = require('../gitLoginHelpers');
const config = getConfig();

const login = async driver => {
  await driver.get(config.baseURL);

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
    'should display the app'
  );
};

module.exports = login;
