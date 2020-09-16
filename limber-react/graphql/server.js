var express = require('express');
var { graphqlHTTP } = require('express-graphql');
var { buildSchema } = require('graphql');
var cors = require('cors');

var app = express();
app.use(cors());

app.use('/graphql', graphqlHTTP({
  schema: buildSchema(`
    type Hello {
      world: String!
    }
  `),
  rootValue: {},
  graphiql: true,
}));

app.listen(4000);
console.log('Running a GraphQL API server at http://localhost:4000/graphql');
