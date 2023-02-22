import { ComponentMeta, Story } from '@storybook/react';
import Spinner from 'component/spinner/Spinner';
import { ComponentProps } from 'react';

export default {} as ComponentMeta<typeof Spinner>;

const Template: Story<ComponentProps<typeof Spinner>> = () => {
  return <Spinner />;
};

export const Default = Template.bind({});
