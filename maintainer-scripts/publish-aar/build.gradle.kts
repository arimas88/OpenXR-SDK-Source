// Copyright 2021, Collabora, Ltd.
// SPDX-License-Identifier: BSL-1.0
plugins {
    id("maven-publish")
    signing
}

// These next few lines are just to make the version match the OpenXR release.
val root by extra(file("../.."))
project.ext["repoRoot"] = root
apply(file(File(root, "src/version.gradle")))

version = project.ext["versionOpenXR"].toString() + project.ext["versionQualifier"]

val siteUrl = "https://github.com/KhronosGroup/OpenXR-SDK-Source"
val gitUrl = "scm:git:https://github.com/KhronosGroup/OpenXR-SDK-Source.git"

signing {
    // val signingKeyId: String? by project
    // val signingKey: String? by project
    // val signingPassword: String? by project
    // useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
    // sign(file(File(root, "openxr_loader_for_android-${version}.aar")))
    sign(publishing.publications)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "openxr_loader_for_android"
            afterEvaluate {
                artifact(file(File(root, "openxr_loader_for_android-${version}.aar")))
            }
            pom {
                name.set("OpenXR Loader for Android")
                description.set("The AAR for the OpenXR Loader as used on Android.")
                url.set(siteUrl)
                licenses {
                    license {
                        name.set("Apache-2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                    // or MIT, but "OR" is not easy to express clearly in POM.
                    // license {
                    //     name.set("MIT")
                    //     url.set("https://spdx.org/licenses/MIT.txt")
                    // }
                }
                developers {
                    developer {
                        id.set("openxr-speceditor")
                        name.set("The Khronos Group, Inc. OpenXR Working Group")
                        email.set("openxr-speceditor AT khronos DOT org")
                    }
                }
                scm {
                    connection.set(gitUrl)
                    developerConnection.set(gitUrl)
                    url.set(siteUrl)
                }
            }
        }
        repositories {
            maven {
                name = "BuildDir"
                url = uri(layout.buildDirectory.dir("repo"))
            }
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/KhronosGroup/OpenXR-SDK-Source")
            }
            maven {
                name = "OSSRH"
                url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                credentials {
                    username = System.getenv("OSSRH_USER") ?: return@credentials
                    password = System.getenv("OSSRH_PASSWORD") ?: return@credentials
                }
            }
            maven {
                name = "OSSRH-Snapshots"
                url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
                credentials {
                    username = System.getenv("OSSRH_USER") ?: return@credentials
                    password = System.getenv("OSSRH_PASSWORD") ?: return@credentials
                }
            }
        }
    }
}
