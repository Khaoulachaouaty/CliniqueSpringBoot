# Guide de déploiement sur Google Cloud (GKE)

## Architecture

```
GitHub Actions (CI/CD)
    │
    ├── Build & Test (toutes branches)
    ├── Push Docker → GHCR (branche prod)
    └── Deploy → GKE (branche prod)
                    │
                    └── Cluster GKE
                            ├── Namespace: clinique
                            ├── MySQL (1 replica + PVC 5Gi)
                            ├── Backend Spring Boot (2 replicas)
                            └── Frontend Angular/Nginx (2 replicas + LoadBalancer)
```

---

## Étape 1 — Prérequis

### Installer Google Cloud CLI
https://cloud.google.com/sdk/docs/install

### Se connecter et configurer le projet
```bash
gcloud auth login
gcloud config set project TON_PROJECT_ID
```

---

## Étape 2 — Créer le cluster GKE

```bash
# Activer les APIs nécessaires
gcloud services enable container.googleapis.com
gcloud services enable containerregistry.googleapis.com

# Créer le cluster (environ 5-10 minutes)
gcloud container clusters create clinique-cluster \
  --zone europe-west1-b \
  --num-nodes 2 \
  --machine-type e2-standard-2 \
  --enable-autoscaling \
  --min-nodes 1 \
  --max-nodes 3 \
  --disk-size 30GB

# Configurer kubectl
gcloud container clusters get-credentials clinique-cluster \
  --zone europe-west1-b
```

---

## Étape 3 — Créer le Service Account GCP pour GitHub Actions

```bash
# Créer le service account
gcloud iam service-accounts create github-actions-sa \
  --display-name="GitHub Actions SA"

# Donner les permissions nécessaires
gcloud projects add-iam-policy-binding TON_PROJECT_ID \
  --member="serviceAccount:github-actions-sa@TON_PROJECT_ID.iam.gserviceaccount.com" \
  --role="roles/container.developer"

gcloud projects add-iam-policy-binding TON_PROJECT_ID \
  --member="serviceAccount:github-actions-sa@TON_PROJECT_ID.iam.gserviceaccount.com" \
  --role="roles/container.clusterViewer"

# Générer la clé JSON
gcloud iam service-accounts keys create gcp-sa-key.json \
  --iam-account=github-actions-sa@TON_PROJECT_ID.iam.gserviceaccount.com

# Afficher la clé (à copier dans GitHub Secrets)
cat gcp-sa-key.json
```

> ⚠️ Supprime le fichier `gcp-sa-key.json` après avoir copié son contenu dans GitHub Secrets.
> ```bash
> del gcp-sa-key.json
> ```

---

## Étape 4 — Configurer les secrets GitHub

Dans ton repo GitHub → Settings → Secrets and variables → Actions :

| Nom du secret   | Valeur                                      |
|-----------------|---------------------------------------------|
| `GCP_PROJECT_ID`| Ton project ID GCP (ex: `mon-projet-12345`) |
| `GCP_SA_KEY`    | Contenu JSON complet de `gcp-sa-key.json`   |

---

## Étape 5 — Premier déploiement manuel (optionnel)

Si tu veux déployer sans attendre le pipeline :

```bash
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/mysql-secret.yaml
kubectl apply -f k8s/mysql-deployment.yaml
kubectl apply -f k8s/backend-deployment.yaml
kubectl apply -f k8s/frontend-deployment.yaml
```

### Vérifier le déploiement
```bash
# Voir tous les pods
kubectl get pods -n clinique

# Voir les services (et l'IP publique du frontend)
kubectl get svc -n clinique

# Logs du backend
kubectl logs -l app=backend -n clinique --tail=50

# Logs du frontend
kubectl logs -l app=frontend -n clinique --tail=50
```

---

## Étape 6 — Déclencher le pipeline automatique

```bash
git checkout prod
git push origin prod
```

Le pipeline va :
1. Compiler et tester le backend et le frontend
2. Construire et pousser les images Docker sur GHCR avec le tag `latest` + SHA du commit
3. Déployer sur GKE avec rollout progressif
4. Afficher l'IP publique du frontend dans les logs

---

## Accéder à l'application

```bash
# Récupérer l'IP publique
kubectl get svc frontend -n clinique

# L'application sera accessible à :
# http://EXTERNAL_IP
```

---

## Commandes utiles

```bash
# Voir l'état du cluster
kubectl get all -n clinique

# Redémarrer un déploiement
kubectl rollout restart deployment/backend -n clinique
kubectl rollout restart deployment/frontend -n clinique

# Scaler manuellement
kubectl scale deployment/backend --replicas=3 -n clinique

# Voir les events en cas de problème
kubectl get events -n clinique --sort-by='.lastTimestamp'
```

---

## Coût estimé (europe-west1-b)

| Ressource              | Coût mensuel estimé |
|------------------------|---------------------|
| 2x e2-standard-2       | ~$100               |
| PVC 5Gi (SSD)          | ~$1                 |
| LoadBalancer           | ~$18                |
| **Total estimé**       | **~$120/mois**      |

> Pour réduire les coûts en dev/test, utilise `e2-small` ou `e2-micro` avec 1 seul nœud.
