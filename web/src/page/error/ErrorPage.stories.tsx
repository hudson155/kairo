import { ComponentMeta, Story } from '@storybook/react';
import ErrorPage from 'page/error/ErrorPage';
import { ComponentProps, useRef } from 'react';
import * as Decorator from 'story/Decorator';

export default {
  decorators: [
    Decorator.recoilRoot(),
    Decorator.helmetProvider(),
  ],
} as ComponentMeta<typeof ErrorPage>;

const Template: Story<ComponentProps<typeof ErrorPage>> = () => {
  const error = useRef(new Error('A terrible error (this is the error message).'));
  return <ErrorPage error={error.current} />;
};

export const Default = Template.bind({});
