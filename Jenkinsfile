pipeline {
  agent any

  tools {
    maven 'Maven-3.9'
    jdk 'JDK-25'
  }

  parameters {
    choice(name: 'ENV', choices: ['dev','test','stage'], description: 'Target environment')
    choice(name: 'BROWSER', choices: ['chrome','edge'], description: 'Browser')
    booleanParam(name: 'HEADLESS', defaultValue: true, description: 'Headless mode')
    // Optional: let user pick, but we will enforce rules based on ENV
    choice(name: 'SUITE', choices: ['auto','smoke','regression'], description: 'auto = enforce by ENV')
  }

  options { timestamps() }

  stages {
    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Decide Suite + Load Credentials') {
      steps {
        script {
          // Enforce suite rules by ENV
          def enforcedSuite = (params.ENV == 'test') ? 'regression' : 'smoke'

          if (params.SUITE == 'auto') {
            env.SUITE_TO_RUN = enforcedSuite
          } else {
            // Validate manual selection
            if (params.ENV == 'test' && params.SUITE != 'regression') {
              error("ENV=test must run regression only. You selected: ${params.SUITE}")
            }
            if ((params.ENV == 'dev' || params.ENV == 'stage') && params.SUITE != 'smoke') {
              error("ENV=${params.ENV} must run smoke only. You selected: ${params.SUITE}")
            }
            env.SUITE_TO_RUN = params.SUITE
          }

          env.GROUPS = env.SUITE_TO_RUN
          echo "ENV=${params.ENV}, SUITE=${env.SUITE_TO_RUN}, GROUPS=${env.GROUPS}"
        }
      }
    }

    stage('Build') {
      steps {
        script {
          if (isUnix()) {
            sh "mvn -B clean test-compile"
          } else {
            bat "mvn -B clean test-compile"
          }
        }
      }
    }

    stage('Run Tests') {
      steps {
        script {
          // Map ENV -> Jenkins credential ID
          def credId = (params.ENV == 'dev') ? 'db-dev' :
                       (params.ENV == 'test') ? 'db-test' :
                       'db-stage'

          withCredentials([usernamePassword(credentialsId: credId,
                                            usernameVariable: 'DB_USER',
                                            passwordVariable: 'DB_PASS')]) {

            def mvnCmd = "mvn -B test -Denv=${params.ENV} -Dgroups=${env.GROUPS} -Dbrowser=${params.BROWSER} -Dheadless=${params.HEADLESS} -Ddb.user=%DB_USER% -Ddb.pass=%DB_PASS%"

            if (isUnix()) {
              // On Linux/Mac, env vars are $DB_USER / $DB_PASS
              sh "mvn -B test -Denv=${params.ENV} -Dgroups=${env.GROUPS} -Dbrowser=${params.BROWSER} -Dheadless=${params.HEADLESS} -Ddb.user=$DB_USER -Ddb.pass=$DB_PASS"
            } else {
              // On Windows, env vars are %DB_USER% / %DB_PASS%
              bat mvnCmd
            }
          }
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
