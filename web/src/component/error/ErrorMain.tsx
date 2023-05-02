import ErrorContent from 'component/error/ErrorContent';
import HeaderSection from 'component/section/HeaderSection';
import React, { ReactNode } from 'react';

interface Props {
  error: Error;
}

/**
 * This error component should be shown when something goes wrong at the page-level.
 */
const ErrorMain: React.FC<Props> = ({ error }) => {
  return (
    <HeaderSection title="Error">
      <ErrorContent error={error} message="Something went wrong while loading the page." />
    </HeaderSection>
  );
};

const fallback = (error: Error): ReactNode => <ErrorMain error={error} />;

export default Object.assign(ErrorMain, { fallback });
