export type FeatureType = 'Placeholder' | 'Form';

export default interface FeatureRep {
  id: string;
  organizationId: string;
  isDefault: boolean;
  type: FeatureType;
  name: string;
  iconName: string | null;
  rootPath: string;
}
