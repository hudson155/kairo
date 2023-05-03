import { Meta, Story } from '@storybook/react';
import ErrorPage from 'page/error/ErrorPage';
import { ComponentProps, useRef } from 'react';
import * as Decorator from 'story/Decorator';

type StoryProps = ComponentProps<typeof ErrorPage>;

const story: Meta<StoryProps> = {
  decorators: [
    Decorator.recoilRoot(),
    Decorator.helmetProvider(),
  ],
};

export default story;

const Template: Story<StoryProps> = () => {
  const error = useRef(new Error('A terrible error (this is the error message).'));
  return <ErrorPage error={error.current} />;
};

export const Default = Template.bind({});
