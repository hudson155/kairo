import React from 'react';
import { useDebugSettings } from '../../provider/DebugSettingsProvider';

interface LoadingPageProps {
  readonly debugMessage: string;
}

/**
 * This full-page loading page should only be used when the entire app is loading. If an individual
 * component is loading, a local loading state is more appropriate.
 *
 * TODO: This page is pretty ugly. Make it look nicer. Using ADDITIONAL_LIMBER_API_LATENCY_MS in the
 *  .env file is one way to artificially slow down loading time. It might be nice to avoid showing
 *  any components until 500ms have passed or something (the "fake progress bar" concept).
 */
const LoadingPage: React.FC<LoadingPageProps> = ({ debugMessage }) => {
  const { showDebugMessages } = useDebugSettings();
  return (
    <>
      <p>Loading...</p>
      {showDebugMessages && <p>{debugMessage}</p>}
    </>
  );
};

export default LoadingPage;
