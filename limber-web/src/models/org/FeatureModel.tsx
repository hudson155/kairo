export default interface FeatureModel {
  id: string;
  name: string;
  path: string;
  type: FeatureModelType;
  isDefaultFeature: boolean;
}

export type FeatureModelType = 'FORMS' | 'HOME';
