import { ComponentMeta, Story } from '@storybook/react';
import Paragraph from 'component/text/Paragraph';
import React, { ComponentProps } from 'react';
import Icon, { Size } from './Icon';

export default {} as ComponentMeta<typeof Icon>;

const Template: Story<ComponentProps<typeof Icon>> = () => {
  return (
    <>
      <Paragraph>These are just some examples. It isn&apos;t comprehensive</Paragraph>
      <Icons size="small" />
      <Icons />
      <Icons size="large" />
      <Icons size="extraLarge" />
    </>
  );
};

export const Default = Template.bind({});

const Icons: React.FC<{ size?: Size }> = ({ size = undefined }) => {
  return (
    <div>
      <Icon name="search" size={size} />
      <Icon name="home" size={size} />
      <Icon name="menu" size={size} />
      <Icon name="close" size={size} />
      <Icon name="settings" size={size} />
      <Icon name="expand_more" size={size} />
      <Icon name="done" size={size} />
      <Icon name="check_circle" size={size} />
    </div>
  );
};
