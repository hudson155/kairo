import classNames from 'classnames';
import { sizeClassName, TextSize } from 'component/text/Text';
import React, { HTMLAttributes, ReactNode } from 'react';
import styles from './Paragraph.module.scss';

interface Props extends HTMLAttributes<HTMLParagraphElement> {
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
const Paragraph: React.FC<Props> = ({ className, size = undefined, children, ...props }) => {
  return (
    <p className={classNames(styles.p, sizeClassName(size), className)} {...props}>
      {children}
    </p>
  );
};

export default Paragraph;
