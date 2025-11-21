def call(String projectName, String imageTag, String dockerhubuser) {
    withCredentials([usernamePassword(credentialsId: 'docker', 
                                     usernameVariable: 'dockerhubuser', 
                                     passwordVariable: 'dockerhubpass')]) {
        sh """
            IMAGE="${dockerhubuser}/${projectName}:${imageTag}"
            echo "Scanning Docker image: \$IMAGE"
            trivy image "\$IMAGE" > trivyimage.txt
        """
    }
}

