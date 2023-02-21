# Script to automate build + push
az acr login --name ifesunmola

# Starting with backend
echo "Building backend img..."
(cd backend || exit; docker build --no-cache  -t ifesunmola.azurecr.io/backend .)
echo "Backend img built"
echo "Pushing backend img..."
docker push ifesunmola.azurecr.io/backend

# Next frontend
echo "Building frontend img..."
(cd frontend || exit; docker build --no-cache -t ifesunmola.azurecr.io/frontend .)
echo "Frontend img built"
echo "Pushing frontend img..."
docker push ifesunmola.azurecr.io/frontend