package com.unigrad.funiverseappservice.entity.academic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class Specialization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String code;

    @ManyToOne
    @JoinColumn
    private Major major;

    @OneToMany(mappedBy = "specialization")
    @JsonIgnore
    private List<Curriculum> curricula;
}