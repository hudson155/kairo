import { ComponentMeta, ComponentStory } from '@storybook/react';
import Paragraph from 'component/text/Paragraph';
import React from 'react';
import { useResizeObserver } from './useResizeObserver';

const Story: React.FC = () => {
  const isSmall = useResizeObserver((size) => size < 992);
  return <Paragraph>{isSmall ? `Small` : `Large`}</Paragraph>;
};

export default {} as ComponentMeta<typeof Story>;

const Template: ComponentStory<typeof Story> = () => {
  return <Story />;
};

export const Default = Template.bind({});
