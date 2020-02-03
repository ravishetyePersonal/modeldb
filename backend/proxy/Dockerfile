FROM golang:alpine as builder
RUN apk add --no-cache git mercurial bash

WORKDIR /tmp/build

ADD protos/gen/go ./protos/gen/go
ADD go.mod .
ADD go.sum .
ADD backend/proxy ./backend/proxy
RUN go build ./backend/proxy

FROM scratch
COPY --from=builder /tmp/build/backend/proxy/proxy /app/bin/proxy
EXPOSE 8080
ENTRYPOINT ["/app/bin/proxy"]
