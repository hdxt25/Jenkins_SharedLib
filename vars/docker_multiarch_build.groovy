def call(String Project, String ImageTag, String dockerhubuser){
  withCredentials([usernamePassword(credentialsId: 'docker', passwordVariable: 'dockerhubpass', usernameVariable: 'dockerhubuser')]) {
      sh "docker login -u ${dockerhubuser} -p ${dockerhubpass}"
  }
  sh "docker buildx create --name multiarch --platform linux/amd64,linux/arm64 --driver docker-container --bootstrap --use"
  # Build and push multi-arch image
  sh "docker buildx build --platform linux/amd64,linux/arm64 -t ${dockerhubuser}/${Project}:${ImageTag} --push ."
  sh "docker logout"
}
