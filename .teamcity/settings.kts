import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.dockerRegistryConnections
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.ScriptBuildStep
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.triggers.vcs
import jetbrains.buildServer.configs.kotlin.vcs.GitVcsRoot

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2025.11"

project {

    vcsRoot(TestArea_LumeMarkets_CoreBuild_HttpsGithubComDraganJotanovicCoreBackendRefsHeadsMain1)

    buildType(TestArea_LumeMarkets_CoreBuild_BuildOld)
    buildType(TestArea_LumeMarkets_CoreBuild_ReleaseOld)
}

object TestArea_LumeMarkets_CoreBuild_BuildOld : BuildType({
    id("BuildOld")
    name = "Build-old"

    vcs {
        root(TestArea_LumeMarkets_CoreBuild_HttpsGithubComDraganJotanovicCoreBackendRefsHeadsMain1)
    }

    steps {
        script {
            name = "Build"
            id = "Build"
            scriptContent = """echo "Building""""
        }
        script {
            name = "Test"
            id = "Test"
            scriptContent = """echo "Testing""""
        }
    }

    triggers {
        vcs {
        }
    }

    features {
        perfmon {
        }
    }
})

object TestArea_LumeMarkets_CoreBuild_ReleaseOld : BuildType({
    id("ReleaseOld")
    name = "Release-Old"

    vcs {
        root(TestArea_LumeMarkets_CoreBuild_HttpsGithubComDraganJotanovicCoreBackendRefsHeadsMain1)

        branchFilter = """
            +:<default>
            +:release*
        """.trimIndent()
    }

    steps {
        script {
            id = "simpleRunner"
            scriptContent = "release-it-containerized patch --ci"
            dockerImage = "repo.prd.lucera.com/release-it-docker:0.7.0"
            dockerImagePlatform = ScriptBuildStep.ImagePlatform.Linux
        }
    }

    features {
        perfmon {
        }
        dockerRegistryConnections {
            loginToRegistry = on {
                dockerRegistryId = "PROJECT_EXT_6"
            }
        }
    }

    dependencies {
        snapshot(TestArea_LumeMarkets_CoreBuild_BuildOld) {
        }
    }
})

object TestArea_LumeMarkets_CoreBuild_HttpsGithubComDraganJotanovicCoreBackendRefsHeadsMain1 : GitVcsRoot({
    id("HttpsGithubComDraganJotanovicCoreBackendRefsHeadsMain1")
    name = "https://github.com/dragan-jotanovic/core-backend#refs/heads/main (1)"
    url = "https://github.com/dragan-jotanovic/core-backend"
    branch = "refs/heads/main"
    branchSpec = "refs/heads/*"
    authMethod = password {
        userName = "dragan-jotanovic"
        password = "credentialsJSON:698b6e57-3224-4b56-9ac2-d9cc0211cfbf"
    }
    param("pipelines.connectionId", "PROJECT_EXT_2")
})
