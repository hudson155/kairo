import { ComponentMeta, ComponentStory } from '@storybook/react';
import ErrorMain from 'page/error/ErrorMain';
import { useRef } from 'react';
import * as Decorator from 'story/Decorator';

export default {
  title: `page/ErrorMain`,
  decorators: [Decorator.helmetProvider()],
} as ComponentMeta<typeof ErrorMain>;

const Template: ComponentStory<typeof ErrorMain> = () => {
  const error = useRef(new Error(`Something went wrong!`));
  return <ErrorMain error={error.current} />;
};

export const Default = Template.bind({});
