#save-image.sh
# arg1: version-number
clear;
echo "tar files are being deleted...";
rm license-management-system-customer-image.tar;
rm licensed-service-image.tar;
echo "tar files have been deleted...";
echo "license-management-system-customer-image:v$1 is being deleted...";
docker rmi license-management-system-customer-image:v$1;
docker rmi licensed-service-image:v$1;
echo "license-management-system-customer-image:v$1 has been deleted...";
echo "Images are being built...";
docker build -t license-management-system-customer-image:v$1 -f ./license-management-system-customer/Dockerfile .;
docker build -t licensed-service-image:v$1 -f ./licensed-service/Dockerfile .;
echo "Images have been built.";
echo "Images are being saved to tar archive...";
docker image save --output="license-management-system-customer-image.tar" license-management-system-customer-image:v$1;
docker image save --output="licensed-service-image.tar" licensed-service-image:v$1;
echo "Images have been saved to tar archives.";
echo "Successfully Done!";
echo "NOTE: Load the tar file to kubernetes cluster manually.";