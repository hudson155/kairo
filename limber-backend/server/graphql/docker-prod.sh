set -e

docker build -t limber-graphql-server:master .

docker run -p 55200:55200 \
  -e LIMBER_CONFIG=prod \
  -e LIMBER_PROD_JWT_SECRET="$LIMBER_PROD_JWT_SECRET" \
  limber-graphql-server:master
