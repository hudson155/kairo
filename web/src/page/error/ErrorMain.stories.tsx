import { ComponentMeta, Story } from '@storybook/react';
import { ComponentProps, useRef } from 'react';
import * as Decorator from 'story/Decorator';
import ErrorMain from './ErrorMain';

export default {
  decorators: [Decorator.helmetProvider()],
} as ComponentMeta<typeof ErrorMain>;

const Template: Story<ComponentProps<typeof ErrorMain>> = () => {
  const error = useRef(new Error('Something went wrong!'));
  return <ErrorMain error={error.current} />;
};

export const Default = Template.bind({});
