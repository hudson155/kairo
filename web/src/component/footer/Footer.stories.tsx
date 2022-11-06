import { ComponentMeta, ComponentStory } from '@storybook/react';
import { MutableSnapshot, RecoilRoot } from 'recoil';
import organizationAuth from 'state/core/organizationAuth';
import Footer from './Footer';

const initializeState = ({ set }: MutableSnapshot): void => {
  set(organizationAuth, {
    organizationGuid: crypto.randomUUID(),
    guid: crypto.randomUUID(),
    auth0OrganizationId: `org_abcdefghijklmnop`,
  });
};

export default {
  title: `Footer`,
  decorators: [(story) => <RecoilRoot initializeState={initializeState}>{story()}</RecoilRoot>],
} as ComponentMeta<typeof Footer>;

const Template: ComponentStory<typeof Footer> = () => {
  return <Footer />;
};

export const Default = Template.bind({});
