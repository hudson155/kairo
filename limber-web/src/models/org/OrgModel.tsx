import FeatureModel from './FeatureModel';
import MembershipModel from './MembershipModel';

export default interface OrgModel {
  id: string;
  name: string;
  features: FeatureModel[];
  members: MembershipModel[];
}
