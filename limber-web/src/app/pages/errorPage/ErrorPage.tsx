import React from 'react';
import { useDebugSettings } from '../../../provider/DebugSettingsProvider';

interface ErrorPageProps {
  readonly error: Error;
}

/**
 * This full-page error page should only be used when something goes terribly wrong and a partial
 * state can't even be shown.
 *
 * TODO: This page is pretty ugly. Make it look nicer. Running the frontend without the backend
 *  running is one way to force this page to show.
 */
const ErrorPage: React.FC<ErrorPageProps> = ({ error }) => {
  const { showDebugMessages } = useDebugSettings();
  return (
    <>
      <p>Something went wrong while loading the page.</p>
      {showDebugMessages ? <code>{error.stack}</code> : null}
    </>
  );
};

export default ErrorPage;
