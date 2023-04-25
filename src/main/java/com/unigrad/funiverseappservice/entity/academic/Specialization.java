package com.unigrad.funiverseappservice.entity.academic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;


/**
 * The class represents a specialization of a major. For example:
 * <p>
 * Major: Information Technology will have many specializations like Software Engineering,
 * Graphic Design, Information System, Artificial Intelligence
 * </p>
 *
 *
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Specialization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "nvarchar(255)")
    private String name;

    private String code;

    @ManyToOne
    @JoinColumn
    private Major major;

    private String studentCode;

    private boolean isActive = true;

    @OneToMany(mappedBy = "specialization")
    @JsonIgnore
    private List<Curriculum> curricula;
}