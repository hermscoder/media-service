package com.shareit.data.repository;

import com.shareit.domain.entity.MediaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface MediaRepository extends JpaRepository<MediaEntity, Long> {

    @Query("SELECT m FROM MediaEntity m " +
            "WHERE m.id = ?1 AND m.pendingDeletion <> true ")
    Optional<MediaEntity> findById(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE MediaEntity m " +
            "SET m.pendingDeletion = true  WHERE m.id in ?1")
    int setMediaToBeDeleted(Long... id);

    @Query("SELECT m FROM MediaEntity m WHERE m.pendingDeletion = true")
    Optional<List<MediaEntity>> findAllToBeDeleted();

    @Transactional
    @Modifying
    @Query("UPDATE MediaEntity m " +
            "SET m.publicId = ?2, m.url = ?3  WHERE m.id in ?1")
    int updateMediaUploadInformation(Long id, String publicId, String url);
}
