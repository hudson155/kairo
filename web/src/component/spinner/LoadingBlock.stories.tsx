import { ComponentMeta, Story } from '@storybook/react';
import LoadingBlock from 'component/spinner/LoadingBlock';
import { ComponentProps } from 'react';

export default {} as ComponentMeta<typeof LoadingBlock>;

const Template: Story<ComponentProps<typeof LoadingBlock>> = () => {
  return <LoadingBlock />;
};

export const Default = Template.bind({});
