import ButtonDelegate from 'component/button/ButtonDelegate';
import { doNothing } from 'helper/doNothing';
import React, { ReactNode } from 'react';

interface Props {
  className?: string;
  isSubmitting: boolean;
  children: ReactNode;
}

const SubmitButton: React.ForwardRefRenderFunction<HTMLButtonElement, Props> =
  ({ className = undefined, isSubmitting, children }, ref) => {
    return (
      <ButtonDelegate
        ref={ref}
        className={className}
        isSubmitting={isSubmitting}
        type="submit"
        variant="primary"
        onClick={doNothing}
      >
        {children}
      </ButtonDelegate>
    );
  };

export default React.forwardRef(SubmitButton);
