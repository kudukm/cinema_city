pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "cinema-city:latest"
    }

    stages {
        stage('Checkout') {
            steps {
                git credentialsId: 'git-https-token', url: 'https://github.com/kudukm/cinema_city.git', branch: 'main'
            }
        }

        stage('Build') {
            steps {
                sh './gradlew clean build --info'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t ${DOCKER_IMAGE} .'
            }
        }

        stage('Deploy') {
            steps {
                sh 'docker run -p 127.0.0.1:8081:8081 ${DOCKER_IMAGE}'
            }
        }
    }

    post {
        always {
            sh 'docker rmi ${DOCKER_IMAGE}'
        }
        success {
            echo 'Deployment succeeded!'
        }
        failure {
            echo 'Deployment failed!'
        }
    }
}