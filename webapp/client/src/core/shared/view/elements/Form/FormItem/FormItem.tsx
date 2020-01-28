import * as React from 'react';

import InlineErrorView from 'core/shared/view/elements/Errors/InlineErrorView/InlineErrorView';

import styles from './FormItem.module.css';

interface ILocalProps {
  label: string;
  children: React.ReactNode;
  dataTest?: string;
  additionalContent?: React.ReactNode;
  meta?: { touched: boolean; error?: string };
  subItem?: React.ReactNode;
}

const FormItem = ({
  dataTest,
  label,
  additionalContent,
  meta,
  children,
  subItem,
}: ILocalProps) => {
  return (
    <div className={styles.form_item} data-test={dataTest}>
      <div className={styles.main}>
        <span className={styles.label}>{label}</span>
        {additionalContent && (
          <div className={styles.additional_content}>{additionalContent}</div>
        )}
        <div className={styles.content}>{children}</div>
      </div>
      {subItem}
      {meta && meta.touched && meta.error && (
        <InlineErrorView error={meta.error} dataTest="field-error" />
      )}
    </div>
  );
};

export default FormItem;
