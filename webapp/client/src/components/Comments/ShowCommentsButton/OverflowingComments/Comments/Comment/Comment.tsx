import cn from 'classnames';
import { bind } from 'decko';
import moment from 'moment';
import * as React from 'react';
import { connect } from 'react-redux';

import ButtonLikeText from 'core/shared/view/elements/ButtonLikeText/ButtonLikeText';
import { EntityId, IComment } from 'models/Comments';
import { bindActionCreators, Dispatch } from 'redux';
import { deleteComment, selectIsDeletingComment } from 'store/comments';
import { IApplicationState } from 'store/store';

import styles from './Comment.module.css';

interface ILocalProps {
  entityId: EntityId;
  data: IComment;
}

interface IPropsFromState {
  isDeleting: boolean;
}

interface IActionProps {
  deleteComment: typeof deleteComment;
}

type AllProps = ILocalProps & IPropsFromState & IActionProps;

class Comment extends React.PureComponent<AllProps> {
  public render() {
    const { data, isDeleting } = this.props;

    return (
      <div className={styles.root} data-test="comment">
        <div
          className={cn(styles.root, { [styles.deleting]: isDeleting })}
          key={data.id}
        >
          <div className={styles.rightContent}>
            <div className={styles.header}>
              <span className={styles.time}>
                {moment(data.dateTime).fromNow()}
              </span>
            </div>
            <div className={styles.message} data-test="comment-message">
              {data.message}
            </div>
            <div className={styles.actions}>
              <div className={styles.action}>
                {
                  <ButtonLikeText
                    isDisabled={isDeleting}
                    dataTest="delete-comment-button"
                    onClick={this.makeOnDeleteComment(data.id)}
                  >
                    delete
                  </ButtonLikeText>
                }
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }

  @bind
  private makeOnDeleteComment(commentId: string) {
    return () => {
      if (!this.props.isDeleting) {
        this.props.deleteComment(this.props.entityId, commentId);
      }
    };
  }
}

const mapStateToProps = (
  state: IApplicationState,
  localProps: ILocalProps
): IPropsFromState => {
  return {
    isDeleting: selectIsDeletingComment(state, localProps.data.id),
  };
};

const mapDispatchToProps = (dispatch: Dispatch): IActionProps =>
  bindActionCreators(
    {
      deleteComment,
    },
    dispatch
  );

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Comment);
