import Code from 'component/code/Code';
import styles from 'component/code/CodeBlock.module.scss';
import React, { ReactNode } from 'react';

interface Props {
  children: ReactNode;
}

/**
 * Use this to display a multi-line block of code.
 * For code inline within some text, use {@link Code} instead.
 *
 * The wrapping div serves to allow this component to display as a block element
 * without having the background of the code span the full width.
 */
const CodeBlock: React.FC<Props> = ({ children }) => {
  return (
    <div>
      <Code className={styles.codeBlock}>{children}</Code>
    </div>
  );
};

export default CodeBlock;
