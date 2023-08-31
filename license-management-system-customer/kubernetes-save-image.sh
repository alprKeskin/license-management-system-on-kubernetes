#save-image.sh
# arg1: version-number
clear;
echo "license-management-system-customer-image.tar is being deleted...";
rm license-management-system-customer-image.tar;
echo "license-management-system-customer-image.tar has been deleted...";
echo "license-management-system-customer-image:v$1 is being deleted...";
docker rmi license-management-system-customer-image:v$1;
echo "license-management-system-customer-image:v$1 has been deleted...";
echo "Image is being built...";
docker build -t license-management-system-customer-image:v$1 -f ./Dockerfile .;
echo "Image has been built.";
echo "Image is being saved to tar archive...";
docker image save --output="license-management-system-customer-image.tar" license-management-system-customer-image:v$1;
echo "Image has been saved to tar archive.";
echo "Successfully Done!";
echo "NOTE: Load the tar file to kubernetes cluster manually.";