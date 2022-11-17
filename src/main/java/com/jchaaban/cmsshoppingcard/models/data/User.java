package com.jchaaban.cmsshoppingcard.models.data;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Table(name = "users")
@Data
@Getter
@Setter
public class User extends IDBasedEntity implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Size(min = 2,message = "Username must be at least 2 characters long")
    private String username;

    @Size(min = 4,message = "Password must be at least 4 characters long")
    private String password;

    @Transient // not part of the table
    private String confirmPassword;

    @Column(name = "phone_number")
    @Pattern(regexp = "^[0-9]{3}-[0-9]{3}-[0-9]{4}$", message = "The phone number must be at least 9 digits long")
    private String phoneNumber;

    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "please enter a valid email")
    private String email;

    private boolean isAdmin;

    private boolean isEnabled;

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public boolean getIsEnabled() {
        return isEnabled;
    }

    public boolean isAdmin() {
        return isAdmin;
    }


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL) // delete orders when customer gets deleted
    private Set<Order> orders = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new LinkedList<>();
        if (isAdmin)
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        else
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return authorities;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
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
}
