# Kubernetes Infrastructure

As you might expect, the Kubernetes infrastructure is managed using infrastructure-as-code YAML
files. The Kubernetes YAML files in this folder declare the Kubernetes infrastructure almost
comprehensively, but not quite. There are some things such as secrets and roles that were created
manually.

The Kubernetes cluster and node pools should all use the Limber service account
(`limber@limberio.iam.gserviceaccount.com`).

To apply the latest version of the infrastructure, use this command.

```
kubectl apply -f infrastructure/kubernetes/limber/namespace.yaml --namespace limber
```

## Workload Identity

In order to authorize the cluster to connect to the Postgres instance, the cluster has to
use [Workload Identity](https://cloud.google.com/kubernetes-engine/docs/how-to/workload-identity).
To set this up for the cluster, we ran the following command.

```
gcloud container clusters update <CLUSTER_NAME> \
    --zone <ZONE> \
    --workload-pool=<PROJECT_NAME>.svc.id.goog
```

The existing node pool also needed to be converted to use Workload Identity (new clusters will use
it by default).

```
gcloud container node-pools update <NODE_POOL_NAME> \
    --zone <ZONE> \
    --cluster=<CLUSTER_NAME> \                                                   
    --workload-metadata=GKE_METADATA
```

## Postgres connection

Setting up a secure connection to the Postgres instance meant using
GCP's [Cloud SQL Proxy](https://cloud.google.com/sql/docs/postgres/connect-kubernetes-engine#proxy)
and the Limber GCP service account.

## Secrets

### Postgres credentials

```
kubectl create secret generic postgres-credentials \
    --namespace limber \
    --from-literal=username=<USERNAME> \
    --from-literal=password=<PASSWORD>
```

### JWT auth

```
kubectl create secret generic jwt-auth \
    --namespace limber \
    --from-literal=secret=<SECRET>
```
