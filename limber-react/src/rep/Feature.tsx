type Type = 'FORMS' | 'HOME';

export class FeatureRepComplete {
  public readonly guid: string;
  public readonly rank: number;
  public readonly name: string;
  public readonly path: string;
  public readonly type: Type;
  public readonly isDefaultFeature: boolean;

  constructor(guid: string, rank: number, name: string, path: string, type: Type, isDefaultFeature: boolean) {
    this.guid = guid;
    this.rank = rank;
    this.name = name;
    this.path = path;
    this.type = type;
    this.isDefaultFeature = isDefaultFeature;
  }
}

export function getDefaultFeature(features: FeatureRepComplete[]): FeatureRepComplete | undefined {
  const defaultFeatures = features.filter(it => it.isDefaultFeature).sort((a, b) => a.rank - b.rank);
  if (defaultFeatures.length === 0) return undefined;
  return defaultFeatures[0];
}
