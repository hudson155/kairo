import React, { ReactNode } from 'react';
import Page from '../Page/Page';
import AppPageHeader from './components/AppPageHeader/AppPageHeader';
import AppPageFooter from './components/AppPageFooter/AppPageFooter';

interface Props {
  children: ReactNode;
}

const AppPage: React.FC<Props> = (props: Props) => {
  return (
    <Page header={<AppPageHeader />} footer={<AppPageFooter />}>
      <div>{props.children}</div>
    </Page>
  );
};

export default AppPage;
