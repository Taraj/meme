package tk.tarajki.meme

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MemeApplication

fun main(args: Array<String>) {
    runApplication<MemeApplication>(*args)
}
