import * as React from 'react';
import { connect } from 'react-redux';
import { bindActionCreators, Dispatch } from 'redux';

import { Icon } from 'core/shared/view/elements/Icon/Icon';
import Preloader from 'core/shared/view/elements/Preloader/Preloader';
import { IComment } from 'models/Comments';
import {
  loadComments,
  resetEntityComments,
  selectEntityComments,
  selectIsLoadingEntityComments,
} from 'store/comments';
import { IApplicationState } from 'store/store';
import { IRequiredEntityInfo } from '../../types';

import AddCommentForm from './AddCommentForm/AddCommentForm';
import Comment from './Comment/Comment';
import styles from './Comments.module.css';

interface ILocalProps {
  entityInfo: IRequiredEntityInfo;
  onClose?(): void;
}

interface IPropsFromState {
  isLoading: boolean;
  comments: IComment[] | undefined;
}

interface IActionProps {
  loadComments: typeof loadComments;
  resetEntityComments: typeof resetEntityComments;
}

type AllProps = ILocalProps & IPropsFromState & IActionProps;

class Comments extends React.PureComponent<AllProps> {
  public componentDidMount() {
    this.props.loadComments(this.props.entityInfo.id);
  }

  public render() {
    const { isLoading, entityInfo, comments, onClose } = this.props;
    return (
      <div className={styles.root} data-test="comments">
        <div className={styles.header}>
          <div className={styles.headerLabel}>
            Comments
            <Icon type="close" className={styles.close} onClick={onClose} />
          </div>
          <div className={styles.headerContent}>
            <div className={styles.commentNumber}>
              {isLoading || !comments ? (
                <Preloader variant="dots" />
              ) : (
                comments.length
              )}
            </div>
            <div className={styles.entityInfo} data-test="comments-entity-name">
              {entityInfo.name}
            </div>
          </div>
        </div>
        {isLoading || !comments ? (
          <div className={styles.preloader} data-test="comments-preloader">
            <Preloader variant="dots" />
          </div>
        ) : (
          <div className={styles.content}>
            <div className={styles.items}>
              {comments.map(comment => (
                <div className={styles.item} key={comment.id}>
                  <Comment data={comment} entityId={entityInfo.id} />
                </div>
              ))}
            </div>
            <AddCommentForm entityId={entityInfo.id} />
          </div>
        )}
      </div>
    );
  }
}

const mapDispatchToProps = (dispatch: Dispatch): IActionProps =>
  bindActionCreators(
    {
      loadComments,
      resetEntityComments,
    },
    dispatch
  );

const mapStateToProps = (
  state: IApplicationState,
  localProps: ILocalProps
): IPropsFromState => {
  return {
    comments: selectEntityComments(state, localProps.entityInfo.id),
    isLoading: selectIsLoadingEntityComments(state, localProps.entityInfo.id),
  };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Comments);