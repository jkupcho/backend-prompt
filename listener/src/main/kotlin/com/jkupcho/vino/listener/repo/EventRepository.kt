package com.jkupcho.vino.listener.repo

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EventRepository : CrudRepository<EventEntity, Int> {
}