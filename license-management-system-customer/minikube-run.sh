#run.sh
# arg1: image_version
clear;
docker build -t license-management-system-customer-image:v$1 -f ./Dockerfile .;
echo "Image has been built.";
minikube image ls --format table;
echo "Image is being loaded into minikube...";
minikube image load license-management-system-customer-image:v$1;
echo "Image has been loaded to the minikube.";
minikube image ls --format table;
echo "Creating deployment";
kubectl apply -f deployment.yaml;
echo "Creating service";
kubectl apply -f service.yaml;
echo "Applying role services";
kubectl apply -f node-list-role.yaml;
kubectl apply -f node-list-role-binding.yaml;
kubectl apply -f pod-get-role.yaml;
kubectl apply -f pod-get-role-binding.yaml;
kubectl apply -f secret-role.yaml;
kubectl apply -f secret-role-binding.yaml;
echo "Checking Permissions";
kubectl auth can-i get nodes --as=system:serviceaccount:default:default;
kubectl auth can-i list nodes --as=system:serviceaccount:default:default;
kubectl auth can-i get pods --as=system:serviceaccount:default:default;
kubectl auth can-i list pods --as=system:serviceaccount:default:default;
kubectl auth can-i get secrets --as=system:serviceaccount:default:default;
kubectl auth can-i list secrets --as=system:serviceaccount:default:default;
echo "Done";
