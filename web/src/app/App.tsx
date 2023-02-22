/* IMPORTANT: Preserve CSS import order. */
import 'the-new-css-reset/css/reset.css';
import 'index.scss';

import LoadingPage from 'page/loading/LoadingPage';
import React, { Suspense } from 'react';
import { RecoilRoot } from 'recoil';

const AppDelegate = React.lazy(() => import('./AppDelegate'));

/**
 * This component is extremely minimal to provide the quickest possible first paint performance.
 * The Recoil root is included because it doesn't behave well inside a Suspense tag.
 */
const App: React.FC = () => {
  return (
    <RecoilRoot>
      <Suspense fallback={<LoadingPage />}>
        <AppDelegate />
      </Suspense>
    </RecoilRoot>
  );
};

export default App;
