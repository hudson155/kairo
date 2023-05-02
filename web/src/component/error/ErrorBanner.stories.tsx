import { ComponentMeta, Story } from '@storybook/react';
import ErrorBanner from 'component/error/ErrorBanner';
import { ComponentProps, useRef } from 'react';
import * as Decorator from 'story/Decorator';

export default {
  decorators: [Decorator.helmetProvider()],
} as ComponentMeta<typeof ErrorBanner>;

const Template: Story<ComponentProps<typeof ErrorBanner>> = () => {
  const error = useRef(new Error('A terrible error (this is the error message).'));
  return <ErrorBanner error={error.current} operation="doing something" />;
};

export const Default = Template.bind({});
