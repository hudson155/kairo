import { ComponentMeta, Story } from '@storybook/react';
import Button from 'component/button/Button';
import SideNav from 'component/sideNav/SideNav';
import SideNavEntry from 'component/sideNav/SideNavEntry';
import TopNav from 'component/topNav/TopNav';
import BaseLayout from 'layout/BaseLayout/BaseLayout';
import React, { ComponentProps, useState } from 'react';
import { MutableSnapshot } from 'recoil';
import organizationAuth from 'state/global/core/organizationAuth';
import * as Decorator from 'story/Decorator';

const initializeState = ({ set }: MutableSnapshot): void => {
  set(organizationAuth, {
    organizationGuid: crypto.randomUUID(),
    guid: crypto.randomUUID(),
    auth0OrganizationId: 'org_yDiVK18hoeddya8J',
    auth0OrganizationName: 'acme-co',
  });
};

export default {
  decorators: [
    Decorator.recoilRoot(initializeState),
    Decorator.browserRouter(),
  ],
} as ComponentMeta<typeof BaseLayout>;

const Template: Story<ComponentProps<typeof BaseLayout>> = () => {
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
