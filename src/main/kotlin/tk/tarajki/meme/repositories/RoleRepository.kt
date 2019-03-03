package tk.tarajki.meme.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import tk.tarajki.meme.models.Role
import tk.tarajki.meme.models.RoleName

@Repository
interface RoleRepository : JpaRepository<Role, Long> {
    fun findRoleByName(name: RoleName): Role?
}