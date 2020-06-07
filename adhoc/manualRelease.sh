# Manually releases the current code
#
# adhoc/manualRelease.sh

set -e

./gradlew clean
ENV=production ./gradlew build
docker build -t registry.digitalocean.com/limber-registry/limber-backend:master limber-backend-application
docker tag registry.digitalocean.com/limber-registry/limber-backend:master registry.digitalocean.com/limber-registry/limber-backend:$(git rev-parse HEAD)
docker push registry.digitalocean.com/limber-registry/limber-backend:master
docker push registry.digitalocean.com/limber-registry/limber-backend:$(git rev-parse HEAD)
docker build -t registry.digitalocean.com/limber-registry/limber-web:master limber-web
docker tag registry.digitalocean.com/limber-registry/limber-web:master registry.digitalocean.com/limber-registry/limber-web:$(git rev-parse HEAD)
docker push registry.digitalocean.com/limber-registry/limber-web:master
docker push registry.digitalocean.com/limber-registry/limber-web:$(git rev-parse HEAD)
kubectl set image deployment/limber-backend limber-backend=registry.digitalocean.com/limber-registry/limber-backend:$(git rev-parse HEAD) --namespace limber
kubectl set image deployment/limber-web limber-web=registry.digitalocean.com/limber-registry/limber-web:$(git rev-parse HEAD) --namespace limber
