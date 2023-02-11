import Api, { apiState } from 'api/Api';
import { selector } from 'recoil';
import FeatureRep from 'rep/FeatureRep';

class FeatureApi {
  private readonly api: Api;

  constructor(api: Api) {
    this.api = api;
  }

  async listByOrganization(organizationGuid: string): Promise<FeatureRep[]> {
    return await this.api.request<FeatureRep[]>({
      method: 'GET',
      path: `/organizations/${organizationGuid}/features`,
    });
  }
}

export const featureApiState = selector<FeatureApi>({
  key: 'api/feature',
  get: ({ get }) => {
    const api = get(apiState({ authenticated: true }));
    return new FeatureApi(api);
  },
});
