import { Meta, Story } from '@storybook/react';
import CodeBlock from 'component/code/CodeBlock';
import Container from 'component/container/Container';
import Paragraph from 'component/text/Paragraph';
import { ComponentProps } from 'react';

type StoryProps = ComponentProps<typeof CodeBlock>;

const story: Meta<StoryProps> = {};

export default story;

const Template: Story<StoryProps> = () => {
  const stackTrace = 'Exception in thread "main" java.lang.NullPointerException\n' +
    '        at com.example.myproject.Book.getTitle(Book.java:16)\n' +
    '        at com.example.myproject.Author.getBookTitles(Author.java:25)\n' +
    '        at com.example.myproject.Bootstrap.main(Bootstrap.java:14)';

  return (
    <Container direction="vertical">
      <Paragraph>{'Something went wrong while starting the app. Here\'s the error.'}</Paragraph>
      <CodeBlock>{stackTrace}</CodeBlock>
    </Container>
  );
};

export const Default = Template.bind({});
