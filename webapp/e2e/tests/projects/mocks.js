const axios = require('axios');
const { createProjects } = require('../../helpers/userData');

const addProjectCollaborator = (projectId, { email, access }) => {
    return axios.post('/v1/uac-proxy/collaborator/addOrUpdateProjectCollaborator', {
        entity_ids: [projectId],
        share_with: email,
        collaborator_type: access,
    });
};
const createProjectWithCollaborator = (project, collaboratorInfo) =>
    createProjects([project])
    .then(([{ data: { project: { id } } }]) => addProjectCollaborator(id, collaboratorInfo));

module.exports = {
    createProjectWithCollaborator,
};
