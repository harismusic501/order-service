pipeline {
  agent any

  environment {
    HELM_REPO_URL = 'https://github.com/harismusic501/helm-template-repo.git'
    IMAGE_NAME    = 'order-service'
    IMAGE_TAG     = 'v1'
    NAMESPACE     = 'dev'
  }

  stages {

    stage('Checkout App Repo') {
      steps {
        // Jenkins checks out this repo automatically via 'checkout scm'
        checkout scm
      }
    }

    stage('Checkout Helm Repo') {
      steps {
        dir('helm-template-repo') {
          git branch: 'main',
              url: "${HELM_REPO_URL}"
        }
      }
    }

    stage('Build App') {
      steps {
        sh 'mvn clean package -DskipTests'
      }
    }

    stage('Build Docker Image') {
      steps {
        sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
      }
    }

    stage('Helm Lint') {
      steps {
        sh """
          helm lint helm-template-repo/base-microservice-chart \
            -f values-dev.yaml
        """
      }
    }

    stage('Deploy to Kubernetes via Helm') {
      steps {
        sh """
          helm upgrade --install order-service \
            helm-template-repo/base-microservice-chart \
            -f values-dev.yaml \
            -n ${NAMESPACE} --create-namespace
        """
      }
    }

    stage('Verify Deployment') {
      steps {
        sh "kubectl rollout status deployment/order-service -n ${NAMESPACE} --timeout=90s"
      }
    }
  }

  post {
    success {
      echo 'Deployment succeeded. Service available at http://order-service.local/order'
    }
    failure {
      echo 'Deployment failed. Check logs above.'
    }
  }
}
