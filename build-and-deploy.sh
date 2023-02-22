#!/bin/bash
# Script to automate build + push
az acr login --name ifesunmola

arg=$1

run_backend(){
      echo "Building backend img..."
      (cd backend || exit; docker build --no-cache  -t ifesunmola.azurecr.io/backend .)
      echo "Backend img built"
      echo "Pushing backend img..."
      docker push ifesunmola.azurecr.io/backend
}

run_frontend(){
      read -rp "Did you change the api link in AppConstants.ts?"
      echo "Building frontend img..."
      (cd frontend || exit; docker build --no-cache -t ifesunmola.azurecr.io/frontend .)
      echo "Frontend img built"
      echo "Pushing frontend img..."
      docker push ifesunmola.azurecr.io/frontend
}

if [ "$arg" = "frontend" ]; then
      run_frontend
      exit 0;
fi

if [ "$arg" = "backend" ]; then
      run_backend
      exit 0;
fi

if [ "$arg" = "" ]; then
      run_backend
      run_frontend
      exit 0;
fi