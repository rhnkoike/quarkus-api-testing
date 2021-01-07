#!groovy

node('maven') {
	def mvnCmd = "mvn"

	def appName = "testapp"
	def devPrj = "dev"

	stage('java version') {
		sh "java -version"
		sh "mvn -version"
		// sh "echo $JAVA_HOME"
	}

	stage('Cleanup env Dev') {
		// Delete all objects except for is.
		openshift.withCluster() {
			openshift.withProject(devPrj) {
			    openshift.selector("bc", [ "app.kubernetes.io/name": appName ]).delete()
			    openshift.selector("dc", [ "app.kubernetes.io/name": appName ]).delete()
			    openshift.selector("svc", [ "app.kubernetes.io/name": appName ]).delete()
			    openshift.selector("pod", [ "app.kubernetes.io/name": appName ]).delete()
			    openshift.selector("route", [ "app.kubernetes.io/name"
				: appName ]).delete()
				openshift.selector("secret", [ "app.kubernetes.io/name": appName ]).delete()
			}
		}

	}

	stage('Checkout Source') {
		// 注意：Checkoutするブランチは"master"で固定されています
		checkout scm
	}

	def groupId    = getGroupIdFromPom("pom.xml")
	def artifactId = getArtifactIdFromPom("pom.xml")
	def version    = getVersionFromPom("pom.xml")

	stage('Build') {
		if(fileExists("./settings.xml")) {
			sh "cp ./settings.xml ~/.m2/"
		}
		if(fileExists("./settings-security.xml")) {
			sh "cp ./settings-security.xml ~/.m2/"
		}

		echo "Building version ${version}"
		sh "${mvnCmd} clean compile"
	}

	stage('Tests') {

		// Prepare Test - DB start
		openshift.withCluster() {
			openshift.withProject(devPrj) {
			    openshift.newApp("postgresql-ephemeral",
								"-p DATABASE_SERVICE_NAME=postgresql",
								"-p POSTGRESQL_DATABASE=quarkus_test",
								"-p POSTGRESQL_USER=quarkus_test",
								"-p POSTGRESQL_PASSWORD=quarkus_test",
								"-l app.kubernetes.io/name=${appName}"
				)
			}
		}

		echo "Unit&Integration Tests"
		sh "${mvnCmd} test"
	}

	def newTag = "dev-${version}"


	stage('Deploy to Dev') {
		
		echo "Deploy image ${newTag}"
		sh "${mvnCmd} clean package -Dquarkus.kubernetes.deploy=true -DskipTests"

	}
}

// Convenience Functions to read variables from the pom.xml
def getVersionFromPom(pom) {
	def matcher = readFile(pom) =~ '<version>(.+)</version>'
	matcher ? matcher[0][1] : null
}
def getGroupIdFromPom(pom) {
	def matcher = readFile(pom) =~ '<groupId>(.+)</groupId>'
	matcher ? matcher[0][1] : null
}
def getArtifactIdFromPom(pom) {
	def matcher = readFile(pom) =~ '<artifactId>(.+)</artifactId>'
	matcher ? matcher[0][1] : null
}
