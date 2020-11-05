# Build parameters
OUT?=./target
DOCKER_TMP?=$(OUT)/docker_temp/
DOCKER_PROVIDER_TAG?=aeraki/dubbo-sample-provider:latest
DOCKER_CONSUMER_TAG?=aeraki/dubbo-sample-consumer:latest
BINARY_NAME?=$(OUT)/dubbo-samples-basic-1.0-SNAPSHOT.jar

build:
	mvn package
docker-build: build
	rm -rf $(DOCKER_TMP)
	mkdir $(DOCKER_TMP)
	cp $(BINARY_NAME) $(DOCKER_TMP)
	cp ./docker/Dockerfile.provider $(DOCKER_TMP)Dockerfile
	docker build -t $(DOCKER_PROVIDER_TAG) $(DOCKER_TMP)
	cp ./docker/Dockerfile.consumer $(DOCKER_TMP)Dockerfile
	docker build -t $(DOCKER_CONSUMER_TAG) $(DOCKER_TMP)
	rm -rf $(DOCKER_TMP)
docker-push:
	docker push $(DOCKER_PROVIDER_TAG)
	docker push $(DOCKER_CONSUMER_TAG)
clean:
	rm -rf $(OUT)

.PHONY: build docker-build docker-push clean
