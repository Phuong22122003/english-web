pipeline {
    agent any

    environment {
        // Image name for each service
        API_GATEWAY_IMAGE = 'api-gateway:latest'
        USER_SERVICE_IMAGE = 'user-service:latest'
        CONTENT_SERVICE_IMAGE = 'content-service:latest'
        LEARNING_SERVICE_IMAGE = 'learning-service:latest'
        COMPOSE_FILE = 'docker-compose.yml'
    }

    stages {
        stage('Checkout Source Code') {
            steps {
                echo '🔹 Checking out source code...'
                git branch: 'main', url: 'https://github.com/Phuong22122003/english-web.git'
            }
        }

        stage('Clean Old Docker Images') {
            steps {
                script {
                    echo '🧹 Removing old Docker images...'
                    sh '''
                        docker compose down --remove-orphans || true
                        docker rmi -f ${API_GATEWAY_IMAGE} || true
                        docker rmi -f ${USER_SERVICE_IMAGE} || true
                        docker rmi -f ${CONTENT_SERVICE_IMAGE} || true
                        docker rmi -f ${LEARNING_SERVICE_IMAGE} || true
                    '''
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                script {
                    echo '🏗️ Building Docker images for each service...'
                    sh '''
                        docker build -t ${API_GATEWAY_IMAGE} api-gateway
                        docker build -t ${USER_SERVICE_IMAGE} user-service
                        docker build -t ${CONTENT_SERVICE_IMAGE} content-service
                        docker build -t ${LEARNING_SERVICE_IMAGE} learning-service
                    '''
                }
            }
        }

        stage('Run Docker Compose') {
            steps {
                script {
                    echo 'Starting all services using docker-compose...'
                    sh '''
                        docker compose up -d --build
                    '''
                }
            }
        }

        stage('Verify Deployment') {
            steps {
                script {
                    echo 'Checking running containers...'
                    sh 'docker ps'
                }
            }
        }
    }

    post {
        success {
            echo 'Deployment successful! All microservices are up and running.'
        }
        failure {
            echo 'Build or deployment failed. Check Jenkins logs for details.'
        }
    }
}
