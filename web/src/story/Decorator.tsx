/* eslint-disable react/display-name */

import { DecoratorFunction } from '@storybook/csf/dist/story';
import { ReactFramework } from '@storybook/react';
import { HelmetProvider } from 'react-helmet-async';
import { BrowserRouter } from 'react-router-dom';
import { MutableSnapshot, RecoilRoot } from 'recoil';

export const browserRouter: () => DecoratorFunction<ReactFramework> =
  () => (story) => {
    return <BrowserRouter>{story()}</BrowserRouter>;
  };

export const helmetProvider: () => DecoratorFunction<ReactFramework> =
  () => (story) => {
    return <HelmetProvider>{story()}</HelmetProvider>;
  };

export const recoilRoot: (
  initializeState?: (mutableSnapshot: MutableSnapshot) => void,
) => DecoratorFunction<ReactFramework> =
  (initializeState) => (story) => {
    return <RecoilRoot initializeState={initializeState}>{story()}</RecoilRoot>;
  };
