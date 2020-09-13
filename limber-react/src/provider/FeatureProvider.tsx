import React, { useContext } from 'react';
import { FeatureRepComplete } from '../rep/Feature';

const Context = React.createContext<FeatureRepComplete>(
  undefined as unknown as FeatureRepComplete,
);

const FeatureProvider = Context.Provider;

export default FeatureProvider;

export const useFeature = () => useContext(Context);
