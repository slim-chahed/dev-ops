pipeline {
    agent any

    triggers {
        pollSCM('H/30 * * * *')
    }

    environment {
        DOCKER_HUB_USER = 'salimchahed'
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
                    withSonarQubeEnv('SonarQube') {
                        sh 'mvn sonar:sonar'
                    }
                }
            }
        }

        stage('SonarQube Analysis Frontend') {
            steps {
                dir('frontend') {
                    withSonarQubeEnv('SonarQube') {
                        sh '''
                            set -e
                            curl -sSLo sonar-scanner.zip https://binaries.sonarsource.com/Distribution/sonar-scanner-cli/sonar-scanner-cli-4.8.0.2856-linux.zip
                            jar xf sonar-scanner.zip
                            SCANNER_DIR=$(find . -maxdepth 1 -type d -name "sonar-scanner-*" | head -n 1)
                            chmod +x "$SCANNER_DIR/bin/sonar-scanner"
                            sed -i 's/\r$//' "$SCANNER_DIR/bin/sonar-scanner"
                            export JAVA_HOME=$(dirname "$(dirname "$(readlink -f "$(which javac)")")")
                            export PATH="$JAVA_HOME/bin:$PATH"
                            java -version
                            "$SCANNER_DIR/bin/sonar-scanner" -Dsonar.projectKey=tn.esprit:frontend
                        '''
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
                    docker rm -f sonarqube || true
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
