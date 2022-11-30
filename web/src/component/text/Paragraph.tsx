import classNames from 'classnames';
import { sizeClassName, TextSize } from 'component/text/Text';
import React, { ReactNode } from 'react';
import styles from './Paragraph.module.scss';

interface Props {
  className?: string;

  /**
   * Don't provide this prop unless you need to override the text size.
   */
  size?: TextSize;

  children: ReactNode;
}

/**
 * Use this for paragraphs of text.
 * No need to add [Text] inside, except to change how text looks.
 */
const Paragraph: React.FC<Props> = ({ className = undefined, size = undefined, children }) => {
  return (
    <p className={classNames(styles.p, sizeClassName(size), className)}>
      {children}
    </p>
  );
};

export default Paragraph;
