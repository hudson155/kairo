import OrgsApi from './orgs/Api';
import TenantsApi from './tenants/Api';
import UsersApi from './users/Api';

const Api = {
  orgs: OrgsApi,
  tenants: TenantsApi,
  users: UsersApi,
};
export default Api;
