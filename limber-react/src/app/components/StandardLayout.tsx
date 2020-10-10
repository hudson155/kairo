import { CSSObject } from '@emotion/core';
import React, { ReactElement, ReactNode, Suspense } from 'react';

import { EmotionTheme } from '../EmotionTheme';

// TODO (ENG-81): Properly implement and style this component to support navigation component etc.
const styles = {
  root: (theme: EmotionTheme): CSSObject => ({
    marginLeft: theme.size.$64,
    marginRight: theme.size.$64,
  }),
};

interface Props {
  readonly children: ReactNode;
}

function StandardLayout(props: Props): ReactElement {
  return (
    <div css={theme => styles.root(theme)}>
      <Suspense fallback={<p>Loading</p>}>
        {props.children}
      </Suspense>
    </div>
  );
}

export default StandardLayout;
