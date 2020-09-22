import { Environment, Network, RecordSource, RequestParameters, Store, Variables } from 'relay-runtime';
import { env } from './env';
import axios from 'axios';

const axiosInstance = axios.create({
  baseURL: env.LIMBER_GRAPHQL_BASE_URL,
});

function fetchQuery(
  operation: RequestParameters,
  variables: Variables,
) {
  return axiosInstance.post('/graphql', {
    query: operation.text,
    variables,
  }).then(response => response.data);
}

const environment = new Environment({
  network: Network.create(fetchQuery),
  store: new Store(new RecordSource()),
});

export { environment };
