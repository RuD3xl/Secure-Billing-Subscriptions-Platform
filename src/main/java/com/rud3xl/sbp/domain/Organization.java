package com.rud3xl.sbp.domain;

import com.rud3xl.sbp.domain.enums.OrganizationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "organizations")
@Entity
public class Organization extends AbstractEntity{
    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrganizationStatus status;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Membership> memberships;
}
