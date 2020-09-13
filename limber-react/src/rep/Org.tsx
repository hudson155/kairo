import { FeatureRepComplete } from './Feature';

export class OrgRepComplete {
  public readonly guid: string;
  public readonly name: string;
  public readonly features: FeatureRepComplete[];

  constructor(guid: string, name: string, features: FeatureRepComplete[]) {
    this.guid = guid;
    this.name = name;
    this.features = features;
  }
}
