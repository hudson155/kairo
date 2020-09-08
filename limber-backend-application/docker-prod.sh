set -e

docker build -t limber-backend:master .

docker run -p 8080:80 \
  -e LIMBER_CONFIG=prod \
  -e LIMBER_PROD_POSTGRES_JDBC_URL="$LIMBER_PROD_POSTGRES_JDBC_URL" \
  -e LIMBER_PROD_POSTGRES_PASSWORD="$LIMBER_PROD_POSTGRES_PASSWORD" \
  -e LIMBER_PROD_JWT_SECRET="$LIMBER_PROD_JWT_SECRET" \
  limber-backend:master
