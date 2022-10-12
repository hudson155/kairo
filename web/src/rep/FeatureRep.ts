export default interface FeatureRep {
  organizationGuid: string;
  guid: string;
  isDefault: boolean;
  type: 'Placeholder' | 'Forms';
  rootPath: string;
}
