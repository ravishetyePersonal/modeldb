import cn from 'classnames';
import * as React from 'react';
import ReactAvatar from 'react-avatar';
import { NavLink } from 'react-router-dom';

import styles from './EntityWithMembersList.module.css';

interface IUser {
  id: string;
  username: string;
  picture?: string;
}

const EntityWithMembers = ({
  actions,
  name,
  members = [],
  entityLink,
  id,
  isUpdating,
  role,
  dataTest,
}: {
  id: string;
  name: string;
  entityLink: string;
  role: 'owner' | 'member' | undefined;
  members?: IUser[];
  isUpdating: boolean;
  actions: React.ReactNode[];
  dataTest?: string;
}) => {
  return (
    <div
      className={cn(styles.entity, {
        [styles.entity_updating]: isUpdating,
      })}
      key={id}
      data-test={`entity-with-members`}
    >
      <div className={styles.entity__column}>
        <NavLink
          className={styles.entity__name}
          to={entityLink}
          data-test="entity-name"
        >
          {name}
        </NavLink>
        <div className={styles.entity__userRole} data-test="entity-role">
          {role || null}
        </div>
      </div>
      <div className={styles.entity__members}>
        {members.length > 0 ? <Members members={members} /> : null}
      </div>
      <div className={styles.entity__actions}>
        {actions.map((action, i) => (
          <div
            className={styles.entity__action}
            key={i}
            data-test="entity-action"
          >
            {action}
          </div>
        ))}
      </div>
    </div>
  );
};

const Members = ({ members }: { members: IUser[] }) => {
  const maxMembers = 7;
  const displayedMembers = members
    .slice(0, maxMembers)
    .map(collaborator => (
      <ReactAvatar
        key={collaborator.id}
        name={collaborator.username}
        round={true}
        size="30"
        textSizeRatio={24 / 11}
        className={styles.collaboratorAvatar}
        src={collaborator.picture}
      />
    ));
  if (members.length >= maxMembers) {
    return (
      <div className={styles.members}>
        {displayedMembers}
        <div className={styles.team__restCollaborators}>
          +{members.length - displayedMembers.length}
        </div>
      </div>
    );
  }
  return <div className={styles.members}>{displayedMembers}</div>;
};

const EntityWithMembersList = ({
  noEntitiesPlaceholder,
  children,
  dataTest,
}: {
  noEntitiesPlaceholder: string;
  children: React.ReactNode;
  dataTest?: string;
}) => {
  return (
    <div className={styles.entities} data-test={dataTest}>
      {React.Children.count(children) === 0 ? (
        <div className={styles.entitiesPlaceholder}>
          {noEntitiesPlaceholder}
        </div>
      ) : (
        children
      )}
    </div>
  );
};

export { EntityWithMembers };
export default EntityWithMembersList;
