#minikube-delete-image.sh
# arg1: image-version
clear;
echo "Reset Minikube...";
kubectl delete all --all;
echo "Minikube has been reset."
minikube image ls --format table;
echo "The image is being deleted...";
minikube image remove docker.io/library/license-management-system-customer-image:v$1;
echo "The image has been deleted."
minikube image ls --format table;