FROM nginx:stable-alpine

CMD [ "nginx", "-g", "daemon off;" ]
EXPOSE 80

COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY build /var/www
