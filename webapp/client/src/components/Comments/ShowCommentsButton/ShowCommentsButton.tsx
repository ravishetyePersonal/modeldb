import { bind } from 'decko';
import * as React from 'react';
import { connect } from 'react-redux';
import { bindActionCreators, Dispatch } from 'redux';

import Fai from 'core/shared/view/elements/Fai/Fai';
import FaiWithLabel from 'core/shared/view/elements/FaiWithLabel/FaiWithLabel';
import { Icon } from 'core/shared/view/elements/Icon/Icon';
import { IComment } from 'models/Comments';
import { loadComments, selectEntityComments } from 'store/comments';
import { IApplicationState } from 'store/store';

import OverflowingComments from './OverflowingComments/OverflowingComments';
import styles from './ShowCommentsButton.module.css';
import { IRequiredEntityInfo } from './types';

type ILocalProps =
  | {
      buttonType: 'faiWithLabel';
      entityInfo: IRequiredEntityInfo;
      onHover(): void;
      onUnhover(): void;
    }
  | {
      buttonType: 'fai';
      entityInfo: IRequiredEntityInfo;
    };

interface IPropsFromState {
  comments: IComment[] | undefined;
}

interface IActionProps {
  loadComments: typeof loadComments;
}

type AllProps = ILocalProps & IPropsFromState & IActionProps;

interface ILocalState {
  isShowComments: boolean;
}

class ShowCommentsButton extends React.PureComponent<AllProps, ILocalState> {
  public state: ILocalState = {
    isShowComments: false,
  };

  public render() {
    return (
      <div className={styles.root}>
        {this.props.buttonType === 'fai' ? (
          <Fai
            theme="blue"
            icon={<Icon type="comment" />}
            variant="outlined"
            dataTest="show-comments-button"
            onClick={this.showComments}
          />
        ) : (
          <FaiWithLabel
            theme="blue"
            iconType={'comment'}
            label={'show comments'}
            dataTest="show-comments-button"
            onClick={this.showComments}
            onHover={this.props.onHover}
            onUnhover={this.props.onUnhover}
          />
        )}
        <div className={styles.miniCommentNumber}>
          {this.props.comments && (
            <span data-test="show-comments-button-badge">
              {this.props.comments.length}
            </span>
          )}
        </div>
        {this.state.isShowComments && (
          <OverflowingComments
            entityInfo={this.props.entityInfo}
            onClose={this.closeComments}
          />
        )}
      </div>
    );
  }

  @bind
  private showComments() {
    this.setState({ isShowComments: true });
  }
  @bind
  private closeComments() {
    this.setState({ isShowComments: false });
  }
}

const mapStateToProps = (
  state: IApplicationState,
  localProps: ILocalProps
): IPropsFromState => {
  return {
    comments: selectEntityComments(state, localProps.entityInfo.id),
  };
};

const mapDispatchToProps = (dispatch: Dispatch): IActionProps =>
  bindActionCreators(
    {
      loadComments,
    },
    dispatch
  );

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ShowCommentsButton);
