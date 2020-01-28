import { findByDataTestAttribute } from 'core/shared/utils/tests/react/helpers';
import { ReactWrapper } from 'enzyme';

export const makeEntityWithMembersListHelpers = (dataTest: string) => {
  const find = findByDataTestAttribute(dataTest);
  const findEntities = findByDataTestAttribute(`entity-with-members`);
  const findEntitiesInfo = (component: ReactWrapper) => {
    return findEntities(component).map(e => ({
      node: e,
      name: findByDataTestAttribute('entity-name', e)
        .last()
        .text(),
      role: findByDataTestAttribute('entity-role', e)
        .last()
        .text(),
    }));
  };
  const findEntityByName = (name: string, component: ReactWrapper) => {
    return findEntitiesInfo(component).find(d => d.name === name)!.node;
  };
  const findEntityInfoByName = (name: string, component: ReactWrapper) => {
    return findEntitiesInfo(component).find(en => en.name === name);
  };
  const findEntityActions = (entity: ReactWrapper) =>
    findByDataTestAttribute('entity-action', entity);
  const findUpdatingEntityNames = (
    entity: ReactWrapper,
    component: ReactWrapper
  ) => {
    findEntitiesInfo(component)
      .filter(n => n.node.find(`.entity_updating`))
      .map(({ name }) => name);
  };

  return {
    find,
    findEntities,
    findEntitiesInfo,
    findEntityInfoByName,
    findEntityActions,
    findEntityByName,
    findUpdatingEntityNames,
  };
};
