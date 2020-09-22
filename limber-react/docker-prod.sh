set -e

docker build -t limber-react:master .

docker run -p 8080:80 \
  limber-react:master
