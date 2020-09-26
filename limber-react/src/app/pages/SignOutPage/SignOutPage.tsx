import { useAuth0 } from '@auth0/auth0-react';
import { ReactElement } from 'react';

import { app } from '../../../app';

function SignOutPage(): ReactElement | null {
  const auth = useAuth0();
  auth.logout({ returnTo: app.rootUrl });
  return null;
}

export default SignOutPage;
