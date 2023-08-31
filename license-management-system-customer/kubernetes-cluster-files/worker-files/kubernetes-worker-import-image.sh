# kubernetes-worker-import-image.sh
# no-args
clear;
echo "Images";
sudo crictl images;
echo "Image is being imported...";
sudo ctr -n=k8s.io images import license-management-system-customer-image.tar;
echo "Image has been imported.";
echo "Images";
sudo crictl images;
echo "Done!";