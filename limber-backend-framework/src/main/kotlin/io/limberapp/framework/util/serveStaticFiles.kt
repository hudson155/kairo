package io.limberapp.framework.util

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.content.LocalFileContent
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.util.combineSafe
import java.io.File

internal fun Application.serveStaticFiles(rootPath: String, defaultFilePath: String) {
    val path = "path"
    routing {
        route("/{$path...}") {
            val root = File(rootPath)
            get {
                val filePath = call.parameters.getAll(path)!!.joinToString(File.separator)
                val file = root.combineSafe(filePath)
                if (file.isFile) {
                    call.respond(LocalFileContent(file))
                } else {
                    call.respond(LocalFileContent(root.combineSafe(defaultFilePath)))
                }
            }
        }
    }
}
