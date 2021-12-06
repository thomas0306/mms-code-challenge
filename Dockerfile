FROM openjdk:11-bullseye
EXPOSE 8080:8080

COPY build/install/order-service/ /app/
COPY build/resources/main/ /app/resources/

WORKDIR /app

CMD ["./bin/order-service", "-config=resources/application-k8s.conf"]
