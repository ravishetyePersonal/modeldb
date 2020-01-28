import * as React from 'react';

import { EntityWithDescription } from 'core/shared/models/Description';

import DescriptionManager from 'core/shared/view/domain/BaseDescriptionManager/DescriptionManager';

interface ILocalProps {
  entityType: EntityWithDescription;
  entityId: string;
  description: string;
}

class ProjectEntityDescriptionManager extends React.Component<ILocalProps> {
  public render() {
    return (
      <DescriptionManager
        description={this.props.description}
        entityType={this.props.entityType}
        entityId={this.props.entityId}
        isLoadingAccess={false}
        isReadOnly={false}
      />
    );
  }
}

export default ProjectEntityDescriptionManager;
