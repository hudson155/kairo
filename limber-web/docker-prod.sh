set -e

docker build -t limber-web:master .

docker run -p 8080:80 \
  limber-web:master
