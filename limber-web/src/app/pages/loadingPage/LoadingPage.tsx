import React from 'react';
import { useDebugSettings } from '../../../provider/DebugSettingsProvider';

interface LoadingPageProps {
  readonly debugMessage: string;
}

const LoadingPage: React.FC<LoadingPageProps> = ({ debugMessage }) => {
  const { showDebugMessages } = useDebugSettings();

  return (
    <>
      <p>Loading...</p>
      {showDebugMessages ? <p>{debugMessage}</p> : null}
    </>
  );
};

export default LoadingPage;
