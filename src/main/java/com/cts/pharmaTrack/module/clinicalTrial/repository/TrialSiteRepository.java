package com.cts.pharmaTrack.module.clinicalTrial.repository;

import com.cts.pharmaTrack.module.clinicalTrial.entity.TrialSite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TrialSiteRepository extends JpaRepository<TrialSite, Integer> {

    boolean existsByTrialId(int trialId);

    boolean existsByTrialIdAndSiteName(int trialId, String siteName);

    List<TrialSite> findByTrialId(int trialId);
}
