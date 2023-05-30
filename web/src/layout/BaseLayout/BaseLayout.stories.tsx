import { Meta, Story } from '@storybook/react';
import Button from 'component/button/Button';
import SideNav from 'component/sideNav/SideNav';
import SideNavEntry from 'component/sideNav/SideNavEntry';
import TopNav from 'component/topNav/TopNav';
import BaseLayout from 'layout/BaseLayout/BaseLayout';
import React, { ComponentProps, useState } from 'react';
import { MutableSnapshot } from 'recoil';
import organizationAuth from 'state/core/organizationAuth';
import * as Decorator from 'story/Decorator';

const initializeState = ({ set }: MutableSnapshot): void => {
  set(organizationAuth, {
    organizationId: 'org_0',
    id: 'auth_0',
    auth0OrganizationId: 'org_yDiVK18hoeddya8J',
    auth0OrganizationName: 'acme-co',
  });
};

type StoryProps = ComponentProps<typeof BaseLayout>;

const story: Meta<StoryProps> = {
  decorators: [
    Decorator.recoilRoot(initializeState),
    Decorator.browserRouter(),
  ],
};

export default story;

const Template: Story<StoryProps> = () => {
  const [sideNavIsOpen, setSideNavIsOpen] = useState(false);

  const toggleSideNav = () => setSideNavIsOpen((currVal) => !currVal);

  return (
    <BaseLayout sideNav={<SideNavImpl isOpen={sideNavIsOpen} setIsOpen={setSideNavIsOpen} />} topNav={<TopNavImpl />}>
      <Button variant="primary" onClick={toggleSideNav}>{sideNavIsOpen ? 'Close' : 'Open'}</Button>
    </BaseLayout>
  );
};

export const Default = Template.bind({});

interface Props {
  isOpen: boolean;
  setIsOpen: (isOpen: boolean) => void;
}

const SideNavImpl: React.FC<Props> = ({ isOpen, setIsOpen }) => {
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
