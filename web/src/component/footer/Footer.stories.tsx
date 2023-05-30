import { Meta, Story } from '@storybook/react';
import Footer from 'component/footer/Footer';
import { ComponentProps } from 'react';
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

type StoryProps = ComponentProps<typeof Footer>;

const story: Meta<StoryProps> = {
  decorators: [Decorator.recoilRoot(initializeState)],
};

export default story;

const Template: Story<StoryProps> = () => {
  return <Footer />;
};

export const Default = Template.bind({});
