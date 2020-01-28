import { action } from 'typesafe-actions';

import { IArtifact } from 'core/shared/models/Artifact';
import { isFileExtensionImage } from 'core/shared/models/File';
import normalizeError from 'core/shared/utils/normalizeError';
import { ActionResult } from 'store/store';

import { getArtifactPreviewFileExtension } from './helpers';
import { selectArtifactUrl } from './selectors';
import {
  ILoadArtifactUrlActions,
  loadArtifactUrlActionTypes,
  IResetAction,
  resetActionType,
  ILoadArtifactPreviewActions,
  loadArtifactPreviewActionTypes,
  downloadArtifactActionTypes,
  IDownloadArtifactActions,
  loadDatasetVersionActionTypes,
  ILoadDatasetVersionActions,
  IDeleteArtifactActions,
  deleteArtifactActionTypes,
  EntityType,
} from './types';

export const loadArtifactUrl = (
  entityType: EntityType,
  entityId: string,
  artifact: IArtifact
): ActionResult<void, ILoadArtifactUrlActions> => async (
  dispatch,
  getState,
  { ServiceFactory }
) => {
  dispatch(action(loadArtifactUrlActionTypes.REQUEST, { key: artifact.key }));

  switch (entityType) {
    case 'experiment':
      return ServiceFactory.getExperimentsService()
        .loadArtifactUrl(entityId, artifact)
        .then(res => {
          const success = action(loadArtifactUrlActionTypes.SUCCESS, {
            key: artifact.key,
            url: res,
          });
          dispatch(
            action(loadArtifactUrlActionTypes.SUCCESS, {
              key: artifact.key,
              url: res,
            })
          );
          return success;
        })
        .catch(error => {
          const failure = action(
            loadArtifactUrlActionTypes.FAILURE,
            normalizeError(error)
          );
          dispatch(failure);
          throw error;
        });
    case 'experimentRun':
      return ServiceFactory.getExperimentRunsService()
        .loadArtifactUrl(entityId, artifact)
        .then(res => {
          const success = action(loadArtifactUrlActionTypes.SUCCESS, {
            key: artifact.key,
            url: res,
          });
          dispatch(
            action(loadArtifactUrlActionTypes.SUCCESS, {
              key: artifact.key,
              url: res,
            })
          );
          return success;
        })
        .catch(error => {
          const failure = action(
            loadArtifactUrlActionTypes.FAILURE,
            normalizeError(error)
          );
          dispatch(failure);
          throw error;
        });
    case 'project':
      return ServiceFactory.getProjectsService()
        .loadArtifactUrl(entityId, artifact)
        .then(res => {
          const success = action(loadArtifactUrlActionTypes.SUCCESS, {
            key: artifact.key,
            url: res,
          });
          dispatch(
            action(loadArtifactUrlActionTypes.SUCCESS, {
              key: artifact.key,
              url: res,
            })
          );
          return success;
        })
        .catch(error => {
          const failure = action(
            loadArtifactUrlActionTypes.FAILURE,
            normalizeError(error)
          );
          dispatch(failure);
          throw error;
        });
  }
};

const downloadFromUrl = (url: string) => {
  return fetch(url)
    .then(response => {
      if (response.status === 200) {
        const link = document.createElement('a');
        link.style.display = 'none';
        document.body.appendChild(link);
        link.setAttribute('href', url);
        link.setAttribute('download', '');
        link.click();
        window.URL.revokeObjectURL(link.href);
        document.body.removeChild(link);
      } else {
        throw new Error(`${response.status} - ${response.statusText}`);
      }
    })
    .catch(err => {
      throw err;
    });
};

export const reset = (): IResetAction => {
  return { type: resetActionType.RESET };
};

export const downloadArtifact = (
  entityType: EntityType,
  entityId: string,
  artifact: IArtifact
): ActionResult<void, IDownloadArtifactActions> => async (
  dispatch,
  getState,
  deps
) => {
  dispatch(action(downloadArtifactActionTypes.REQUEST, { key: artifact.key }));

  (async () => {
    const artifactUrl = selectArtifactUrl(getState());
    if (!artifactUrl) {
      await loadArtifactUrl(entityType, entityId, artifact)(
        dispatch,
        getState,
        deps
      );
      return selectArtifactUrl(getState())!;
    }
    return artifactUrl;
  })()
    .then(url => downloadFromUrl(url))
    .then(() =>
      dispatch(
        action(downloadArtifactActionTypes.SUCCESS, { key: artifact.key })
      )
    )
    .catch(error => {
      dispatch(
        action(downloadArtifactActionTypes.FAILURE, normalizeError(error))
      );
    });
};

export const loadArtifactPreview = (
  entityType: EntityType,
  entityId: string,
  artifact: IArtifact
): ActionResult<void, ILoadArtifactPreviewActions> => async (
  dispatch,
  getState,
  deps
) => {
  dispatch(
    action(loadArtifactPreviewActionTypes.REQUEST, { key: artifact.key })
  );

  (async () => {
    const artifactUrl = selectArtifactUrl(getState());
    if (!artifactUrl) {
      await loadArtifactUrl(entityType, entityId, artifact)(
        dispatch,
        getState,
        deps
      );
      return selectArtifactUrl(getState())!;
    }
    return artifactUrl;
  })()
    .then(url => {
      if (isFileExtensionImage(getArtifactPreviewFileExtension(artifact)!)) {
        dispatch(
          action(loadArtifactPreviewActionTypes.SUCCESS, {
            key: artifact.key,
            preview: url,
          })
        );
      } else {
        return fetch(url)
          .then(data => data.text())
          .then(preview => {
            dispatch(
              action(loadArtifactPreviewActionTypes.SUCCESS, {
                key: artifact.key,
                preview,
              })
            );
          });
      }
    })
    .catch(error => {
      dispatch(
        action(loadArtifactPreviewActionTypes.FAILURE, normalizeError(error))
      );
    });
};

export const loadDatasetVersion = (
  datasetVersionId: string,
  datasetId?: string
): ActionResult<void, ILoadDatasetVersionActions> => async (
  dispatch,
  getState,
  { ServiceFactory }
) => {
  dispatch(action(loadDatasetVersionActionTypes.REQUEST, { datasetVersionId }));

  await ServiceFactory.getDatasetVersionsService()
    .loadDatasetVersion(datasetVersionId, datasetId)
    .then(datasetVersion => {
      dispatch(
        action(loadDatasetVersionActionTypes.SUCCESS, {
          datasetVersionId,
          datasetVersion,
        })
      );
    })
    .catch(error => {
      dispatch(
        action(loadDatasetVersionActionTypes.FAILURE, {
          datasetVersionId,
          error: normalizeError(error),
        })
      );
    });
};

export const deleteArtifact = (
  experimentRunId: string,
  artifactKey: string
): ActionResult<void, IDeleteArtifactActions> => async (
  dispatch,
  getState,
  { ServiceFactory }
) => {
  dispatch(action(deleteArtifactActionTypes.REQUEST, { experimentRunId }));

  await ServiceFactory.getExperimentRunsService()
    .deleteArtifact(experimentRunId, artifactKey)
    .then(() => {
      dispatch(action(deleteArtifactActionTypes.SUCCESS, { experimentRunId }));
    })
    .catch(error => {
      dispatch(
        action(deleteArtifactActionTypes.FAILURE, {
          experimentRunId,
          error: normalizeError(error),
        })
      );
    });
};
