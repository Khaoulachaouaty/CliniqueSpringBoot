FROM nginx:alpine

# Copier les fichiers buildés (dans le dossier browser)
COPY dist/clinique_backend/browser /usr/share/nginx/html

# Configuration Nginx pour le routing Angular et proxy API
RUN echo 'server { \
    listen 80; \
    server_name localhost; \
    location / { \
        root /usr/share/nginx/html; \
        index index.html; \
        try_files $uri $uri/ /index.html; \
    } \
    location /clinique/api/ { \
        proxy_pass http://backend:8082/clinique/api/; \
        proxy_set_header Host $host; \
        proxy_set_header X-Real-IP $remote_addr; \
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for; \
    } \
}' > /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]