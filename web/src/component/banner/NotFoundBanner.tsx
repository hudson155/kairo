import Banner from 'component/banner/Banner';
import Paragraph from 'component/text/Paragraph';
import React from 'react';

interface Props {
  entity: string;
}

const NotFoundBanner: React.FC<Props> = ({ entity }) => {
  return (
    <Banner variant="danger">
      <Paragraph>{`The ${entity} was not found.`}</Paragraph>
    </Banner>
  );
};

export default NotFoundBanner;
