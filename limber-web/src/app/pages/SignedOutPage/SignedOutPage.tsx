import React from 'react';
import { Link } from 'react-router-dom';
import app from '../../../app';

export const signedOutPagePath = () => '/signed-out';

/**
 * TODO: This page is pretty ugly. Make it look nicer.
 */
const SignedOutPage: React.FC = () => {
  return <p>
    You've been signed out.
    <Link to={app.rootPath}>Click here to sign back in.</Link>
  </p>;
};

export default SignedOutPage;
