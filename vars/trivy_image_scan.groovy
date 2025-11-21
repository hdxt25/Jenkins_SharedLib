def call(String projectName, String imageTag, String dockerhubuser) {
    withCredentials([usernamePassword(credentialsId: 'docker', 
                                     usernameVariable: 'dockerhubuser', 
                                     passwordVariable: 'dockerhubpass')]) {
        withEnv(["PROJECT_NAME=${projectName}", "IMAGE_TAG=${imageTag}", "DOCKER_USER=${dockerhubuser}"]) {
            sh '''
                IMAGE="${DOCKER_USER}/${PROJECT_NAME}:${IMAGE_TAG}"
                echo "Scanning Docker image: $IMAGE"
                trivy image "$IMAGE" > trivyimage.txt
            '''
        }
    }
}

