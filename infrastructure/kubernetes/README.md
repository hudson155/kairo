# Kubernetes Infrastructure

The Kubernetes YAMLs in this folder comprehensively declare the Limber infrastructure.

## `nginx-ingress-controller`

```
kubectl apply --recursive -f infrastructure/kubernetes/nginx-ingress-controller
```

## `tls-certificates`

```
kubectl apply --recursive -f infrastructure/kubernetes/tls-certificates
```

## `limber`

```
kubectl apply --recursive -f infrastructure/kubernetes/limber --prune -l instance=limber-prod --namespace limber-prod
```
