import { ComponentMeta, ComponentStory } from '@storybook/react';
import Text from 'component/text/Text';
import React from 'react';
import Icon, { Size } from './Icon';

export default {
  title: `Icon`,
} as ComponentMeta<typeof Icon>;

const Template: ComponentStory<typeof Icon> = () => {
  return (
    <>
      <Text>These are just some examples. It isn&apos;t comprehensive</Text>
      <Icons size="small" />
      <Icons />
      <Icons size="large" />
      <Icons size="extraLarge" />
    </>
  );
};

export const Default = Template.bind({});

const Icons: React.FC<{ size?: Size }> = ({ size = undefined }) => (
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
