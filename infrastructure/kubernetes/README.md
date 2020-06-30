# Kubernetes Infrastructure

The Kubernetes YAMLs in this folder comprehensively declare the Limber infrastructure.

## `nginx-ingress-controller`

```
kubectl apply --recursive -f infrastructure/kubernetes/nginx-ingress-controller
```

Note: You may need to temporarily remove the `service.beta.kubernetes.io/do-loadbalancer-hostname` annotation
in order to get an IP address when running `kubectl get svc --namespace=ingress-nginx`
to get DNS records.

## `tls-certificates`

```
kubectl apply --recursive -f infrastructure/kubernetes/tls-certificates
```

## `limber`

Create the namespace
```
kubectl apply -f infrastructure/kubernetes/limber/namespace.yaml --namespace limber
```

Create a secret named `registry-limber` in the `limber` namespace
using https://www.digitalocean.com/docs/kubernetes/how-to/set-up-registry/#obtain-credentials-from-doctl.
Don't configure the default service account to use this secret.

Create a secret named `limber-prod-postgres-credentials` in the `limber` namespace.
Create a secret named `limber-prod-auth` in the `limber` namespace.

```
kubectl apply --recursive -f infrastructure/kubernetes/limber --prune -l instance=limber --namespace limber
```
