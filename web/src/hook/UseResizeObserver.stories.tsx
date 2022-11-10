import { ComponentMeta, ComponentStory } from '@storybook/react';
import Text from 'component/text/Text';
import React from 'react';
import { useResizeObserver } from './useResizeObserver';

const Story: React.FC = () => {
  const isSmall = useResizeObserver((size) => size < 992);
  return <Text>{isSmall ? `Small` : `Large`}</Text>;
};

export default {
  title: `hook/useResizeObserver`,
} as ComponentMeta<typeof Story>;

const Template: ComponentStory<typeof Story> = () => {
  return <Story />;
};

export const Default = Template.bind({});
