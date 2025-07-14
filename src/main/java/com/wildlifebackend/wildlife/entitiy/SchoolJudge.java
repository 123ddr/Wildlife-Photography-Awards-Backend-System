package com.wildlifebackend.wildlife.entitiy;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "school_judge")
@Getter
@Setter
public class SchoolJudge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long judgeId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "judge", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<SchoolJudging> judgings = new HashSet<>();

    public SchoolJudge(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public SchoolJudge() {
    }
}