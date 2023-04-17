package com.unigrad.funiverseappservice.entity.socialnetwork;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unigrad.funiverseappservice.entity.academic.Curriculum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserDetail implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(columnDefinition = "nvarchar(255)")
    private String name;

    private String code;

    private String identifyNumber;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String schoolYear;

    @Email
    @Column(unique = true)
    private String personalMail;

    @Email
    @Column(unique = true)
    private String eduMail;

    private String avatar;

    private String phoneNumber;

    private boolean isActive = true;

    @ManyToOne
    @JoinColumn
    private Curriculum curriculum;

    @OneToMany(mappedBy = "teacher")
    @JsonIgnore
    private List<Group> teachingGroup;

    @OneToMany(mappedBy = "owner")
    @JsonIgnore
    private List<Post> posts;

    @OneToMany(mappedBy = "actor")
    @JsonIgnore
    private List<Event> performedEvents;

    @OneToMany(mappedBy = "receiver")
    @JsonIgnore
    private List<Event> events;

    @OneToMany(mappedBy = "owner")
    @JsonIgnore
    private List<Comment> comments;

    @OneToMany(mappedBy = "userDetail")
    @JsonIgnore
    private List<TimetableEvent> timetableEvents;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.toString()));
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return personalMail;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return isActive;
    }

    @JsonIgnore
    public boolean isAdmin() {
        return Role.WORKSPACE_ADMIN.equals(role) || Role.SYSTEM_ADMIN.equals(role);
    }
}