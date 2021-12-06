# mms-code-challenge

## Dependencies
1. minikube environment
2. Intellij IDEA

## To run development environment
1. Import the project into Intellij

2. Execute the following
```shell
./gradlew build

kubectl apply -f infra/postgres.yml,infra/kafka-dev.yml
```

3. Use intellij to run Configuration - ApplicaitonKt

## CI Steps
### To deploy into kubernetes environment

1. Build & test the project 
```shell
./gradlew build
./gradlew test
./gradlew installDist
```
2. Publish to registry and apply the plan to kubernetes
```shell
docker build -t order-service .
kubectl apply -f deploy/plan-dev.yml
```

## Development tricks
Inspecting topics
```shell
kubectl exec statefulset/kafka -- bash -c "kafka-console-consumer.sh --bootstrap-server localhost:9092 --include 'order-event|payment-event|shipment-event'"
```
