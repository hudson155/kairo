import React from 'react';
import { useDebugSettings } from '../../provider/DebugSettingsProvider';

interface Props {
  readonly errorMessage?: string;
  readonly error?: Error;
}

/**
 * This full-page error page should only be used when something goes terribly wrong and a partial
 * state can't even be shown.
 *
 * TODO: This page is pretty ugly. Make it look nicer.
 */
const ErrorPage: React.FC<Props> = ({ errorMessage, error }) => {
  const { showDebugMessages } = useDebugSettings();
  const errorStack = showDebugMessages ? error?.stack : undefined;
  return (
    <>
      <p>{errorMessage || 'Something went wrong while loading the page.'}</p>
      {errorStack && <code>{errorStack}</code>}
    </>
  );
};

export default ErrorPage;
