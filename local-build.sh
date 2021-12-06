./gradlew build installDist
eval $(minikube docker-env)
docker build -t order-service .
kubectl apply -f deploy/plan-dev.yml
