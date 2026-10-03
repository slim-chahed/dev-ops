pipeline {
    agent any

    environment {
        DOCKER_HUB_USER = 'salimchahed'
        SONAR_HOST_URL = 'http://192.168.33.10:9000'
        SONAR_LOGIN = credentials('sonar-token')
        DOCKER_HUB_CREDS = credentials('docker-hub-credentials')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Compile') {
            steps {
                dir('backend') {
                    sh 'mvn compile'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                dir('backend') {
                    sh '''
                        mvn sonar:sonar \
                            -Dsonar.host.url=${SONAR_HOST_URL} \
                            -Dsonar.login=${SONAR_LOGIN}
                    '''
                }
            }
        }

        stage('Test') {
            steps {
                dir('backend') {
                    sh 'mvn test'
                }
            }
        }

        stage('Package') {
            steps {
                dir('backend') {
                    sh 'mvn package -DskipTests'
                }
            }
        }

        stage('Build & Push Images') {
            steps {
                script {
                    def backendImage = docker.build("${DOCKER_HUB_USER}/backend:${BUILD_NUMBER}", "backend")
                    def frontendImage = docker.build("${DOCKER_HUB_USER}/frontend:${BUILD_NUMBER}", "frontend")

                    docker.withRegistry('https://index.docker.io/v1/', 'docker-hub-credentials') {
                        backendImage.push()
                        frontendImage.push()
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                sh 'docker compose up -d --build'
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}
