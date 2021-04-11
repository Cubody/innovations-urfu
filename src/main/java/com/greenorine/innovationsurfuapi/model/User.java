package com.greenorine.innovationsurfuapi.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
@Data
public class User extends BaseEntity{
    @Column(name="full_name")
    private String fullName;

    @Column(name="id_card", unique = true)
    private String idCard;

    @Column(name="email", unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name="role")
    private Role role = Role.USER;

    @ManyToMany(fetch = FetchType.EAGER)
    @ToString.Exclude
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinTable(name = "user_votes",
    joinColumns = {@JoinColumn(name="user_id", referencedColumnName = "id")},
    inverseJoinColumns = {@JoinColumn(name = "application_id", referencedColumnName = "id")})
    private List<Application> votes;

    @ToString.Exclude
    @OneToMany(mappedBy = "author",
            fetch = FetchType.EAGER,
            orphanRemoval = true)
    private List<Application> applications;

    public boolean hasRole(Role role) {
        return this.role == role || this.role.equals(Role.SUPERUSER);
    }
}
