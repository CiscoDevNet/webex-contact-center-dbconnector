#stage 1
FROM node:latest as node
WORKDIR /app
COPY . .
RUN npm install
RUN npm run build
#stage 2
FROM nginx:alpine
COPY --from=node /app/dist /usr/share/nginx/html
COPY nginx.conf  /etc/nginx/conf.d/default.conf

# docker build -t cisco/dbconnector-angular .
# docker run -p 4200:80 cisco/dbconnector-angular

# docker build -t cisco/dbconnector-angular .;docker run -p 4200:80 cisco/dbconnector-angular