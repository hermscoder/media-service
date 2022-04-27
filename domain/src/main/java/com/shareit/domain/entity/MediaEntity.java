package com.shareit.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "media")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediaEntity {
    @Id
    @Column(name = "media_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Enumerated(EnumType.STRING)
    private MediaType type;
    private String url;
    private String publicId;
    private Boolean pendingDeletion;

    public MediaEntity(Long id, MediaType type, String url, String publicId) {
        this(id, type, url, publicId, false);
    }

}
