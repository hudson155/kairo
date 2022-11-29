import { ComponentMeta, ComponentStory } from '@storybook/react';
import Paragraph from 'component/text/Paragraph';
import CodeBlock from './CodeBlock';

export default {} as ComponentMeta<typeof CodeBlock>;

const Template: ComponentStory<typeof CodeBlock> = () => {
  const stackTrace = `Exception in thread "main" java.lang.NullPointerException\n` +
    `        at com.example.myproject.Book.getTitle(Book.java:16)\n` +
    `        at com.example.myproject.Author.getBookTitles(Author.java:25)\n` +
    `        at com.example.myproject.Bootstrap.main(Bootstrap.java:14)`;

  return (
    <>
      <Paragraph>{`Something went wrong while starting the app. Here's the error.`}</Paragraph>
      <CodeBlock>{stackTrace}</CodeBlock>
    </>
  );
};

export const Default = Template.bind({});
