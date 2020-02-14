import FeatureModel from './FeatureModel';

export default interface OrgModel {
  id: string;
  name: string;
  features: FeatureModel[];
}
