pipeline {
  agent any

  tools {
    maven 'Maven-3.9'
    jdk 'JDK-25'
  }

  parameters {
    choice(name: 'BROWSER', choices: ['chrome','edge'], description: 'Browser')
    booleanParam(name: 'HEADLESS', defaultValue: true, description: 'Headless mode')
    choice(name: 'SUITE', choices: ['smoke','regression'], description: 'Test suite')
  }

  options { timestamps() }

  stages {
    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Build') {
      steps { sh "mvn -B clean test-compile" }
    }

    stage('Run Tests') {
      steps {
        script {
          def groups = (params.SUITE == 'smoke') ? 'smoke' : 'regression'
          sh """
            mvn -B test \
              -Dgroups=${groups} \
              -Dbrowser=${params.BROWSER} \
              -Dheadless=${params.HEADLESS}
          """
        }
      }
      post {
        always {
          junit allowEmptyResults: true, testResults: "target/surefire-reports/*.xml"
          archiveArtifacts artifacts: "target/screenshots/**, target/**/*.png, target/**/*.html", allowEmptyArchive: true
        }
      }
    }
  }

  post {
    always { cleanWs() }
  }
}
