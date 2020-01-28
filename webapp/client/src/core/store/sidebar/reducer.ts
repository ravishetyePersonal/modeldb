import { createReducer, ActionType } from 'typesafe-actions';

import * as actions from './actions';
import { ISidebarState } from './types';

export default createReducer<ISidebarState, ActionType<typeof actions>>({
  isCollapsedSidebar: false,
}).handleAction(actions.toggleCollapsingSidebar, state => ({
  ...state,
  isCollapsedSidebar: !state.isCollapsedSidebar,
}));
