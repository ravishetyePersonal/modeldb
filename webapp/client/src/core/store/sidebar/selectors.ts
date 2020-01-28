import { IApplicationState } from 'store/store';

export const selectIsCollapsedSidebar = (state: IApplicationState) =>
  state.sidebar.isCollapsedSidebar;
