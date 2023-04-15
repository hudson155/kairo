import ButtonDelegate, { Props as DelegateProps } from 'component/button/ButtonDelegate';
import React from 'react';

export type Props = Omit<DelegateProps, 'onClick' | 'type' | 'variant'>;

const SubmitButtonDelegate: React.ForwardRefRenderFunction<HTMLButtonElement, Props> =
  ({ ...props }, ref) => {
    return <ButtonDelegate ref={ref} type="submit" variant="primary" {...props} />;
  };

export default React.forwardRef(SubmitButtonDelegate);
