import OrgsApi from './orgs/Api';
import UsersApi from './users/Api';
import TenantsApi from './tenants/Api';

const Api = {
  orgs: OrgsApi,
  tenants: TenantsApi,
  users: UsersApi,
};

export default Api;
