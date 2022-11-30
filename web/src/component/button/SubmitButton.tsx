import ButtonDelegate from 'component/button/ButtonDelegate';
import ButtonSubmittingOverlay from 'component/button/ButtonSubmittingOverlay';
import doNothing from 'helper/doNothing';
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
        {isSubmitting ? <ButtonSubmittingOverlay /> : null}
      </ButtonDelegate>
    );
  };

export default React.forwardRef(SubmitButton);
