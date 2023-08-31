# docker-image.sh
# arg1: version-number
clear;
echo "Image is being built...";
docker build -t licensed-service-image:v$1 -f ./Dockerfile .;
echo "Image has been built.";