export type FeatureType = 'Placeholder' | 'Form';

export default interface FeatureRep {
  organizationGuid: string;
  guid: string;
  isDefault: boolean;
  type: FeatureType;
  name: string;
  iconName: string | null;
  rootPath: string;
}
