package tk.tarajki.meme.services

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import tk.tarajki.meme.models.Role
import tk.tarajki.meme.models.RoleName
import tk.tarajki.meme.repositories.RoleRepository

@Component
class RoleService(
        private val roleRepository: RoleRepository
) {
    @Transactional
    fun createIfNoExist(name: RoleName) {
        roleRepository.findRoleByName(name) ?: roleRepository.save(
                Role(
                        name = name
                )
        )
    }
}