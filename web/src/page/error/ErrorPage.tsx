import BaseLayout from 'layout/BaseLayout/BaseLayout';
import ErrorMain from 'page/error/ErrorMain';
import React, { ReactNode } from 'react';

interface Props {
  error: Error;
}

/**
 * This full-page error page should only be used when something goes terribly wrong and a partial
 * state can't even be shown.
 */
const ErrorPage: React.FC<Props> = ({ error }) => {
  return (
    <BaseLayout sideNav={null} topNav={false}>
      <ErrorMain error={error} />
    </BaseLayout>
  );
};

const fallback = (error: Error): ReactNode => <ErrorPage error={error} />;

export default Object.assign(ErrorPage, { fallback });
