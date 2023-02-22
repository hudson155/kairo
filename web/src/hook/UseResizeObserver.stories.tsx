import { ComponentMeta, Story } from '@storybook/react';
import Paragraph from 'component/text/Paragraph';
import { useResizeObserver } from 'hook/useResizeObserver';
import React, { ComponentProps } from 'react';

export default {} as ComponentMeta<typeof Impl>;

const Template: Story<ComponentProps<typeof Impl>> = () => {
  return <Impl />;
};

export const Default = Template.bind({});

const Impl: React.FC = () => {
  const isSmall = useResizeObserver((size) => size < 992);
  return <Paragraph>{isSmall ? 'Small' : 'Large'}</Paragraph>;
};
