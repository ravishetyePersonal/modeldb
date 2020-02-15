package main

import (
	"fmt"
	"log"
	"net/http"
	"os"

	gw "github.com/VertaAI/modeldb/protos/gen/go/protos/public/modeldb"
	"github.com/grpc-ecosystem/grpc-gateway/runtime"
	"golang.org/x/net/context"
	"google.golang.org/grpc"
)

func main() {
	address := os.Getenv("MDB_ADDRESS")
	if address == "" {
		address = "localhost:8085"
	}
	log.Println("MDB_ADDRESS : " + address)
	port := os.Getenv("SERVER_HTTP_PORT")
	if port == "" {
		port = "8080"
	}
	log.Println("SERVER_HTTP_PORT : " + port)
	mux := runtime.NewServeMux()
	opts := []grpc.DialOption{grpc.WithInsecure()}
	endpoints := []func(context.Context, *runtime.ServeMux, string, []grpc.DialOption) (err error){
		gw.RegisterProjectServiceHandlerFromEndpoint,
		gw.RegisterExperimentServiceHandlerFromEndpoint,
		gw.RegisterExperimentRunServiceHandlerFromEndpoint,
		gw.RegisterCommentServiceHandlerFromEndpoint,
		gw.RegisterHydratedServiceHandlerFromEndpoint,
		gw.RegisterDatasetServiceHandlerFromEndpoint,
		gw.RegisterDatasetVersionServiceHandlerFromEndpoint,
		gw.RegisterLineageServiceHandlerFromEndpoint,
	}
	for i, endpoint := range endpoints {
		if err := endpoint(context.Background(), mux, address, opts); err != nil {
			panic(fmt.Sprintf("failed to register endpoint %d", i))
		}
	}
	localServer := &http.Server{
		Handler: mux,
		Addr:    ":" + port,
	}
	log.Println("Starting verta-backend proxy on port : " + port)
	err := localServer.ListenAndServe()
	if err != http.ErrServerClosed {
		panic(err)
	}
}
