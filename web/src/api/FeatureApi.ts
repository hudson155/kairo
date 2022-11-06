import Api, { apiState } from 'api/Api';
import { selector } from 'recoil';
import FeatureRep from 'rep/FeatureRep';

class FeatureApi {
  private readonly api: Api;

  constructor(api: Api) {
    this.api = api;
  }

  async getByOrganization(organizationGuid: string): Promise<FeatureRep[]> {
    const path = `/organizations/${organizationGuid}/features`;
    return await this.api.request<FeatureRep[]>({ method: `GET`, path });
  }
}

export const featureApiState = selector<FeatureApi>({
  key: `api/feature`,
  get: ({ get }) => {
    const api = get(apiState({ authenticated: true }));
    return new FeatureApi(api);
  },
});
