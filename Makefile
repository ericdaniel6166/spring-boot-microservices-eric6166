.PHONY: remove_images docker-down docker-compose-down docker-up maven-install maven-package maven-clean local-up local-down

IMAGE_PATTERNS = user-service order-service payment-service inventory-service product-service notification-service api-gateway discovery-server

up: maven-install docker-up

down: docker-down

docker-up:
	docker compose -f ./compose.docker.yml up -d

docker-down: docker-compose-down remove_images

docker-compose-down:
	docker compose -f ./compose.docker.yml down -v

remove_images:
	@for pattern in ${IMAGE_PATTERNS}; do \
		echo "Checking for images matching pattern: $$pattern"; \
		if docker images | grep -E "$$pattern"; then \
		  echo "Removing images matching pattern: $$pattern"; \
		  docker rmi $$(docker images | grep "$$pattern" | awk '{print $$3}'); \
		fi \
	done

local-up:
	docker compose -f ./compose.local.yml up -d

local-down:
	docker compose -f ./compose.local.yml down -v

maven-install:
	echo "Maven install"
	./mvnw clean install -Dmaven.test.skip
	cd ../inventory-service-eric6166; \
	./mvnw clean install -Dmaven.test.skip
	#cd ../api-gateway; \
#	./mvnw clean install -Dmaven.test.skip
#	cd ../discovery-server; \
#	./mvnw clean install -Dmaven.test.skip
#	cd ../notification-service; \
#	./mvnw clean install -Dmaven.test.skip
#	cd ../order-service; \
#	./mvnw clean install -Dmaven.test.skip
#	cd ../product-service; \
#	./mvnw clean install -Dmaven.test.skip
#	cd ../payment-service; \
#	./mvnw clean install -Dmaven.test.skip
#	cd ../user-service; \
#	./mvnw clean install -Dmaven.test.skip