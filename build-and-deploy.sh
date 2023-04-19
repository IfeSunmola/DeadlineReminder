#!/bin/bash
# Script to automate build + push
az acr login --name ifesunmola

arg=$1

run_backend(){
      read -rp "BACKEND - Did you change application.yml to use prod profile?"
      echo "Building backend img..."
      (cd backend || exit; docker build --no-cache  -t ifesunmola.azurecr.io/backend .)
      echo "Backend img built"
      echo "Pushing backend img..."
      docker push ifesunmola.azurecr.io/backend
      echo "Pushed to docker... restarting app service"
      az webapp restart --name api-deadline-reminder --resource-group rg-deadline-reminder
      echo "Done"
}

run_frontend(){
      message="
      FRONTEND - Did you change the api link in AppConstants.ts?
      Did you update the annoying status message?"
      read -rp "$message"
      echo "Building frontend img..."
      (cd frontend || exit; docker build --no-cache -t ifesunmola.azurecr.io/frontend .)
      echo "Frontend img built"
      echo "Pushing frontend img..."
      docker push ifesunmola.azurecr.io/frontend
      echo "Pushed to docker... restarting app service"
      az webapp restart --name deadline-reminder --resource-group rg-deadline-reminder
      echo "Done"
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
