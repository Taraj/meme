package tk.tarajki.meme.controllers


import org.springframework.security.core.annotation.AuthenticationPrincipal

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tk.tarajki.meme.security.UserPrincipal

@RestController
@RequestMapping("/api/v1/self")
class SelfController {
    @GetMapping("/")
    fun whoAmI(@AuthenticationPrincipal principal: UserPrincipal): String {
        return "Username: ${principal.username}"
    }



}
