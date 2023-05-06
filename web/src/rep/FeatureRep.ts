export type FeatureType = 'Placeholder' | 'Form';

export default interface FeatureRep {
  guid: string;
  organizationGuid: string;
  isDefault: boolean;
  type: FeatureType;
  name: string;
  iconName: string | null;
  rootPath: string;
}
