package tk.tarajki.meme


import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import tk.tarajki.meme.models.RoleName
import tk.tarajki.meme.services.RoleService

@Component
class DatabaseInitializer(
        private val roleService: RoleService
) : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        roleService.createIfNoExist(RoleName.ROLE_ADMIN)
        roleService.createIfNoExist(RoleName.ROLE_USER)
    }
}