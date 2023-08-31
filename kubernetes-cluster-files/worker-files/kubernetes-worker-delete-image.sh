#kubernetes-worker-delete-image.sh
#arg1: image-version
clear;
echo "Images";
sudo crictl images;
echo "The image is being deleted...";
sudo crictl rmi license-management-system-customer-image:v$1;
echo "The image has been deleted."
echo "Images";
sudo crictl images;
echo "Done!";