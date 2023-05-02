import Banner from 'component/banner/Banner';
import ErrorContent from 'component/error/ErrorContent';
import React from 'react';

interface Props {
  error: unknown;
  operation: string;
}

/**
 * This error component should be shown when something goes wrong at a component-level.
 */
const ErrorBanner: React.FC<Props> = ({ error, operation }) => {
  return (
    <Banner variant="danger">
      <ErrorContent error={error} message={`Something went wrong while ${operation}.`} />
    </Banner>
  );
};

export default ErrorBanner;
