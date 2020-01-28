mvn clean
mvn package -Dmaven.test.skip=true
docker build --no-cache -t modeldb-backend:latest -f dockerfile --rm .
