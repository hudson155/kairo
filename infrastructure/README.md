# Infrastructure

This folder contains both infrastructure-as-code YAML files as well as documentation of Limber's
infrastructure and how to use it.

## Useful commands

### Sign in to the GCloud CLI

This command authenticates you with the CLI so you can interact with GCP.

```
gcloud auth login
```

### Sign in to a service account

This command authenticates your CLI as if you were the given service account. This is great for
debugging issues with service accounts.

```
gcloud auth activate-service-account --key-file <KEY_FILE>
```

### Connect to Kubernetes

This command connects your local Kubernetes to Limber's GKE cluster.

```
gcloud container clusters get-credentials <CLUSTER_NAME> \
    --zone <ZONE> \
    --project <PROJECT_NAME>
```

### Connect to SQL instance

See [here](sql/README.md#iam-users).
