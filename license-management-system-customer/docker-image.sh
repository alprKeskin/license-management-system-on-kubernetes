# docker-image.sh
# arg1: version-number
clear;
docker build -t license-management-system-customer-image:v$1 -f ./Dockerfile .;
echo "Image has been built.";