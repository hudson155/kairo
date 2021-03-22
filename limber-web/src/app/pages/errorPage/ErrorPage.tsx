import React from 'react';
import { useDebugSettings } from '../../../provider/DebugSettingsProvider';

interface ErrorPageProps {
  readonly debugMessage: string;
}

const ErrorPage: React.FC<ErrorPageProps> = ({ debugMessage }) => {
  const { showDebugMessages } = useDebugSettings();

  return (
    <>
      <p>Something went wrong while loading the page. If this continues, please contact us.</p>
      {showDebugMessages ? <p>{debugMessage}</p> : null}
    </>
  );
};

export default ErrorPage;
