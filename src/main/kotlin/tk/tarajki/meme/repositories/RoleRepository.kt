package tk.tarajki.meme.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import tk.tarajki.meme.models.Role
import tk.tarajki.meme.models.RoleName

@Repository
interface RoleRepository : JpaRepository<Role, Long> {
    fun findRoleByName(name: RoleName): Role?
}