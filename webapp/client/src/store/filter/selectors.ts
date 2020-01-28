import { PropertyType } from 'core/shared/models/Filters';
import { IApplicationState } from '../store';
import { IFilterState } from './types';

const selectState = (state: IApplicationState): IFilterState => state.filters;

// todo rename
export const selectCurrentContextAppliedFilters = (
  state: IApplicationState
) => {
  return selectCurrentContextFilters(state).filter(filter =>
    'isEdited' in filter ? !filter.isEdited : true
  );
};

export const selectContextFilters = (
  state: IApplicationState,
  name: string
) => {
  const context = selectContextDataByName(state, name);
  return context ? context.filters : [];
};

export const selectCurrentContextFilters = (state: IApplicationState) => {
  const context = selectCurrentContextData(state);
  return context ? context.filters : [];
};

export const selectCurrentContextName = (state: IApplicationState) =>
  selectState(state).data.currentContextName;

export const selectCurrentContextData = (state: IApplicationState) => {
  const currentContextName = selectCurrentContextName(state);
  return currentContextName
    ? selectContextsData(state)[currentContextName]
    : undefined;
};

export const selectContextDataByName = (
  state: IApplicationState,
  name: string
) => selectContextsData(state)[name];

export const selectContextsData = (state: IApplicationState) =>
  selectState(state).data.contexts;

export const hasContext = (state: IApplicationState, name: string) =>
  Boolean(selectContextDataByName(state, name));

export const selectCurrentContextFiltersByType = (
  state: IApplicationState,
  type: PropertyType
) => {
  const context = selectCurrentContextData(state);
  return context ? context.filters.filter(filter => filter.type === type) : [];
};
