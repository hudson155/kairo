const { formTemplates, formInstances, user } = require('./mock_data');
const express = require('express');
const { graphqlHTTP } = require('express-graphql');
const { buildSchema } = require('graphql');
const cors = require('cors');
const fs = require('fs');

// Construct a schema, using GraphQL schema language
const schema = fs.readFileSync('./graphql/schema.graphql').toString('utf-8');

const root = {
  node: (input) => {
    const formInstance = formInstances.find(instance => instance.id === input.id);
    const formTemplate = formTemplates[0];
    const mappedQuestions = formInstance.questions.map(instanceQuestion => ({
        ...instanceQuestion,
        question: formTemplate.questions.find(templateQuestion => templateQuestion.id === instanceQuestion.questionId),
      }),
    );

    formInstance.formTemplate = formTemplate;
    formInstance.questions = mappedQuestions;

    return formInstance;
  },
};

const app = express();
app.use(cors());

app.use('/graphql', graphqlHTTP({
  schema: buildSchema(schema),
  rootValue: root,
  graphiql: true,
}));

app.listen(4000);
console.log('Running a GraphQL API server at http://localhost:4000/graphql');
