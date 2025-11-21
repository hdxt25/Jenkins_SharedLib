// vars/trivy_scan_docker.groovy
def call(String projectName) {
    // Construct the Docker image from environment variables
    def dockerImage = "${env.DOCKER_USER}/${projectName}:${env.BUILD_NUMBER}"
    echo "Scanning Docker image: ${dockerImage} with Trivy"
    
    // Run Trivy scan
    sh "trivy image ${dockerImage} > trivyimage.txt"
}
