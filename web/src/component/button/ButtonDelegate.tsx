import classNames from 'classnames';
import styles from 'component/button/ButtonDelegate.module.scss';
import ButtonOverlay from 'component/button/ButtonOverlay';
import React, { ReactNode } from 'react';

type Type = 'submit' | 'button';

export type Variant = 'primary' | 'unstyled';

export interface Props extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  isSubmitting: boolean;
  type: Type;
  variant: Variant;
  children: ReactNode;
}

/**
 * Although all props are destructured, [...props] is propagated in the button components
 * to support library code that passes arbitrary props (such as Headless UI).
 */
const ButtonDelegate: React.ForwardRefRenderFunction<HTMLButtonElement, Props> =
  ({
    className = undefined,
    disabled = undefined,
    isSubmitting,
    variant,
    children,
    ...props
  }, ref) => {
    return (
      <button
        ref={ref}
        className={classNames(styles.button, variantClassName(variant), className)}
        disabled={disabled}
        {...props}
      >
        {children}
        {disabled || isSubmitting ? <ButtonOverlay isSubmitting={isSubmitting} /> : null}
      </button>
    );
  };

export default React.forwardRef(ButtonDelegate);

const variantClassName = (variant: Variant): string | undefined => {
  switch (variant) {
  case 'primary':
    return styles.primary;
  case 'unstyled':
    return styles.unstyled;
  default:
    throw new Error(`Unsupported variant: ${variant}.`);
  }
};
