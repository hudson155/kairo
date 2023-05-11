import ButtonDelegate, { Props as DelegateProps } from 'component/button/ButtonDelegate';
import React from 'react';

export interface Props extends Omit<DelegateProps, 'onClick' | 'type' | 'variant'> {
  variant?: 'primary' | 'danger';
}

const SubmitButtonDelegate: React.ForwardRefRenderFunction<HTMLButtonElement, Props> =
  ({ variant = 'primary', ...props }, ref) => {
    return <ButtonDelegate ref={ref} type="submit" variant={variant} {...props} />;
  };

export default React.forwardRef(SubmitButtonDelegate);
