import { EntityId } from 'models/Comments';

import { IApplicationState } from '../store';
import { ICommentsState } from './types';

const selectState = (state: IApplicationState): ICommentsState =>
  state.comments;

const selectCommunications = (state: IApplicationState) =>
  selectState(state).communications;

export const selectIsLoadingEntityComments = (
  state: IApplicationState,
  entityId: EntityId
) => {
  const comm = selectCommunications(state).loadingComments[entityId];
  return Boolean(comm && comm.isRequesting);
};

export const selectEntityComments = (
  state: IApplicationState,
  entityId: EntityId
) => {
  return state.comments.data.entitiesComments[entityId];
};

export const selectIsAddedComment = (state: IApplicationState) => {
  return selectCommunications(state).addingComment.isSuccess;
};

export const selectIsAddingComment = (state: IApplicationState) => {
  return selectCommunications(state).addingComment.isRequesting;
};

export const selectIsDeletingComment = (
  state: IApplicationState,
  commentId: string
) => {
  const comm = selectCommunications(state).deletingComment[commentId];
  return Boolean(comm && comm.isRequesting);
};
