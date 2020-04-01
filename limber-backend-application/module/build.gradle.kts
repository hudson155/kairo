subprojects {
    val baseModule = ":limber-backend-application:module:[a-z\\-]+"
    when {
        project.path.matches(Regex("$baseModule:api")) -> {
            dependencies {
                implementation(Dependencies.Jackson.annotations)
                implementation(project(":limber-backend-application:common:api"))
            }
        }
        project.path.matches(Regex("$baseModule:app")) -> {
            dependencies {
                api(project(":limber-backend-application:module:${project.parent!!.name}:api"))
                implementation(project(":limber-backend-application:common"))
                implementation(project(":limber-backend-application:common:api"))
                testImplementation(project(":limber-backend-application:common:testing"))
            }
        }
        project.path.matches(Regex(baseModule)) -> Unit
        else -> error("Unexpected project path: " + project.path)
    }
}
