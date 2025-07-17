package dev.jlaguna.repositories

import dev.jlaguna.models.Note

object NotesRepository {

    fun getAll(): List<Note> {
        val notes = (0..10).map {
            Note(
               "Title $it",
                "Description $it",
                if (it % 3 == 0) Note.Type.AUDIO else Note.Type.TEXT
            )
        }

        return notes
    }
}