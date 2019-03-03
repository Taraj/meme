package tk.tarajki.meme.repositories

import org.springframework.data.jpa.repository.JpaRepository
import tk.tarajki.meme.models.Warn

interface WarnRepository :JpaRepository<Warn, Long>