import { ComponentMeta, ComponentStory } from '@storybook/react';
import Paragraph from 'component/text/Paragraph';
import React from 'react';
import Icon, { Size } from './Icon';

export default {
  title: `component/Icon`,
} as ComponentMeta<typeof Icon>;

const Template: ComponentStory<typeof Icon> = () => {
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
