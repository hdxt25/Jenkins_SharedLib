def call(String projectName,String ImageTag) {
    sh """
        IMAGE="$DOCKER_USR/${projectName}:${ImageTag}"
        echo "Scanning Docker image: \$IMAGE"
        trivy image "\$IMAGE" > trivyimage.txt
    """
}
