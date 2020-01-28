import { History } from 'history';

import {
  makeDefaultTagFilter,
  IFilterData,
  IStringFilterData,
} from 'core/shared/models/Filters';
import flushAllPromises from 'core/shared/utils/tests/integrations/flushAllPromises';
import setupIntegrationTest from 'utils/tests/integrations/setupIntegrationTest';

import routes from 'routes';
import {
  addFilterToCurrentContext,
  setContext,
  removeFilterFromCurrentContext,
  editFilterInCurrentContext,
} from '../actions';
import { selectCurrentContextFilters } from '../selectors';
import { IFilterState } from '../types';
import { userWorkspacesWithCurrentUser } from 'utils/tests/mocks/models/workspace';

const currentContextName = 'projects';

const makeMockFilterState = (
  defaultFilters: IFilterData[] = []
): IFilterState => {
  return {
    data: {
      contexts: {
        projects: {
          name: currentContextName,
          ctx: {
            name: currentContextName,
            quickFilters: [],
            onApplyFilters: jest.fn(),
          },
          filters: defaultFilters,
        },
      },
      currentContextName,
    },
  };
};

const localStorageFilters = {
  save: (
    localStorage: Storage,
    contextName: string,
    filters: IFilterData[]
  ) => {
    localStorage[`${contextName}_filter`] = JSON.stringify(
      JSON.stringify(filters)
    );
  },
  get: (localStorage: Storage, contextName: string): IFilterData[] | null => {
    const rawFilters = localStorage[`${contextName}_filter`];
    return rawFilters ? JSON.parse(rawFilters) : null;
  },
};

const URLFilters = {
  makePathWithFilters: (path: string, filters: IFilterData[]) => {
    const formattedFilters = encodeURIComponent(JSON.stringify(filters));
    return `${path}?filters=${formattedFilters}`;
  },
  get: (history: History) => {
    const rawFiltersFromURL = new URLSearchParams(history.location.search).get(
      'filters'
    );
    return rawFiltersFromURL
      ? JSON.parse(decodeURIComponent(rawFiltersFromURL))
      : null;
  },
};

describe('store', () => {
  describe('filter', () => {
    describe('saving filters in localStorage', () => {
      it('should add filters if it not exist', async () => {
        const newFilter = makeDefaultTagFilter('demo');
        const { store } = setupIntegrationTest({
          initialState: { filters: makeMockFilterState() },
        });

        await store.dispatch(addFilterToCurrentContext(newFilter) as any);
        await flushAllPromises();

        expect(
          localStorageFilters.get(localStorage, currentContextName)
        ).toEqual([newFilter]);
      });

      it('should remove filters if a user delete all filters', async () => {
        const { store } = setupIntegrationTest({
          initialState: { filters: makeMockFilterState() },
        });

        const newFilter = makeDefaultTagFilter('demo');
        await store.dispatch(addFilterToCurrentContext(newFilter) as any);
        await store.dispatch(removeFilterFromCurrentContext(newFilter) as any);
        await flushAllPromises();

        expect(
          localStorageFilters.get(localStorage, currentContextName)
        ).toEqual(null);
      });

      it('should change updated filter', async () => {
        const { store } = setupIntegrationTest({
          initialState: { filters: makeMockFilterState() },
        });

        const newFilter = makeDefaultTagFilter('demo');
        const editedFilter: IStringFilterData = {
          ...newFilter,
          value: 'edited filter value',
          invert: true,
        };
        await store.dispatch(addFilterToCurrentContext(newFilter) as any);
        await store.dispatch(editFilterInCurrentContext(editedFilter) as any);
        await flushAllPromises();

        expect(
          localStorageFilters.get(localStorage, currentContextName)
        ).toEqual([editedFilter]);
      });
    });

    describe('saving filters in URL', () => {
      it('should add filters param if it not exist', async () => {
        const newFilter = makeDefaultTagFilter('demo');
        const { history, store } = setupIntegrationTest({
          initialState: { filters: makeMockFilterState() },
        });

        await store.dispatch(addFilterToCurrentContext(newFilter) as any);
        await flushAllPromises();

        expect(URLFilters.get(history)).toEqual([newFilter]);
      });

      it('should remove filters param if a user delete all filters', async () => {
        const { history, store } = setupIntegrationTest({
          initialState: { filters: makeMockFilterState() },
        });

        const newFilter = makeDefaultTagFilter('demo');
        await store.dispatch(addFilterToCurrentContext(newFilter) as any);
        await store.dispatch(removeFilterFromCurrentContext(newFilter) as any);
        await flushAllPromises();

        expect(URLFilters.get(history)).toEqual(null);
      });

      it('should change updated filter in filters param', async () => {
        const { history, store } = setupIntegrationTest({
          initialState: { filters: makeMockFilterState() },
        });

        const newFilter = makeDefaultTagFilter('demo');
        const editedFilter: IStringFilterData = {
          ...newFilter,
          value: 'edited filter value',
          invert: true,
        };
        await store.dispatch(addFilterToCurrentContext(newFilter) as any);
        await store.dispatch(editFilterInCurrentContext(editedFilter) as any);
        await flushAllPromises();

        expect(URLFilters.get(history)).toEqual([editedFilter]);
      });
    });
  });

  describe('restore filters from URL and localStorage', () => {
    it('should apply filters from localStorage if they exist', async () => {
      localStorage.clear();
      const filters = [makeDefaultTagFilter('demo')];
      localStorageFilters.save(localStorage, currentContextName, filters);
      const { store } = setupIntegrationTest({
        initialState: { filters: makeMockFilterState() },
      });

      await store.dispatch(setContext(
        makeMockFilterState().data.contexts[currentContextName].ctx
      ) as any);
      await flushAllPromises();

      // todo fix localStorage
      // expect(selectCurrentContextFilters(store.getState())).toEqual(filters);
    });

    it('should apply filters from URL if they filters param exist', async () => {
      const filters = [makeDefaultTagFilter('demo')];
      const { store } = setupIntegrationTest({
        pathname: URLFilters.makePathWithFilters(
          routes.projects.getRedirectPath({
            workspaceName: userWorkspacesWithCurrentUser.user.name,
          }),
          filters
        ),
      });

      await store.dispatch(setContext(
        makeMockFilterState().data.contexts[currentContextName].ctx
      ) as any);
      await flushAllPromises();

      expect(selectCurrentContextFilters(store.getState())).toEqual(filters);
    });

    it('should apply filters from URL if filters are in localStorage and in URL', async () => {
      localStorageFilters.save(localStorage, currentContextName, [
        makeDefaultTagFilter('test'),
        makeDefaultTagFilter('test2'),
      ]);
      const filtersInURL = [makeDefaultTagFilter('demo')];
      const { store } = setupIntegrationTest({
        pathname: URLFilters.makePathWithFilters(
          routes.projects.getRedirectPath({
            workspaceName: userWorkspacesWithCurrentUser.user.name,
          }),
          filtersInURL
        ),
      });

      await store.dispatch(setContext(
        makeMockFilterState().data.contexts[currentContextName].ctx
      ) as any);
      await flushAllPromises();

      expect(selectCurrentContextFilters(store.getState())).toEqual(
        filtersInURL
      );
    });
  });
});
