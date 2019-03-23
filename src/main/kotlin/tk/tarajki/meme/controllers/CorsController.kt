package tk.tarajki.meme.controllers

import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse


@CrossOrigin
@RestController
class CorsController {

    @RequestMapping(value = ["/**"], method = [RequestMethod.OPTIONS])
    fun corsHeaders(response: HttpServletResponse) {
        response.addHeader("Access-Control-Allow-Origin", "*")
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
        response.addHeader("Access-Control-Allow-Headers", "origin, content-type, accept, x-requested-with")
        response.addHeader("Access-Control-Max-Age", "3600")
    }

}