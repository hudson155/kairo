export default interface FeatureModel {
  id: string;
  created: Date;
  name: string;
  path: string;
  type: FeatureModelType;
}

enum FeatureModelType {
  FORMS,
  HOME,
}
