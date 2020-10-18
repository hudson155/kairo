import { CSSObject } from '@emotion/core';
import { graphql } from 'babel-plugin-relay/macro';
import React, { ReactElement } from 'react';
import { useFragment } from 'react-relay/hooks';

import { EmotionTheme } from '../EmotionTheme';

import { FormRendererFragment_formInstance$key } from './__generated__/FormRendererFragment_formInstance.graphql';
import { FormRendererFragment_formInstanceQuestion$key } from './__generated__/FormRendererFragment_formInstanceQuestion.graphql';

const formInstanceQuestionFragment = graphql`
  fragment FormRendererFragment_formInstanceQuestion on FormInstanceQuestion {
    __typename
    id
    question {
      label
    }
    ... on FormInstanceDateQuestion {
      date
    }
    ... on FormInstanceRadioSelectorQuestion {
      selection
    }
    ... on FormInstanceTextQuestion {
      text
    }
    ... on FormInstanceYesNoQuestion {
      yes
    }
  }
`;

const formInstanceFragment = graphql`
  fragment FormRendererFragment_formInstance on FormInstance {
    submittedDate
    questions {
      id
      ... FormRendererFragment_formInstanceQuestion
    }
  }
`;

const styles = {
  formInstance: (theme: EmotionTheme): CSSObject => ({
    backgroundColor: theme.colors.grey50,
    border: `${theme.size.$1} solid ${theme.colors.grey300}`,
    borderRadius: theme.size.$4,
    padding: theme.size.$24,
  }),
  formQuestion: (theme: EmotionTheme): CSSObject => ({
    padding: theme.size.$24,
    ':not(:last-child)': {
      borderBottom: `${theme.size.$1} solid ${theme.colors.grey300}`,
    },
  }),
  formQuestionLabel: (theme: EmotionTheme): CSSObject => ({
    color: theme.colors.grey700,
    display: 'inline-block',
    marginBottom: theme.size.$4,
  }),
};

interface Props {
  readonly formInstance: FormRendererFragment_formInstance$key;
}

function FromRendererQuestion(props: {
  readonly question: FormRendererFragment_formInstanceQuestion$key | null;
}): ReactElement {
  const question = useFragment(formInstanceQuestionFragment, props.question);

  let answer;
  switch (question?.__typename) {
  case 'FormInstanceDateQuestion':
    answer = question?.date != null ? new Date(question.date).toDateString() : null;
    break;
  case 'FormInstanceRadioSelectorQuestion':
    answer = question?.selection;
    break;
  case 'FormInstanceTextQuestion':
    answer = question?.text;
    break;
  case 'FormInstanceYesNoQuestion':
    answer = question?.yes != null ? question.yes ? 'Yes' : 'No' : null;
    break;
  }

  return (
    <div css={theme => styles.formQuestion(theme)}>
      <small>
        <label css={theme => styles.formQuestionLabel(theme)}>{question?.question?.label}</label>
      </small>
      <br />
      <div>{answer != null ? answer : '(Not Answered)'}</div>
    </div>
  );
}

function FormRenderer(props: Props): ReactElement {
  const formInstance = useFragment(formInstanceFragment, props.formInstance);
  const questions = formInstance.questions;

  return (
    <div css={theme => styles.formInstance(theme)}>
      {questions.map(question => <FromRendererQuestion key={question?.id} question={question} />)}
    </div>
  );
}

export default FormRenderer;
