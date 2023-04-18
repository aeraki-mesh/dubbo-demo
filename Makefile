# Build parameters
OUT?=./target
DOCKER_TMP?=$(OUT)/docker_temp/
DOCKER_PROVIDER_TAG?=ghcr.io/aeraki-mesh/dubbo-sample-provider:latest
DOCKER_CONSUMER_TAG?=ghcr.io/aeraki-mesh/dubbo-sample-consumer:latest
DOCKER_SECOND_PROVIDER_TAG=ghcr.io/aeraki-mesh/dubbo-sample-second-provider:latest
BINARY_NAME?=$(OUT)/dubbo-samples-basic-*.jar

build:
	mvn package
docker-build: build
	rm -rf $(DOCKER_TMP)
	mkdir $(DOCKER_TMP)
	cp $(BINARY_NAME) $(DOCKER_TMP)
	cp ./docker/Dockerfile.consumer $(DOCKER_TMP)Dockerfile
	cp ./config/dubbo-resolve.properties $(DOCKER_TMP)dubbo-resolve.properties
	docker build -t $(DOCKER_CONSUMER_TAG) $(DOCKER_TMP)
	cp ./docker/Dockerfile.provider $(DOCKER_TMP)Dockerfile
	docker build -t $(DOCKER_PROVIDER_TAG) $(DOCKER_TMP)
	cp ./docker/Dockerfile.second-provider $(DOCKER_TMP)Dockerfile
	docker build -t $(DOCKER_SECOND_PROVIDER_TAG) $(DOCKER_TMP)
	rm -rf $(DOCKER_TMP)
docker-push: docker-build
	docker push $(DOCKER_PROVIDER_TAG)
	docker push $(DOCKER_CONSUMER_TAG)
	docker push $(DOCKER_SECOND_PROVIDER_TAG)
clean:
	rm -rf $(OUT)

.PHONY: build docker-build docker-push clean
