import { ComponentMeta, Story } from '@storybook/react';
import Loader from 'component/spinner/Loader';
import { ComponentProps } from 'react';

export default {} as ComponentMeta<typeof Loader>;

const Template: Story<ComponentProps<typeof Loader>> = () => {
  return <Loader />;
};

export const Default = Template.bind({});
