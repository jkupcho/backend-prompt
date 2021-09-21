package com.jkupcho.vino.listener.repo

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name= "events")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType::class)
@EntityListeners(AuditingEntityListener::class)
class EventEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column(name = "event_type")
    val eventType: String? = null,

    @Type(type = "jsonb")
    @Column(name = "event_payload", columnDefinition = "jsonb")
    var eventPayload: Map<String, Any>? = null
) {

    @Column(name = "created_at")
    @CreatedDate
    lateinit var createdAt: LocalDateTime;

    @Column(name = "modified_at")
    @LastModifiedDate
    lateinit var modifiedAt: LocalDateTime;

}