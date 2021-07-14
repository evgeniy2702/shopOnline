package com.meatshop.shopOnline.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

/**
 @author Zhurenko Evgeniy  08.06.21
 */


@Getter
@Setter
@NoArgsConstructor
@Entity
@DynamicUpdate
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(name = "user")

public class User implements UserDetails {

    @Id
    @Column(name = "id_user")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    @NotEmpty
    private String first_name;

    @NotEmpty
    private String last_name;

    @Pattern(regexp = "^(\\+38\\(0)\\d{2}\\)\\d{7}$")
    private String phone;

    @NotEmpty(message = "Поле email не должно быть пустым")
    @Email(message = "Email должен быть валидным")
    private String email;
    private String city;
    private String country;
    private Boolean ban_user;
    private Boolean enabled;
    private String foto;


    @Past(message = "Дата рождения введена не корректно, она должна быть в прошлом времени")
    @Temporal(TemporalType.DATE)
    private Date born;

    private String twiter;
    private String facebook;
    private String instagram;
    private String git_hub;

    public User(String username) {
        this.username = username;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }


    @ManyToMany
    @JoinTable (name="user_roles",
            joinColumns=@JoinColumn (name="id_user"),
            inverseJoinColumns=@JoinColumn(name="id_role"))
    Set<Role> roles ;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
