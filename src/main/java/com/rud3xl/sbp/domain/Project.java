package com.rud3xl.sbp.domain;

import com.rud3xl.sbp.domain.enums.ProjectStatus;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "projects", uniqueConstraints = {@UniqueConstraint(name = "uk_projects_org_name", columnNames = {"organization_id", "name"})}
)
@Entity
public class Project extends AbstractEntity{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ProjectStatus status = ProjectStatus.ACTIVE;
}
