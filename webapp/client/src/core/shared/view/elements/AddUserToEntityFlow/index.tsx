import * as React from 'react';

import ButtonLikeText from '../ButtonLikeText/ButtonLikeText';
import { Icon } from '../Icon/Icon';
import Preloader from '../Preloader/Preloader';
import styles from './AddUserToEntityFlow.module.css';

export const defaultTryAgainButtonText = 'Try Again';
export const ErrorUserAddingComponent = ({
  error,
  tryAgainButtonProps,
}: {
  error: React.ReactNode;
  tryAgainButtonProps: { text?: string; onClick(): void };
}) => (
  <div className={styles.content} data-test="error-user-adding">
    <Icon type="error" className={styles.icon} />
    {error}
    <ButtonLikeText
      dataTest="error-try-again"
      onClick={tryAgainButtonProps.onClick}
    >
      {tryAgainButtonProps.text || defaultTryAgainButtonText}
    </ButtonLikeText>
  </div>
);

export const UserAddingComponent = () => (
  <div className={styles.content}>
    <Preloader variant="dots" />
  </div>
);

export const SuccessfullUserAddingComponent = ({
  successfullMessage,
  addNewUserButtonProps,
}: {
  successfullMessage: string;
  addNewUserButtonProps: { text: string; onClick(): void };
}) => (
  <div className={styles.content} data-test="share-tab-success">
    <Icon type="check" className={styles.icon} />
    <span className={styles.share_result_header}>{successfullMessage}</span>
    <ButtonLikeText
      dataTest="reset-successfull-view"
      onClick={addNewUserButtonProps.onClick}
    >
      {addNewUserButtonProps.text}
    </ButtonLikeText>
  </div>
);
