import Code from 'component/code/Code';
import React, { PropsWithChildren, ReactNode } from 'react';
import styles from './CodeBlock.module.scss';

interface Props extends PropsWithChildren {
  children: ReactNode;
}

/**
 * Use this to display a multi-line block of code.
 * For code inline within some text, use [Code] intead.
 *
 * The wrapping div serves to allow this component to display as a block element
 * without having the background of the code span the full width.
 */
const CodeBlock: React.FC<Props> = ({ children }) => {
  return (
    <div>
      <Code className={styles.code}>{children}</Code>
    </div>
  );
};

export default CodeBlock;
