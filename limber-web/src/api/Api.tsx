import OrgsApi from './orgs/OrgsApi';
import TenantsApi from './tenants/TenantsApi';
import UsersApi from './users/UsersApi';

const Api = {
  orgs: OrgsApi,
  tenants: TenantsApi,
  users: UsersApi,
};

export default Api;
