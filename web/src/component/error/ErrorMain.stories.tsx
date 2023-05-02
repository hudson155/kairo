import { ComponentMeta, Story } from '@storybook/react';
import ErrorMain from 'component/error/ErrorMain';
import { ComponentProps, useRef } from 'react';
import * as Decorator from 'story/Decorator';

export default {
  decorators: [Decorator.helmetProvider()],
} as ComponentMeta<typeof ErrorMain>;

const Template: Story<ComponentProps<typeof ErrorMain>> = () => {
  const error = useRef(new Error('A terrible error (this is the error message).'));
  return <ErrorMain error={error.current} />;
};

export const Default = Template.bind({});
