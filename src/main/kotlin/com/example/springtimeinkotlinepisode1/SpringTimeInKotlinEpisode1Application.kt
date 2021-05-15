package com.example.springtimeinkotlinepisode1

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.CrudRepository
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class SpringTimeInKotlinEpisode1Application

fun main(args: Array<String>) {
    runApplication<SpringTimeInKotlinEpisode1Application>(*args)
}

@RestController
class MessageResource(val messageService: MessageService) {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun index() : List<Message> = messageService.findMessages()

    @PostMapping
    fun post(@RequestBody message: Message) = messageService.post(message)
}

@Service
class MessageService(val db: MessageRepository) {

    fun findMessages(): List<Message> = db.findAll().distinct()

    fun post(message: Message) = db.save(message)
}

interface MessageRepository : CrudRepository<Message, String>

@Table("MESSAGES")
data class Message(@Id val id: String?, val text: String)