var { formTemplates, formInstances, user } = require('./mock_data');
var express = require('express');
var { graphqlHTTP } = require('express-graphql');
var { buildSchema } = require('graphql');
var cors = require('cors');
var fs = require('fs');

// Construct a schema, using GraphQL schema language
var schema = fs.readFileSync('./graphql/schema.graphql').toString('utf-8');

var root = {
  node: (input) => {
    const formTemplate = formTemplates.find(formTemplate => formTemplate.id === input.id);
    const formInstanceEdges = formInstances.map(formInstance => ({
      cursor: formInstance.guid,
      // Note this isn't strictly correct since the formTemplate here won't have the instances set on it. In the real
      // application that's okay because you wouldn't write an infinitely recursive query so it can be filled.
      node: { ...formInstance, formTemplate: formTemplate, creator: user },
    }));
    const formInstancesConnection = {
      edges: formInstanceEdges,
      // ignore the pageInfo for now since we won't paginate the mock data
    };

    formTemplate.formInstances = formInstancesConnection;
    formTemplate.__typename = "FormTemplate";

    return formTemplate;
  },
};

var app = express();
app.use(cors());

app.use('/graphql', graphqlHTTP({
  schema: buildSchema(schema),
  rootValue: root,
  graphiql: true,
}));

app.listen(4000);
console.log('Running a GraphQL API server at http://localhost:4000/graphql');
