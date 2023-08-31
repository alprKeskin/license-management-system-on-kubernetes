# kubernetes-master-run-pod.sh
# no args
clear;
echo "Creating deployment...";
kubectl apply -f deployment.yaml;
echo "Deployment has been created.";
echo "Creating service...";
kubectl apply -f service.yaml;
echo "Service has been created.";
echo "Creating node-port service...";
kubectl apply -f node-port-service.yaml;
echo "Node-port service has been created.";
echo "Applying role configurations...";
kubectl apply -f node-list-role.yaml;
kubectl apply -f node-list-role-binding.yaml;
kubectl apply -f pod-get-role.yaml;
kubectl apply -f pod-get-role-binding.yaml;
echo "Role configurations have been applied.";
echo "Checking Permissions...";
kubectl auth can-i get nodes --as=system:serviceaccount:default:default;
kubectl auth can-i list nodes --as=system:serviceaccount:default:default;
kubectl auth can-i get pods --as=system:serviceaccount:default:default;
kubectl auth can-i list pods --as=system:serviceaccount:default:default;
echo "Done!";