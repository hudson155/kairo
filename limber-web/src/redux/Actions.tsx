import AuthActions from './auth/AuthActions';
import OrgActions from './org/OrgActions';
import TenantActions from './tenant/TenantActions';
import UserActions from './user/UserActions';

const Actions = {
  auth: AuthActions,
  org: OrgActions,
  tenant: TenantActions,
  user: UserActions,
};

export default Actions;
