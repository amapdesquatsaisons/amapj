 node {
  stage 'Checkout'
  env.PATH = "${tool 'Maven 3'}/bin:${env.PATH}"
  checkout scm
  
  stage 'Build and package'
  sh 'mvn clean package'
 }
 