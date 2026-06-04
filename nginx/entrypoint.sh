#!/bin/sh
mkdir -p /etc/nginx/ssl
printf '%s' "$SSL_CERT" > /etc/nginx/ssl/fullchain.pem
printf '%s' "$SSL_KEY"  > /etc/nginx/ssl/privkey.pem
exec nginx -g 'daemon off;'
