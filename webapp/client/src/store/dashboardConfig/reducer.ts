import { Reducer } from 'redux';

import { defaultColumnConfig } from './constants';
import {
  IDashboardConfigState,
  IFeatureAction,
  toggleColumnVisibilityActionTypes,
} from './types';

const dashboardInitialState: IDashboardConfigState = {
  columnConfig: defaultColumnConfig,
};

export const dashboardConfigReducer: Reducer<
  IDashboardConfigState,
  IFeatureAction
> = (state = dashboardInitialState, action) => {
  switch (action.type) {
    case toggleColumnVisibilityActionTypes.TOGGLE_SHOWN_COLUMN_ACTION_TYPES: {
      return {
        ...state,
        columnConfig: {
          ...state.columnConfig,
          [action.payload.columnName]: {
            ...state.columnConfig[action.payload.columnName],
            isShown: !state.columnConfig[action.payload.columnName].isShown,
          },
        },
      };
    }
    default: {
      return state;
    }
  }
};
