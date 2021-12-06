# mms-code-challenge

## CI Steps
```shell
./gradlew build

./gradlew test

./gradlew installDist

docker build -t order-service .

kubectl apply -f deploy/plan-dev.yml
```

## Development tricks
Inspecting topics
```shell
kubectl exec statefulset/kafka -- bash -c "kafka-console-consumer.sh --bootstrap-server localhost:9092 --include 'order-event|payment-event|shipment-event'"
```
