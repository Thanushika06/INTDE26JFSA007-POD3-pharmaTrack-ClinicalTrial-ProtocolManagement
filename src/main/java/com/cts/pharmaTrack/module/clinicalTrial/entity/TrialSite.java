package com.cts.pharmaTrack.module.clinicalTrial.entity;

import com.cts.pharmaTrack.module.clinicalTrial.enums.SiteStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "trial_site")
@Data
@NoArgsConstructor
public class TrialSite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "site_id")
    private int siteId;

    @Column(name = "trial_id", nullable = false)
    private int trialId;

    @Column(name = "site_name", nullable = false, length = 150)
    private String siteName;

    @Column(name = "country", nullable = false, length = 100)
    private String country;

    @Column(name = "principal_investigator_id", nullable = false)
    private int principalInvestigatorId;

    @Column(name = "planned_subjects", nullable = false)
    private int plannedSubjects;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SiteStatus status = SiteStatus.Active;
}