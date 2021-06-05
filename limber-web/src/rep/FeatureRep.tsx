namespace FeatureRep {
  export interface Complete {
    readonly guid: string;
    readonly name: string;
    readonly path: string;
    readonly isDefaultFeature: boolean;
  }
}

export default FeatureRep;
