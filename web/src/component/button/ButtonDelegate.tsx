import classNames from 'classnames';
import React, { FocusEventHandler, MouseEventHandler, PointerEventHandler, ReactNode } from 'react';
import styles from './Button.module.scss';

type Type = 'submit' | 'reset' | 'button';

export type Variant = 'primary' | 'unstyled';

interface Props {
  className?: string;
  isSubmitting?: boolean;
  type: Type;
  variant: Variant;
  onBlur?: FocusEventHandler<HTMLButtonElement>;
  onClick: MouseEventHandler<HTMLButtonElement>;
  onFocus?: FocusEventHandler<HTMLButtonElement>;
  onMouseEnter?: PointerEventHandler<HTMLButtonElement>;
  onMouseLeave?: PointerEventHandler<HTMLButtonElement>;
  children: ReactNode;
}

const ButtonDelegate: React.ForwardRefRenderFunction<HTMLButtonElement, Props> =
  ({
    className = undefined,
    isSubmitting = false,
    type,
    variant,
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
        className={classNames(styles.button, variantClassName(variant), className)}
        disabled={isSubmitting}
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

export default React.forwardRef(ButtonDelegate);

export const variantClassName = (variant: Variant): string | undefined => {
  switch (variant) {
  case `unstyled`:
    return undefined;
  case `primary`:
    return styles.primary;
  default:
    throw new Error(`Unsupported variant: ${variant}.`);
  }
};
