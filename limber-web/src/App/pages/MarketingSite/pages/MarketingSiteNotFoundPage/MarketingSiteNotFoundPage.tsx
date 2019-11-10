import React from 'react';
import { Link } from 'react-router-dom';

const MarketingSiteNotFoundPage: React.FC = () => {
  return <>
    <h1>Not Found</h1>
    <p>We looked everywhere, but we couldn&apos;t find the page you were looking for.</p>
    <p><Link to="/signin">Click here to sign in</Link></p>
    <p><Link to="/">Click here to go home</Link></p>
  </>;
};

export default MarketingSiteNotFoundPage;
