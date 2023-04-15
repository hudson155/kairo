import ButtonDelegate, { Props as DelegateProps } from 'component/button/ButtonDelegate';
import React from 'react';

export type Props = Omit<DelegateProps, 'isSubmitting' | 'type'>;

/**
 * This is a semantic button element
 * that should be used for pretty much anything clickable that isn't a link.
 */
const Button: React.ForwardRefRenderFunction<HTMLButtonElement, Props> =
  ({ ...props }, ref) => {
    return <ButtonDelegate ref={ref} isSubmitting={false} type="button" {...props} />;
  };

export default React.forwardRef(Button);
