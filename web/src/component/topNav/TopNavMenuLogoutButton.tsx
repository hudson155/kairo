import Button, { Props as ButtonProps } from 'component/button/Button';
import Icon from 'component/icon/Icon';
import React, { MouseEvent } from 'react';
import { useRecoilValueLoadable } from 'recoil';
import auth0ClientState from 'state/auth/auth0Client';

type Props = Omit<ButtonProps, | 'variant' | 'children'>;

const TopNavMenuLogoutButton: React.ForwardRefRenderFunction<HTMLButtonElement, Props> =
  ({ onClick = undefined, ...props }, ref) => {
    const auth0 = useRecoilValueLoadable(auth0ClientState).valueMaybe();

    if (!auth0) return null;

    const handleClick = (event: MouseEvent<HTMLButtonElement>) => {
      onClick?.(event);
      void auth0.logout();
    };

    return (
      <Button ref={ref} variant="unstyled" onClick={handleClick} {...props}>
        <Icon name="logout" size="small" />
        {'Log out'}
      </Button>
    );
  };

export default React.forwardRef(TopNavMenuLogoutButton);
