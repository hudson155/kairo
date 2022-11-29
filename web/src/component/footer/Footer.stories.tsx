import { ComponentMeta, ComponentStory } from '@storybook/react';
import { MutableSnapshot } from 'recoil';
import organizationAuth from 'state/core/organizationAuth';
import * as Decorator from 'story/Decorator';
import Footer from './Footer';

const initializeState = ({ set }: MutableSnapshot): void => {
  set(organizationAuth, {
    organizationGuid: crypto.randomUUID(),
    guid: crypto.randomUUID(),
    auth0OrganizationId: `org_abcdefghijklmnop`,
  });
};

export default {
  decorators: [Decorator.recoilRoot(initializeState)],
} as ComponentMeta<typeof Footer>;

const Template: ComponentStory<typeof Footer> = () => {
  return <Footer />;
};

export const Default = Template.bind({});
