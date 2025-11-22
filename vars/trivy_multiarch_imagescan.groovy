def call(String Project, String ImageTag, String dockerhubuser) {
    withCredentials([usernamePassword(
        credentialsId: 'docker', 
        usernameVariable: 'DOCKER_USER', 
        passwordVariable: 'DOCKER_PASS'
    )]) {

        def IMAGE = "${dockerhubuser}/${Project}:${ImageTag}"

        sh """
            echo "\$DOCKER_PASS" | docker login -u "\$DOCKER_USER" --password-stdin

            echo "Detecting CPU architecture..."
            ARCH=\$(uname -m)
            echo "Current architecture: \$ARCH"

            # Pull both architectures for scanning
            docker pull --platform linux/amd64 ${IMAGE}
            docker pull --platform linux/arm64 ${IMAGE}

            # Scan amd64
            trivy image --platform linux/amd64 ${IMAGE} > trivy_amd64.txt || true
            # Scan arm64
            trivy image --platform linux/arm64 ${IMAGE} > trivy_arm64.txt || true

            # Keep only the image matching current CPU architecture
            if [ "\$ARCH" = "x86_64" ]; then
                echo "Keeping amd64 image for next stage"
                docker image rm --platform linux/arm64 ${IMAGE} || true
            elif [ "\$ARCH" = "aarch64" ]; then
                echo "Keeping arm64 image for next stage"
                docker image rm --platform linux/amd64 ${IMAGE} || true
            else
                echo "Unknown architecture \$ARCH, keeping both images"
            fi

            # Merge Trivy scan results into a single file
            cat trivy_amd64.txt trivy_arm64.txt | sort | uniq > trivy_image_report.txt
        """

        sh "docker logout"
    }
}
