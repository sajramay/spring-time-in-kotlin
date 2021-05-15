package demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.MediaType
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.query
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*
import demo.kx.*
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.server.ResponseStatusException

@SpringBootApplication
class Demo

fun main(args: Array<String>) {
    runApplication<Demo>(*args)
}

@Controller
class HelloController {

    @GetMapping("/hello")
    fun index(model: Model): String {
        model["message"] = "Hello"
        return ""
    }
}

@RestController
class MessageResource(val messageService: MessageService) {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun index() : List<Message> = messageService.findMessages()

    @GetMapping("/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun index(@PathVariable("id") id : String) : Message =
        messageService.findMessageById(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Message not found")

    @PostMapping
    fun post(@RequestBody message: Message) {
        messageService.post(message)
    }

}

@Service
class MessageService(val db: JdbcTemplate) {

    fun findMessages(): List<Message> = db.query("select * from messages") {
        rs, _ -> Message(rs.getString("id"), rs.getString("text"))
    }

    fun findMessageById(id: String) : Message? = db.query("select * from messages where id = ?",id) {
            rs, _ -> Message(rs.getString("id"), rs.getString("text"))
    }.firstOrNull()

    fun post(message: Message) = db.update("insert into messages values (?, ?)", message.id ?: message.text.uuid(), message.text)
}


data class Message(val id: String?, val text: String)