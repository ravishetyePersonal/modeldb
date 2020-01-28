import { Dispatch } from 'redux';
import { ActionType } from 'typesafe-actions';

import normalizeError from 'core/shared/utils/normalizeError';
import {
  IApplicationState,
  IThunkActionDependencies,
  ActionResult,
} from 'store/store';

import { IResetableAsyncAction, GetActionCreatorPayload } from './types';

const makeSimpleApiRequest = <
  ResetableAsyncAction extends IResetableAsyncAction<any, any, any, any>
>(
  resetableAsyncAction: ResetableAsyncAction
) => (
  requestApi: (params: {
    payload: GetActionCreatorPayload<ResetableAsyncAction['request']>;
    dispatch: Dispatch;
    getState: () => IApplicationState;
    dependencies: IThunkActionDependencies;
  }) => Promise<GetActionCreatorPayload<ResetableAsyncAction['success']>>,
  handlers?: {
    onSuccess: (params: {
      requestPayload: GetActionCreatorPayload<ResetableAsyncAction['request']>;
      successPayload: GetActionCreatorPayload<ResetableAsyncAction['success']>;
      dispatch: Dispatch;
      getState: () => IApplicationState;
      dependencies: IThunkActionDependencies;
    }) => Promise<any>;
  },
  getFailureActionPayload?: ({
    requestPayload,
    error,
  }: {
    requestPayload: GetActionCreatorPayload<ResetableAsyncAction['request']>;
    error: GetActionCreatorPayload<ResetableAsyncAction['failure']>['error'];
  }) => GetActionCreatorPayload<ResetableAsyncAction['failure']>
) => {
  return (
    requestPayload: GetActionCreatorPayload<ResetableAsyncAction['request']>
  ): ActionResult<void, ActionType<ResetableAsyncAction>> => async (
    dispatch,
    getState,
    dependencies
  ) => {
    dispatch(resetableAsyncAction.request(requestPayload) as any);

    try {
      const res = await requestApi({
        payload: requestPayload,
        dispatch,
        getState,
        dependencies,
      });
      dispatch(resetableAsyncAction.success(res) as any);
      handlers &&
        handlers.onSuccess({
          requestPayload,
          successPayload: res,
          dependencies,
          dispatch,
          getState,
        });
    } catch (e) {
      dispatch(resetableAsyncAction.failure(
        getFailureActionPayload
          ? getFailureActionPayload({
              error: normalizeError(e) as any,
              requestPayload,
            })
          : normalizeError(e)
      ) as any);
    }
  };
};

export default makeSimpleApiRequest;
