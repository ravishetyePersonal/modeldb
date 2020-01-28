import { ActionType, PayloadAction } from 'typesafe-actions';

import { ActionResult } from 'store/store';

import makeResetableAsyncAction from './makeResetableAsyncAction';
import { IResetableAsyncAction, GetActionCreatorPayload } from './types';

export type IThunkActionWithResetableAsyncAction<
  T extends IResetableAsyncAction<any, any, any, any>
> = ((
  payload: GetActionCreatorPayload<T['request']>
) => ActionResult<any, ActionType<T>>) &
  T;

export default function makeCommunicationThunk<
  RequestT extends string,
  SuccessT extends string,
  FailureT extends string,
  ResetT extends string
>(
  requestType: RequestT,
  successType: SuccessT,
  failureType: FailureT,
  resetType: ResetT
) {
  return <
    RequestPayload,
    SuccessPayload,
    FailurePayload,
    ResetPayload = undefined
  >(
    f: (
      communicationActionCreators: IResetableAsyncAction<
        PayloadAction<RequestT, RequestPayload>,
        PayloadAction<SuccessT, SuccessPayload>,
        PayloadAction<FailureT, FailurePayload>,
        PayloadAction<ResetT, undefined>
      >
    ) => (payload: RequestPayload) => ActionResult<any, any>
  ): IThunkActionWithResetableAsyncAction<
    IResetableAsyncAction<
      PayloadAction<RequestT, RequestPayload>,
      PayloadAction<SuccessT, SuccessPayload>,
      PayloadAction<FailureT, FailurePayload>,
      PayloadAction<ResetT, ResetPayload>
    >
  > => {
    const resetableAsyncAction = makeResetableAsyncAction(
      requestType,
      successType,
      failureType,
      resetType
    )<RequestPayload, SuccessPayload, FailurePayload>();
    const res: IThunkActionWithResetableAsyncAction<
      IResetableAsyncAction<
        PayloadAction<RequestT, RequestPayload>,
        PayloadAction<SuccessT, SuccessPayload>,
        PayloadAction<FailureT, FailurePayload>,
        PayloadAction<ResetT, ResetPayload>
      >
    > = f(resetableAsyncAction as any) as any;
    res.request = resetableAsyncAction.request;
    res.success = resetableAsyncAction.success;
    res.failure = resetableAsyncAction.failure;
    res.reset = resetableAsyncAction.reset as any;
    return res as any;
  };
}
