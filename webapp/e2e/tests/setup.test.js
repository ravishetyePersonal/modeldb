const { updateUsername, deleteAllProjects } = require('../helpers/userData');
const getConfig = require('../getConfig');

const config = getConfig();

before(async function() {
  this.timeout(15000);

  await updateUsername({
    username: config.user.username,
    developerKey: config.user.developerKey,
    email: config.user.email,
  });
  await updateUsername({
    username: config.anotherUser.username,
    developerKey: config.anotherUser.developerKey,
    email: config.anotherUser.email,
  });
});
