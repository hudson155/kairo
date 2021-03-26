import React, { ReactNode } from 'react';

interface ErrorBoundaryProps {
  readonly fallback: (error: Error) => ReactNode;
}

interface ErrorBoundaryState {
  readonly error?: Error;
}

/**
 * See https://reactjs.org/docs/error-boundaries.html. This particular error boundary implementation
 * uses a fallback function that returns a ReactNode, allowing the fallback to see the error itself.
 */
export default class ErrorBoundary extends React.Component<ErrorBoundaryProps, ErrorBoundaryState> {
  state = { error: undefined };

  static getDerivedStateFromError(error: Error) {
    return { error };
  }

  render() {
    const error = this.state.error;
    if (error !== undefined) return this.props.fallback(error);
    return this.props.children;
  }
}
