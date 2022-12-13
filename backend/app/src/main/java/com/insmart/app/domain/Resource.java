package com.insmart.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
@Entity
@Table(name= "resources")
@Data@AllArgsConstructor
@NoArgsConstructor
public class Resource implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resource_id")
    private Long id;
    private String code;
    private String name;
    private String url;
    private String description;
    @ManyToOne(targetEntity = Resource.class)
    @JoinColumn(name = "parent_id")
    private Resource parent;
    @OneToMany(targetEntity = Permission.class,mappedBy = "resource")
    @JsonIgnore
    private Collection<Resource> resources ;

    public Resource(Long id, String code, String name, String url, String description, Resource  parent) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.url = url;
        this.description = description;
        this.parent = parent;
    }
}
