def call(String SonarQubeAPI, String ProjectKey){
  withSonarQubeEnv("${SonarQubeAPI}"){
      sh "mvn clean verify sonar:sonar -Dsonar.projectKey=${ProjectKey} -Dsonar.host.url=$SONAR_HOST_URL -Dsonar.login=$SONAR_AUTH_TOKEN"
  }
}
