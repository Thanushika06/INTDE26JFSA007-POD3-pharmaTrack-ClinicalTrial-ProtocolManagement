package com.cts.pharmaTrack.module.clinicalTrial.service;

import com.cts.pharmaTrack.module.clinicalTrial.dto.request.TrialSiteRequestDTO;
import com.cts.pharmaTrack.module.clinicalTrial.dto.response.TrialSiteResponseDTO;
import com.cts.pharmaTrack.module.clinicalTrial.entity.ClinicalTrial;
import com.cts.pharmaTrack.module.clinicalTrial.entity.TrialSite;
import com.cts.pharmaTrack.module.clinicalTrial.enums.SiteStatus;
import com.cts.pharmaTrack.module.clinicalTrial.enums.TrialStatus;
import com.cts.pharmaTrack.module.clinicalTrial.exception.SiteNotFoundException;
import com.cts.pharmaTrack.module.clinicalTrial.exception.TrialNotFoundException;
import com.cts.pharmaTrack.module.clinicalTrial.repository.ClinicalTrialRepository;
import com.cts.pharmaTrack.module.clinicalTrial.repository.TrialSiteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrialSiteService {

    private static final Logger logger =
            LoggerFactory.getLogger(TrialSiteService.class);

    @Autowired
    private TrialSiteRepository trialSiteRepository;

    @Autowired
    private ClinicalTrialRepository clinicalTrialRepository;

    // ── CREATE SITE ───────────────────────────────────────────────────────────

    public TrialSiteResponseDTO createSite(
            int trialId, TrialSiteRequestDTO request) {

        logger.info(
                "Request received to create site for trialId: {}",
                trialId);

        ClinicalTrial trial = clinicalTrialRepository
                .findById(trialId)
                .orElseThrow(() -> {
                    logger.error("Trial not found for trialId: {}",
                            trialId);
                    return new TrialNotFoundException(trialId);
                });

        if (trial.getStatus() == TrialStatus.Terminated) {
            logger.error(
                    "Cannot add site to Terminated trial: {}", trialId);
            throw new IllegalStateException(
                    "Cannot add site to a Terminated trial");
        }

        if (request.getSiteName() == null
                || request.getSiteName().trim().isEmpty()) {
            throw new IllegalArgumentException("Site name is mandatory");
        }
        if (request.getCountry() == null
                || request.getCountry().trim().isEmpty()) {
            throw new IllegalArgumentException("Country is mandatory");
        }
        if (request.getPrincipalInvestigatorId() <= 0) {
            throw new IllegalArgumentException(
                    "Principal investigator ID is mandatory");
        }
        if (request.getPlannedSubjects() <= 0) {
            throw new IllegalArgumentException(
                    "Planned subjects must be greater than zero");
        }

        if (trialSiteRepository.existsByTrialIdAndSiteName(
                trialId, request.getSiteName().trim())) {
            logger.error(
                    "Duplicate site name {} for trialId: {}",
                    request.getSiteName(), trialId);
            throw new IllegalStateException(
                    "Site name already exists for this trial");
        }

        TrialSite trialSite = new TrialSite();
        trialSite.setTrialId(trialId);
        trialSite.setSiteName(request.getSiteName().trim());
        trialSite.setCountry(request.getCountry().trim());
        trialSite.setPrincipalInvestigatorId(
                request.getPrincipalInvestigatorId());
        trialSite.setPlannedSubjects(request.getPlannedSubjects());

        TrialSite savedSite = trialSiteRepository.save(trialSite);
        logger.info(
                "Site created successfully with siteId: {}",
                savedSite.getSiteId());

        return mapToResponseDTO(savedSite);
    }

    // ── GET ALL SITES ─────────────────────────────────────────────────────────

    public List<TrialSiteResponseDTO> getAllSites(int trialId) {
        logger.info(
                "Request received to get all sites for trialId: {}",
                trialId);
        clinicalTrialRepository
                .findById(trialId)
                .orElseThrow(() -> {
                    logger.error("Trial not found for trialId: {}",
                            trialId);
                    return new TrialNotFoundException(trialId);
                });
        List<TrialSite> sites =
                trialSiteRepository.findByTrialId(trialId);
        if (sites.isEmpty()) {
            logger.info("No sites found for trialId: {}", trialId);
            return Collections.emptyList();
        }
        logger.info("Fetched {} sites for trialId: {}",
                sites.size(), trialId);
        return sites.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // ── GET SITE BY ID ────────────────────────────────────────────────────────

    public TrialSiteResponseDTO getSiteById(int trialId, int siteId) {
        logger.info(
                "Request received to get site with siteId: {}" +
                " for trialId: {}", siteId, trialId);
        clinicalTrialRepository
                .findById(trialId)
                .orElseThrow(() -> {
                    logger.error("Trial not found for trialId: {}",
                            trialId);
                    return new TrialNotFoundException(trialId);
                });
        TrialSite trialSite = trialSiteRepository
                .findById(siteId)
                .orElseThrow(() -> {
                    logger.error(
                            "Site not found with siteId: {}", siteId);
                    return new SiteNotFoundException(siteId);
                });
        logger.info("Site fetched successfully with siteId: {}",
                siteId);
        return mapToResponseDTO(trialSite);
    }

    // ── UPDATE SITE ───────────────────────────────────────────────────────────

    public TrialSiteResponseDTO updateSite(
            int trialId, int siteId,
            TrialSiteRequestDTO request) {

        logger.info(
                "Request received to update site with siteId: {}" +
                " for trialId: {}", siteId, trialId);

        clinicalTrialRepository
                .findById(trialId)
                .orElseThrow(() -> {
                    logger.error("Trial not found for trialId: {}",
                            trialId);
                    return new TrialNotFoundException(trialId);
                });

        TrialSite trialSite = trialSiteRepository
                .findById(siteId)
                .orElseThrow(() -> {
                    logger.error(
                            "Site not found with siteId: {}", siteId);
                    return new SiteNotFoundException(siteId);
                });

        if (trialSite.getStatus() == SiteStatus.Closed) {
            logger.error(
                    "Site {} cannot be updated as status is Closed",
                    siteId);
            throw new IllegalArgumentException(
                    "Site cannot be updated when status is Closed");
        }

        if (request.getSiteName() == null
                || request.getSiteName().trim().isEmpty()) {
            throw new IllegalArgumentException("Site name is mandatory");
        }
        if (request.getCountry() == null
                || request.getCountry().trim().isEmpty()) {
            throw new IllegalArgumentException("Country is mandatory");
        }
        if (request.getPrincipalInvestigatorId() <= 0) {
            throw new IllegalArgumentException(
                    "Principal investigator ID is mandatory");
        }
        if (request.getPlannedSubjects() <= 0) {
            throw new IllegalArgumentException(
                    "Planned subjects must be greater than zero");
        }

        trialSite.setSiteName(request.getSiteName().trim());
        trialSite.setCountry(request.getCountry().trim());
        trialSite.setPrincipalInvestigatorId(
                request.getPrincipalInvestigatorId());
        trialSite.setPlannedSubjects(request.getPlannedSubjects());

        TrialSite updatedSite = trialSiteRepository.save(trialSite);
        logger.info(
                "Site updated successfully with siteId: {}", siteId);

        return mapToResponseDTO(updatedSite);
    }

    // ── UPDATE SITE STATUS ────────────────────────────────────────────────────

    public TrialSiteResponseDTO updateSiteStatus(
            int trialId, int siteId, String newStatus) {

        logger.info(
                "Request received to update status for siteId: {}" +
                " trialId: {}", siteId, trialId);

        clinicalTrialRepository
                .findById(trialId)
                .orElseThrow(() -> {
                    logger.error("Trial not found for trialId: {}",
                            trialId);
                    return new TrialNotFoundException(trialId);
                });

        TrialSite trialSite = trialSiteRepository
                .findById(siteId)
                .orElseThrow(() -> {
                    logger.error(
                            "Site not found with siteId: {}", siteId);
                    return new SiteNotFoundException(siteId);
                });

        SiteStatus currentStatus = trialSite.getStatus();
        SiteStatus requestedStatus;

        try {
            requestedStatus = SiteStatus.valueOf(newStatus);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid status value: {}", newStatus);
            throw new IllegalArgumentException(
                    "Invalid status value: " + newStatus +
                    ". Allowed values are Active, OnHold, Closed");
        }

        validateSiteStatusTransition(currentStatus, requestedStatus);

        trialSite.setStatus(requestedStatus);
        TrialSite updatedSite = trialSiteRepository.save(trialSite);
        logger.info(
                "Site status updated to {} for siteId: {}",
                newStatus, siteId);

        return mapToResponseDTO(updatedSite);
    }

    // ── STATUS TRANSITION VALIDATOR ───────────────────────────────────────────

    private void validateSiteStatusTransition(
            SiteStatus current, SiteStatus next) {

        boolean valid = false;

        switch (current) {
            case Active:
                valid = next == SiteStatus.OnHold
                        || next == SiteStatus.Closed;
                break;
            case OnHold:
                valid = next == SiteStatus.Active
                        || next == SiteStatus.Closed;
                break;
            case Closed:
                valid = false;
                break;
            default:
                valid = false;
        }

        if (!valid) {
            logger.error(
                    "Invalid status transition from {} to {}",
                    current, next);
            throw new IllegalArgumentException(
                    "Invalid status transition from "
                    + current + " to " + next);
        }
    }

    // ── MAPPER ────────────────────────────────────────────────────────────────

    private TrialSiteResponseDTO mapToResponseDTO(TrialSite savedSite) {
        TrialSiteResponseDTO response = new TrialSiteResponseDTO();
        response.setSiteId(savedSite.getSiteId());
        response.setTrialId(savedSite.getTrialId());
        response.setSiteName(savedSite.getSiteName());
        response.setCountry(savedSite.getCountry());
        response.setPrincipalInvestigatorId(
                savedSite.getPrincipalInvestigatorId());
        response.setPlannedSubjects(savedSite.getPlannedSubjects());
        response.setStatus(savedSite.getStatus().name());
        response.setMessage("Site updated successfully");
        return response;
    }
}
