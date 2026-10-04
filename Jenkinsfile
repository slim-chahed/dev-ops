pipeline {
    agent any

    triggers {
        pollSCM('H/2 * * * *')
    }

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

        stage('SonarQube Analysis Frontend') {
            steps {
                dir('frontend') {
                    script {
                        def javaHome = tool 'jdk'
                        withEnv(["JAVA_HOME=${javaHome}"]) {
                            sh '''
                                curl -sSLo sonar-scanner.zip https://binaries.sonarsource.com/Distribution/sonar-scanner-cli/sonar-scanner-cli-4.8.0.2856-linux.zip
                                jar xf sonar-scanner.zip
                                chmod +x ./sonar-scanner-4.8.0.2856-linux/bin/sonar-scanner
                                ./sonar-scanner-4.8.0.2856-linux/bin/sonar-scanner \
                                    -Dsonar.projectKey=tn.esprit:frontend \
                                    -Dsonar.host.url=${SONAR_HOST_URL} \
                                    -Dsonar.login=${SONAR_LOGIN}
                            '''
                        }
                    }
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
                sh '''
                    docker compose down || true
                    docker compose up -d --build
                '''
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}
