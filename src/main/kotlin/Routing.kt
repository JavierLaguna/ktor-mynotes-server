package dev.jlaguna

import dev.jlaguna.models.Note
import dev.jlaguna.repositories.NotesRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.html.respondHtml
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.head
import kotlinx.html.p
import kotlinx.html.title


fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        htmlRoutes()

        notesRoutes()
    }
}

private fun Routing.htmlRoutes() {
    route("html") {
        get {
            call.respondHtml(status = HttpStatusCode.OK) {
                head {
                    title { +"jLagunaDev Ktor" }
                }
                body {
                    h1 { +"This is a h1" }
                    p { +"This is a paragraph" }
                    div {
                        a(href = "html/clicked") { +"Click me!" }
                    }
                }
            }
        }

        get("clicked") {
            call.respondHtml(status = HttpStatusCode.OK) {
                head {
                    title { +"Clicked" }
                }
                body {
                    h1 { +"Button Clicked!" }
                }
            }
        }
    }
}

private fun Routing.notesRoutes() {
    route("notes") {
        // CREATE
        post {
            try {
                val note = call.receive<Note>()
                val savedNote = NotesRepository.save(note)
                call.respond(HttpStatusCode.Created, savedNote)

            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    "Bad JSON data body: ${e.message}"
                )
            }
        }

        // READ
        get {
            call.respond(NotesRepository.getAll())
        }

        get("{id}") {
            val id = call.parameters["id"] ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                "Missing or invalid id"
            )

            val note = NotesRepository.getById(id.toLong()) ?: return@get call.respond(
                HttpStatusCode.NotFound,
                "Note not found with $id"
            )

            call.respond(note)
        }

        // UPDATE
        put {
            try {
                val note = call.receive<Note>()
                if (NotesRepository.update(note)) {
                    call.respond(note)

                } else {
                    call.respond(
                        HttpStatusCode.NotFound,
                        "Note not found with ${note.id}"
                    )
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    "Bad JSON data body: ${e.message}"
                )
            }
        }

        // DELETE
        delete("{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(
                HttpStatusCode.BadRequest,
                "Missing or invalid id"
            )

            if (NotesRepository.delete(id.toLong())) {
                call.respond(HttpStatusCode.Accepted)

            } else {
                call.respond(
                    HttpStatusCode.NotFound,
                    "Note not found with $id"
                )
            }
        }
    }
}