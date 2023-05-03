import { Meta, Story } from '@storybook/react';
import Footer from 'component/footer/Footer';
import { ComponentProps } from 'react';
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

type StoryProps = ComponentProps<typeof Footer>;

const story: Meta<StoryProps> = {
  decorators: [Decorator.recoilRoot(initializeState)],
};

export default story;

const Template: Story<StoryProps> = () => {
  return <Footer />;
};

export const Default = Template.bind({});
