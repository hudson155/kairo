import FeatureRep from './FeatureRep';

namespace OrgRep {
  export interface Complete {
    readonly guid: string;
    readonly name: string;
    readonly features: FeatureRep.Complete[];
  }
}

export default OrgRep;
