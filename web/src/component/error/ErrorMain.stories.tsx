import { Meta, Story } from '@storybook/react';
import ErrorMain from 'component/error/ErrorMain';
import { ComponentProps, useRef } from 'react';
import * as Decorator from 'story/Decorator';

type StoryProps = ComponentProps<typeof ErrorMain>;

const story: Meta<StoryProps> = {
  decorators: [Decorator.helmetProvider()],
};
export default story;

const Template: Story<StoryProps> = () => {
  const error = useRef(new Error('A terrible error (this is the error message).'));
  return <ErrorMain error={error.current} />;
};

export const Default = Template.bind({});
