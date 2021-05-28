# Kubernetes Infrastructure

Kubernetes infrastructure is managed using infrastructure-as-code YAML files. The Kubernetes YAML
files in this folder declare the Kubernetes infrastructure almost comprehensively. The only
exception is secrets, which are managed manually.

The Kubernetes cluster and node pools should
use [Workload Identity](https://cloud.google.com/kubernetes-engine/docs/how-to/workload-identity)
and the Limber service account (`limber@limberio.iam.gserviceaccount.com`).

To apply the latest version of the infrastructure, use this command. Use care because it will delete
any resources that exist in the cluster but are not included in the YAML files.

```
kubectl apply --recursive -f infrastructure/kubernetes/limber --namespace limber \
    --prune -l instance=limber
```

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

## Postgres connection

Setting up a secure connection to the Postgres instance means using
GCP's [Cloud SQL Proxy](https://cloud.google.com/sql/docs/postgres/connect-kubernetes-engine#proxy)
and the Limber service account (`limber@limberio.iam.gserviceaccount.com`).
