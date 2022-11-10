import { ComponentMeta, ComponentStory } from '@storybook/react';
import Button from 'component/button/Button';
import SideNav from 'component/sideNav/SideNav';
import SideNavEntry from 'component/sideNav/SideNavEntry';
import TopNav from 'component/topNav/TopNav';
import React, { useState } from 'react';
import { BrowserRouter } from 'react-router-dom';
import { RecoilRoot } from 'recoil';
import BaseLayout from './BaseLayout';

export default {
  title: `layout/BaseLayout`,
  decorators: [
    (story) => (
      <RecoilRoot>
        <BrowserRouter>{story()}</BrowserRouter>
      </RecoilRoot>
    ),
  ],
} as ComponentMeta<typeof BaseLayout>;

const Template: ComponentStory<typeof BaseLayout> = () => {
  const [sideNavIsOpen, setSideNavIsOpen] = useState(false);

  const toggleSideNav = () => setSideNavIsOpen((currVal) => !currVal);

  return (
    <BaseLayout sideNav={<SideNavImpl isOpen={sideNavIsOpen} setIsOpen={setSideNavIsOpen} />} topNav={<TopNavImpl />}>
      {/* TODO: Use a styled button, once they exist. */}
      <Button variant="unstyled" onClick={toggleSideNav}>{sideNavIsOpen ? `Close` : `Open`}</Button>
    </BaseLayout>
  );
};

export const Default = Template.bind({});

const SideNavImpl: React.FC<{ isOpen: boolean, setIsOpen: (isOpen: boolean) => void }> = ({ isOpen, setIsOpen }) => {
  return (
    <SideNav isOpen={isOpen} setIsOpen={setIsOpen}>
      <SideNavEntry label="First" to="/first" />
      <SideNavEntry label="Second" to="/second" />
    </SideNav>
  );
};

const TopNavImpl: React.FC = () => {
  return <TopNav left="Left" right="Right" />;
};
