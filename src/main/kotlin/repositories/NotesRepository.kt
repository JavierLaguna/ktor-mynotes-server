package dev.jlaguna.repositories

import dev.jlaguna.models.Note

object NotesRepository {

    private val list = mutableListOf<Note>()
    private var currentId = 1L

    fun save(note: Note): Note {
        val newNote = note.copy(id = currentId)
        currentId++
        list.add(newNote)
        return newNote
    }

    fun getAll(): List<Note> = list

    fun getById(id: Long): Note? = list.find { it.id == id }

    fun update(note: Note): Boolean {
        val index = list.indexOfFirst { it.id == note.id }
        if (index < 0) return false

        list[index] = note
        return true
    }

    fun delete(id: Long): Boolean {
        val index = list.indexOfFirst { it.id == id }
        if (index < 0) return false

        list.removeAt(index)
        return true
    }
}