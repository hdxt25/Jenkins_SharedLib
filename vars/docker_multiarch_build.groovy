def call(String Project, String ImageTag, String dockerhubuser) {
    // Docker login using Jenkins credentials
    withCredentials([usernamePassword(
        credentialsId: 'docker', 
        passwordVariable: 'dockerhubpass', 
        usernameVariable: 'dockerhubuser'
    )]) {
        sh "docker login -u ${dockerhubuser} -p ${dockerhubpass}"
    }

    // Create or reuse Buildx builder
    sh """
        # If the builder "multiarch" doesn't exist, create it
        docker buildx inspect multiarch >/dev/null 2>&1 || \
            docker buildx create --name multiarch --platform linux/amd64,linux/arm64 --driver docker-container --bootstrap

        # Use the builder
        docker buildx use multiarch
        docker buildx inspect --bootstrap
    """

    // Build and push multi-arch image
    sh "docker buildx build --platform linux/amd64,linux/arm64 -t ${dockerhubuser}/${Project}:${ImageTag} --push ."

    // Logout from Docker
    sh "docker logout"
}

