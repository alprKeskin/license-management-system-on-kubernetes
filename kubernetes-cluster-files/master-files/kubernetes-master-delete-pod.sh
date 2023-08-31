# kubernetes-master-delete-pod.sh
# no args
clear;
echo "Deleting deployment and pod...";
kubectl delete deployment license-management-system-customer-deployment;
echo "Deployment and pod have been deleted";
echo "Done";