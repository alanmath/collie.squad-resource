pipeline {
    agent any
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
    }
}
