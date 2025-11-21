def call(String projectName) {
    sh """
        IMAGE="$DOCKER_USR/${projectName}:$BUILD_NUMBER"
        echo "Scanning Docker image: \$IMAGE"
        trivy image "\$IMAGE" > trivyimage.txt
    """
}
