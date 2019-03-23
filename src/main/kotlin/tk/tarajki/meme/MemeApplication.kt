package tk.tarajki.meme

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MemeApplication
fun main(args: Array<String>) {
    runApplication<MemeApplication>(*args)
}
// url: jdbc:mysql://192.168.56.101:3306/meme?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
