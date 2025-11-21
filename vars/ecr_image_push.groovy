// vars/dockerECR.groovy
def call(String AWS_ECR_REPO_NAME, String AWS_DEFAULT_REGION, String REPOSITORY_URI) {
    def buildNumber = env.BUILD_NUMBER  // automatically take Jenkins build number
    sh 
      """
      aws ecr get-login-password --region ${AWS_DEFAULT_REGION} | docker login --username AWS --password-stdin ${REPOSITORY_URI}
      docker tag ${AWS_ECR_REPO_NAME} ${REPOSITORY_URI}${AWS_ECR_REPO_NAME}:${buildNumber}
      docker push ${REPOSITORY_URI}${AWS_ECR_REPO_NAME}:${buildNumber}
      """
}
