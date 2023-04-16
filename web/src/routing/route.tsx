import React, { ReactNode } from 'react';
import * as ReactRouter from 'react-router-dom';

interface Route {
  path: string,
  route: ReactNode;
}

interface Path {
  path: string;
  nesting?: boolean;
}

/**
 * Defines routes in a standardized way.
 *
 * @param Page is the component to render for the page.
 * Most of the time, this should be a lazily-imported component.
 *
 * @param path is the path for the page.
 * It has a path string, and can also optionally allow nesting.
 */
// eslint-disable-next-line @typescript-eslint/naming-convention
const route = (Page: React.FC, path: Path): Route => {
  return {
    path: path.path,
    route: <ReactRouter.Route element={<Page />} path={routePath(path)} />,
  };
};

export default route;

const routePath = ({ path, nesting = false }: Path): string => {
  let result = path;
  if (nesting) result += '/*';
  return result;
};
