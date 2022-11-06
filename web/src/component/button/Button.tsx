import classNames from 'classnames';
import React, { HTMLAttributes, ReactNode } from 'react';
import styles from './Button.module.scss';

interface Props extends HTMLAttributes<HTMLButtonElement> {
  type?: 'submit' | 'reset' | 'button';
  variant: 'unstyled';
  children: ReactNode;
}

/**
 * This is a semantic button element
 * that should be used for pretty much anything clickable that isn't a link.
 *
 * It currently only has an unstyled variant, but this will change in the future.
 */
const Button: React.ForwardRefRenderFunction<HTMLButtonElement, Props> =
  ({ type = `button`, className, children, ...props }, ref) => {
    return (
      <button ref={ref} className={classNames(styles.button, className)} type={type} {...props}>
        {children}
      </button>
    );
  };

export default React.forwardRef(Button);
