pipeline {
    agent any

    triggers {
        pollSCM('H/30 * * * *')
    }

    environment {
        DOCKER_HUB_USER = 'salimchahed'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Backend') {
            steps {
                dir('backend') {
                    sh '''
                        set -e
                        echo "=== Building Backend ==="
                        mvn clean compile
                    '''
                }
            }
            post {
                always {
                    junit 'backend/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Test Backend') {
            steps {
                dir('backend') {
                    sh '''
                        set -e
                        echo "=== Running Backend Tests ==="
                        mvn test
                    '''
                }
            }
        }

        stage('Build Frontend') {
            steps {
                script {
                    def nodeHome = tool 'NodeJS'

                    dir('frontend') {
                        withEnv(["PATH+NODE=${nodeHome}/bin"]) {
                            sh '''
                                set -e

                                echo "=== Node Version ==="
                                node --version

                                echo "=== NPM Version ==="
                                npm --version

                                echo "=== Installing Dependencies ==="
                                npm ci

                                echo "=== Building Angular Frontend ==="
                                npm run build
                            '''
                        }
                    }
                }
            }
        }

        stage('SonarQube Backend') {
            steps {
                dir('backend') {
                    withSonarQubeEnv('SonarQube') {
                        sh '''
                            set -e

                            echo "=== SonarQube Backend Analysis ==="

                            mvn sonar:sonar \
                                -Dsonar.projectKey=tn.esprit:backend \
                                -Dsonar.projectName="DevOps Backend"
                        '''
                    }
                }
            }
        }

        stage('SonarQube Frontend') {
            steps {
                script {
                    def scannerHome = tool 'SonarScanner'
                    def nodeHome = tool 'NodeJS'

                    dir('frontend') {
                        withEnv(["PATH+NODE=${nodeHome}/bin"]) {
                            withSonarQubeEnv('SonarQube') {
                                sh """
                                    set -e

                                    echo "=== SonarScanner ==="
                                    ${scannerHome}/bin/sonar-scanner --version

                                    echo "=== Frontend SonarQube Analysis ==="

                                    ${scannerHome}/bin/sonar-scanner \
                                        -Dsonar.projectKey=tn.esprit:frontend \
                                        -Dsonar.projectName="DevOps Frontend" \
                                        -Dsonar.sources=src \
                                        -Dsonar.exclusions="node_modules/**,dist/**"
                                """
                            }
                        }
                    }
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Package Backend') {
            steps {
                dir('backend') {
                    sh '''
                        set -e
                        mvn package -DskipTests
                    '''
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                script {

                    def backendImage =
                        docker.build(
                            "${DOCKER_HUB_USER}/backend:${BUILD_NUMBER}",
                            "backend"
                        )

                    def frontendImage =
                        docker.build(
                            "${DOCKER_HUB_USER}/frontend:${BUILD_NUMBER}",
                            "frontend"
                        )

                    docker.withRegistry(
                        'https://index.docker.io/v1/',
                        'docker-hub-credentials'
                    ) {
                        backendImage.push()
                        frontendImage.push()

                        backendImage.push('latest')
                        frontendImage.push('latest')
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                sh '''
                    set -e

                    echo "=== Deploying Application ==="

                    docker compose down || true
                    docker compose up -d --build

                    echo "=== Running Containers ==="
                    docker compose ps
                '''
            }
        }
    }

    post {
        always {
            cleanWs()
        }

        success {
            echo '========================================'
            echo '        PIPELINE SUCCESSFUL'
            echo '========================================'
        }

        failure {
            echo '========================================'
            echo '        PIPELINE FAILED'
            echo '========================================'
        }
    }
}