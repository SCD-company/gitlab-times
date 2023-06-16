FROM node:18-alpine as builder
WORKDIR /app
COPY . /app
RUN npm clean-install
RUN npm run build:docker

FROM nginx:1.19.7-alpine
COPY --from=builder /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
HEALTHCHECK CMD wget -O /dev/null http://localhost/ || exit 1