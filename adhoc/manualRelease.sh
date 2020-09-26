# Manually releases the current code
#
# adhoc/manualRelease.sh

set -e

./gradlew clean
ENV=production ./gradlew build

docker build -t registry.digitalocean.com/limber/limber-monolith-server:master limber-backend/server/monolith
docker tag registry.digitalocean.com/limber/limber-monolith-server:master "registry.digitalocean.com/limber/limber-monolith-server:$(git rev-parse HEAD)"
docker push registry.digitalocean.com/limber/limber-monolith-server:master
docker push "registry.digitalocean.com/limber/limber-monolith-server:$(git rev-parse HEAD)"

docker build -t registry.digitalocean.com/limber/limber-react:master limber-react
docker tag registry.digitalocean.com/limber/limber-react:master "registry.digitalocean.com/limber/limber-react:$(git rev-parse HEAD)"
docker push registry.digitalocean.com/limber/limber-react:master
docker push "registry.digitalocean.com/limber/limber-react:$(git rev-parse HEAD)"

docker build -t registry.digitalocean.com/limber/limber-web:master limber-web
docker tag registry.digitalocean.com/limber/limber-web:master "registry.digitalocean.com/limber/limber-web:$(git rev-parse HEAD)"
docker push registry.digitalocean.com/limber/limber-web:master
docker push "registry.digitalocean.com/limber/limber-web:$(git rev-parse HEAD)"

kubectl set image deployment/limber-monolith-server limber-monolith-server="registry.digitalocean.com/limber/limber-monolith-server:$(git rev-parse HEAD)" --namespace limber
kubectl set image deployment/limber-web limber-web="registry.digitalocean.com/limber/limber-web:$(git rev-parse HEAD)" --namespace limber
