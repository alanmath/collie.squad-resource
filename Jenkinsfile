pipeline {
    agent any
    environment {
        K8S_PORT = 64606
    }
    stages {
        stage ('Jenkins squad') {
            steps {
                echo 'Squad resource'
            }
        }
        stage ('Build interface'){
            steps {
                build job: 'squad', wait: true
            }
        }
        stage ('Build') {
            steps {
                sh 'mvn clean package'
            }
        }
        stage('Build Image') {
            steps {
                script {
                    account = docker.build("alanmath/squad:${env.BUILD_ID}", "-f Dockerfile .")
                }
            }
        }
        stage('Push Image'){
            steps{
                script {
                    docker.withRegistry('https://registry.hub.docker.com', '594871ef-08ca-47da-8365-6dd98ca976e6') {
                        account.push("${env.BUILD_ID}")
                        account.push("latest")
                    }
                }
            }
        }
        stage('Deploy on k8s') {
            steps {
                withCredentials([string(credentialsId: 'my_kubernetes', variable: 'api_token')]) {
                    sh "kubectl --token $api_token --server https://host.docker.internal:${env.K8S_PORT} --insecure-skip-tls-verify=true apply -f ./k8s/deployment.yaml --validate=false"
                    sh "kubectl --token $api_token --server https://host.docker.internal:${env.K8S_PORT} --insecure-skip-tls-verify=true apply -f ./k8s/service.yaml --validate=false"
                }
            }
        }
    }
}
