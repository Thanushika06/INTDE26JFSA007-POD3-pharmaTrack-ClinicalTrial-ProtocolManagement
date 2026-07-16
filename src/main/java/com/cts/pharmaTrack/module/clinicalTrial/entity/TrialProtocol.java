package com.cts.pharmaTrack.module.clinicalTrial.entity;

import com.cts.pharmaTrack.module.clinicalTrial.enums.ProtocolStatus;
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
import java.time.LocalDate;

@Entity
@Table(name = "trial_protocol")
@Data
@NoArgsConstructor
public class TrialProtocol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "protocol_id")
    private int protocolId;

    @Column(name = "trial_id", nullable = false)
    private int trialId;

    @Column(name = "version_number", nullable = false, length = 20)
    private String versionNumber;

    @Column(name = "inclusion_criteria", nullable = false,
            columnDefinition = "TEXT")
    private String inclusionCriteria;

    @Column(name = "exclusion_criteria", nullable = false,
            columnDefinition = "TEXT")
    private String exclusionCriteria;

    @Column(name = "endpoints", nullable = false, columnDefinition = "TEXT")
    private String endpoints;

    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProtocolStatus status = ProtocolStatus.Draft;
}