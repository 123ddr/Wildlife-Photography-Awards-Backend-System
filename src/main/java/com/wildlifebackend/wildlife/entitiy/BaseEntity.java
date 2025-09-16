package com.wildlifebackend.wildlife.entitiy;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;


@Data
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity {
    @Version
    private Long version;
    @CreationTimestamp
    private LocalDate createdAt;
    @Column
    private Long createdBy;
    @Column
    private LocalDate updatedAt;

}
