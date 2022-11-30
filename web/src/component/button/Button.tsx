import classNames from 'classnames';
import React, { FocusEventHandler, MouseEventHandler, PointerEventHandler, ReactNode } from 'react';
import styles from './Button.module.scss';

interface Props {
  className?: string;
  type?: 'submit' | 'reset' | 'button';
  variant: 'unstyled'; // eslint-disable-line react/no-unused-prop-types
  onBlur?: FocusEventHandler<HTMLButtonElement>;
  onClick: MouseEventHandler<HTMLButtonElement>;
  onFocus?: FocusEventHandler<HTMLButtonElement>;
  onMouseEnter?: PointerEventHandler<HTMLButtonElement>;
  onMouseLeave?: PointerEventHandler<HTMLButtonElement>;
  children: ReactNode;
}

/**
 * This is a semantic button element
 * that should be used for pretty much anything clickable that isn't a link.
 *
 * It currently only has an unstyled variant, but this will change in the future.
 */
const Button: React.ForwardRefRenderFunction<HTMLButtonElement, Props> =
  ({
    type = `button`,
    className = undefined,
    onBlur = undefined,
    onClick,
    onFocus = undefined,
    onMouseEnter = undefined,
    onMouseLeave = undefined,
    children,
  }, ref) => {
    return (
      <button
        ref={ref}
        className={classNames(styles.button, className)}
        type={type}
        onBlur={onBlur}
        onClick={onClick}
        onFocus={onFocus}
        onMouseEnter={onMouseEnter}
        onMouseLeave={onMouseLeave}
      >
        {children}
      </button>
    );
  };

export default React.forwardRef(Button);
