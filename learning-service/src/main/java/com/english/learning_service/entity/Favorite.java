package com.english.learning_service.entity;

import com.english.learning_service.enums.ItemTypeEnum;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "favorite")
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_type", nullable = false)
    private ItemTypeEnum itemType;

    @Column(name = "item_id", nullable = false)
    private String itemId;

    @Column(name = "added_at")
    private LocalDateTime addedAt;
}
