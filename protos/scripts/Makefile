#!/bin/bash
#export GOPATH=~/go

GATEWAY_FLAGS := -I.. -I/usr/local/include -I$(GOPATH)/src/github.com/grpc-ecosystem/grpc-gateway/third_party/googleapis

PYTHON_GRPC_FLAGS := --grpc_python_out=../gen/python/ --python_out=../gen/python/
GO_GW_OUTPUT := ../gen/go/

code:
	# note code paths are wrt scripts dir
	mkdir -p ../gen/python
	python3 -m grpc_tools.protoc $(PYTHON_GRPC_FLAGS) $(GATEWAY_FLAGS) ../protos/public/modeldb/*.proto
	python3 -m grpc_tools.protoc $(PYTHON_GRPC_FLAGS) $(GATEWAY_FLAGS) ../protos/public/modeldb/metadata/*.proto
	python3 -m grpc_tools.protoc $(PYTHON_GRPC_FLAGS) $(GATEWAY_FLAGS) ../protos/public/modeldb/versioning/*.proto
	python3 -m grpc_tools.protoc $(PYTHON_GRPC_FLAGS) $(GATEWAY_FLAGS) ../protos/public/artifactstore/*.proto
	python -m grpc_tools.protoc $(PYTHON_GRPC_FLAGS) $(GATEWAY_FLAGS) ../protos/public/uac/*.proto
	python -m grpc_tools.protoc $(PYTHON_GRPC_FLAGS) $(GATEWAY_FLAGS) ../protos/public/common/*.proto

	touch ../gen/python/protos/__init__.py

	touch ../gen/python/protos/public/__init__.py
	touch ../gen/python/protos/public/modeldb/__init__.py
	touch ../gen/python/protos/public/modeldb/metadata/__init__.py
	touch ../gen/python/protos/public/modeldb/versioning/__init__.py
	touch ../gen/python/protos/public/artifactstore/__init__.py
	touch ../gen/python/protos/public/uac/__init__.py
	touch ../gen/python/protos/public/common/__init__.py

	./fix_imports.sh ../gen/python/protos/public/modeldb/
	./fix_imports.sh ../gen/python/protos/public/modeldb/metadata/
	./fix_imports.sh ../gen/python/protos/public/modeldb/versioning/
	./fix_imports.sh ../gen/python/protos/public/artifactstore/
	./fix_imports.sh ../gen/python/protos/public/uac/
	./fix_imports.sh ../gen/python/protos/public/common/

gw:
	mkdir -p ../gen/go
	protoc $(GATEWAY_FLAGS) \
		--go_out=plugins=grpc:$(GO_GW_OUTPUT) \
		--grpc-gateway_out=logtostderr=true,allow_delete_body=true:$(GO_GW_OUTPUT) \
		../protos/public/common/*.proto
	protoc $(GATEWAY_FLAGS) \
		--go_out=plugins=grpc:$(GO_GW_OUTPUT) \
		--grpc-gateway_out=logtostderr=true,allow_delete_body=true:$(GO_GW_OUTPUT) \
		../protos/public/modeldb/*.proto
	protoc $(GATEWAY_FLAGS) \
		--go_out=plugins=grpc:$(GO_GW_OUTPUT) \
		--grpc-gateway_out=logtostderr=true,allow_delete_body=true:$(GO_GW_OUTPUT) \
		../protos/public/modeldb/versioning/*.proto
	protoc $(GATEWAY_FLAGS) \
		--go_out=plugins=grpc:$(GO_GW_OUTPUT) \
		--grpc-gateway_out=logtostderr=true,allow_delete_body=true:$(GO_GW_OUTPUT) \
		../protos/public/artifactstore/*.proto
	protoc $(GATEWAY_FLAGS) \
		--go_out=plugins=grpc:$(GO_GW_OUTPUT) \
		--grpc-gateway_out=logtostderr=true,allow_delete_body=true:$(GO_GW_OUTPUT) \
		../protos/public/uac/*.proto
	./fix_go_imports.sh ../gen/go/protos/public/modeldb/
	./fix_go_imports.sh ../gen/go/protos/public/artifactstore/
	./fix_go_imports.sh ../gen/go/protos/public/uac/
deps:
	go get -u github.com/grpc-ecosystem/grpc-gateway/protoc-gen-grpc-gateway
	go get -u github.com/grpc-ecosystem/grpc-gateway/protoc-gen-swagger
	go get -u github.com/golang/protobuf/protoc-gen-go
