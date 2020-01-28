const axios = require('axios');
const { createDatasets } = require('../../helpers/userData');

const addDatasetCollaborator = (datasetId, { email, access }) => {
    return axios.post('/v1/uac-proxy/collaborator/addOrUpdateDatasetCollaborator', {
        entity_ids: [datasetId],
        share_with: email,
        collaborator_type: access,
    });
};
const createDatasetWithCollaborator = (dataset, collaboratorInfo) =>
    createDatasets([dataset])
        .then(([{ data: { dataset: { id } } }]) => addDatasetCollaborator(id, collaboratorInfo));

module.exports = {
    createDatasetWithCollaborator,
};
