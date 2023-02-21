#stage 1
FROM node:18.10-alpine as node
WORKDIR /app
COPY . .
RUN npm install

RUN npm run build
#stage 2
FROM nginx:alpine
COPY ./nginx-custom.conf /etc/nginx/conf.d/default.conf
COPY --from=node /app/dist/frontend /usr/share/nginx/html

EXPOSE 80