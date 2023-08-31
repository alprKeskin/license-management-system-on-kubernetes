# docker-run.sh
# arg1: version-number
clear;
echo "Image is being built...";
docker build -t licensed-service-image:v$1 -f ./Dockerfile .;
echo "Image has been built.";
echo "Container is being created...";
docker run -p 8082:8082 licensed-service-image:v$1;
echo "Container has been created.";
echo "Done";