all: proxy docker

proxy: go.mod go.sum proxy.go
	CGO_ENABLED=0 GOOS=linux GOARCH=amd64 go build proxy.go

docker: proxy Dockerfile
	docker build -t modeldb-proxy .
