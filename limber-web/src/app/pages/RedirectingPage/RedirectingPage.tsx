import React from 'react';
import { useDebugSettings } from '../../../provider/DebugSettingsProvider';

interface RedirectingPageProps {
  readonly debugMessage: string;
}

/**
 * This full-page redirecting page should be used when redirecting to an external location.
 *
 * TODO: This page is pretty ugly. Make it look nicer.
 */
const RedirectingPage: React.FC<RedirectingPageProps> = ({ debugMessage }) => {
  const { showDebugMessages } = useDebugSettings();
  return (
    <>
      <p>Redirecting...</p>
      {showDebugMessages ? <p>{debugMessage}</p> : null}
    </>
  );
};

export default RedirectingPage;
