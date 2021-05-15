package demo

import demo.kx.uuid
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.*
import org.springframework.http.HttpStatus
import kotlin.random.Random

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = [
        "spring.datasource.url=jdbc:h2:mem:testdb"
    ]
)
class DemoTest(@Autowired val client: TestRestTemplate) {

    @Test
    fun `test if we post and retrieve data`() {
        val id = "${Random(100).nextInt()}".uuid()
        val message = Message(id, "some message")
        client.postForObject<Message>("/", message)

        val entity = client.getForEntity<String>("/$id")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).contains(message.id)
        assertThat(entity.body).contains(message.text)

        val msg = client.getForObject<Message>("/$id")!!
        assertThat(msg.id).isEqualTo(message.id)
        assertThat(msg.text).isEqualTo(message.text)
    }

    @Test
    fun `message not found`() {
        val id = "${Random(100).nextInt()}".uuid()
        val entity = client.getForEntity<String>("/$id")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }

}