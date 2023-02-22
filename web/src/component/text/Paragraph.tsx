import classNames from 'classnames';
import { Size, sizeClassName } from 'component/text/Text';
import React, { ReactNode } from 'react';

interface Props {
  className?: string;
  size?: Size; // Don't provide this prop unless you need to override the text size.
  children: ReactNode;
}

/**
 * Use this for paragraphs of text.
 * No need to add [Text] inside, except to change how text looks.
 */
const Paragraph: React.FC<Props> = ({ className = undefined, size = undefined, children }) => {
  return (
    <p className={classNames(sizeClassName(size), className)}>
      {children}
    </p>
  );
};

export default Paragraph;
