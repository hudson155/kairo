import React, { PropsWithChildren, ReactNode } from 'react';

interface Props extends PropsWithChildren {
  fallback: (error: Error) => ReactNode;
  children: ReactNode;
}

interface State {
  error: Error | undefined;
}

/**
 * See https://reactjs.org/docs/error-boundaries.html. This particular error boundary implementation
 * uses a fallback function that returns a ReactNode, allowing the fallback to see the error itself.
 */
export default class ErrorBoundary extends React.Component<Props, State> {
  static getDerivedStateFromError(error: Error) {
    return { error };
  }

  override state: State = { error: undefined };

  override render() {
    const { fallback, children } = this.props;
    const { error } = this.state;
    if (error) return fallback(error);
    return children;
  }
}
