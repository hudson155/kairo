export default interface FeatureModel {
  id: string;
  name: string;
  path: string;
  type: FeatureModelType;
}

enum FeatureModelType {
  FORMS,
  HOME,
}
