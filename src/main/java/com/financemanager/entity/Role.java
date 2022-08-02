package com.financemanager.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "roles")
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter
    @Setter
    private Integer id;
    
    @NotBlank
    @Column(name = "name")
    @Getter
    @Setter
    private String name;
    public Role(Integer id, @NotBlank String name) {
        super();
        this.id = id;
        this.name = name;
    }

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(
            name = "\"role_permissions\"",
            joinColumns = @JoinColumn(name = "\"role_id\""),
            inverseJoinColumns = @JoinColumn(name = "\"permission_id\""))
    private List<Permission> permissions;
    
    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> userPermissions = null;
        if(permissions == null || permissions.isEmpty()) {
            return userPermissions;
        }
        userPermissions = permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getName())).collect(Collectors.toSet());
        userPermissions.add(new SimpleGrantedAuthority(this.name));
        return userPermissions;
    }

}
