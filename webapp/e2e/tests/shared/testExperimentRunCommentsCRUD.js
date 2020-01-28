const { default: axios } = require('axios');
const { By, until } = require('selenium-webdriver');
const { assert } = require('chai');

const userData = require('../../helpers/userData');
const getDriver = require('../../helpers/getDriver');
const getConfig = require('../../getConfig');
const { testSuitRetry, testCaseRetry } = require('./testRetrySettings');

const config = getConfig();

const testExperimentRunCommentsCRUD = ({ navigateToPage }) => {
  const createExperimentRunWithComments = async (
    { project, experiment, experimentRun },
    comments
  ) => {
    return userData
      .createExperimentRun(project, experiment, experimentRun)
      .then(({ projectId, experimentRunId }) =>
        addComments(projectId, experimentRunId, comments)
      );
  };

  const addComments = async (projectId, experimentRunId, comments) => {
    return Promise.all(
      comments.map(comment =>
        axios.post('/v1/modeldb/comment/addExperimentRunComment', {
          message: comment.message,
          entity_id: experimentRunId,
        })
      )
    ).then(() => ({ projectId, experimentRunId }));
  };

  describe('comments', function() {
    this.retries(testSuitRetry);
    const mockExperimentRun = { name: 'experiment-run-with-comments' };
    const mockComments = [
      { message: 'message-1', author: config.user.username },
    ];

    let driver;

    beforeEach(async () => {
      const createdExprRunsInfo = await createExperimentRunWithComments(
        {
          project: { name: 'project-adf-1' },
          experiment: { name: 'experiment' },
          experimentRun: mockExperimentRun,
        },
        mockComments
      );

      driver = await getDriver();
      await navigateToPage(driver, createdExprRunsInfo);
    });

    afterEach(async () => driver.quit());

    const getDispayedCommentsInfo = async driver => {
      return driver.findElements(By.css('[data-test=comment]')).then(elems => {
        return Promise.all(
          elems.map(async elem => {
            const author = await elem
              .findElement(By.css('[data-test=comment-author]'))
              .then(e => e.getText());
            const message = await elem
              .findElement(By.css('[data-test=comment-message]'))
              .then(e => e.getText());
            return { author, message, elem };
          })
        );
      });
    };
    const findCommentElem = async (commentInfo, driver) => {
      return getDispayedCommentsInfo(driver)
        .then(displayedCommentInfo =>
          displayedCommentInfo.find(
            displayedCommentInfo =>
              displayedCommentInfo.author === commentInfo.author &&
              displayedCommentInfo.message === commentInfo.message
          )
        )
        .then(c => (c ? c.elem : undefined));
    };
    const openExperimentRunCommentsAndWaitLoading = driver => {
      return driver
        .findElement(By.css('[data-test=show-comments-button]'))
        .then(e => e.click())
        .then(_ =>
          driver.wait(
            until.elementsLocated(
              By.css('[data-test=comment]'),
              10000,
              'should display comments'
            )
          )
        );
    };

    it('should display comments button with comments count', async function() {
      this.retries(testCaseRetry);

      await driver.findElement(By.css('[data-test=show-comments-button]'));

      const showCommentsButtonBadge = await driver.findElement(
        By.css('[data-test=show-comments-button-badge]')
      );
      await driver.sleep(1000);
      assert.equal(
        await showCommentsButtonBadge.getText(),
        mockComments.length
      );
    });

    it('should load and display comments after click on the comments button', async function() {
      this.retries(testCaseRetry);

      await openExperimentRunCommentsAndWaitLoading(driver);

      assert.equal(
        await driver
          .findElement(By.css('[data-test=comments-entity-name]'))
          .then(e => e.getText()),
        mockExperimentRun.name,
        'should display a experiment run name'
      );

      const displayedCommentsInfo = await getDispayedCommentsInfo(driver);
      mockComments.forEach(({ message, author }) => {
        const found = displayedCommentsInfo.find(
          displayedCommentInfo =>
            displayedCommentInfo.message === message &&
            displayedCommentInfo.author === author
        );
        assert.exists(
          found,
          `should display comment with message = "${message}" and author = "${author}"`
        );
      });
    });

    it('should create a comment', async function() {
      this.retries(testCaseRetry);

      const newComment = {
        message: 'new comment',
        author: config.user.username,
      };

      await openExperimentRunCommentsAndWaitLoading(driver);

      await driver
        .findElement(By.css('[data-test=comment-input]'))
        .sendKeys(newComment.message);
      await driver
        .findElement(By.css('[data-test=send-comment-button]'))
        .click();

      await driver.sleep(3000);

      const displayedCommentsInfo = await getDispayedCommentsInfo(driver);
      assert.equal(
        displayedCommentsInfo.length,
        mockComments.length + 1,
        `should add the new comment`
      );

      const createdNewComment = displayedCommentsInfo.find(
        ({ message, author }) =>
          message === newComment.message && author === newComment.author
      );
      await driver.sleep(1000);
      assert.isTrue(
        createdNewComment.message === newComment.message &&
          createdNewComment.author === newComment.author,
        `should add the new comment with message = ${
          newComment.message
        } and author = ${newComment.author}`
      );
    });

    it('should delete a comment', async function() {
      this.retries(testCaseRetry);

      const deletedComment = mockComments[0];

      await openExperimentRunCommentsAndWaitLoading(driver);

      const deletedCommentElement = await findCommentElem(
        deletedComment,
        driver
      );
      await deletedCommentElement
        .findElement(By.css('[data-test=delete-comment-button]'))
        .then(e => e.click());

      await driver.wait(
        until.stalenessOf(deletedCommentElement),
        15000,
        `should delete comment`
      );
    });
  });
};

module.exports = testExperimentRunCommentsCRUD;
