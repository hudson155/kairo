import Button from 'component/button/Button';
import Icon from 'component/icon/Icon';
import React from 'react';
import { useRecoilValueLoadable } from 'recoil';
import auth0ClientState from 'state/auth/auth0Client';

interface Props {
  className: string;
}

const TopNavMenuLogoutButton: React.ForwardRefRenderFunction<HTMLButtonElement, Props> =
  ({ className }, ref) => {
    const auth0 = useRecoilValueLoadable(auth0ClientState).valueMaybe();

    if (!auth0) return null;

    return (
      <Button ref={ref} className={className} variant="unstyled" onClick={() => void auth0.logout()}>
        <Icon name="logout" size="small" space="after" />
        {'Log out'}
      </Button>
    );
  };

export default React.forwardRef(TopNavMenuLogoutButton);
