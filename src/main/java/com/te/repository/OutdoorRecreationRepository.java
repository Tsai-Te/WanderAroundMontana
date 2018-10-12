package com.te.repository;

import com.te.domain.OutdoorRecreation;
import org.springframework.data.repository.CrudRepository;

public interface OutdoorRecreationRepository extends CrudRepository <OutdoorRecreation, Long> {
    OutdoorRecreation findByCategoryIgnoreCase(String category);
    OutdoorRecreation findBySkiing(String skiing);
    OutdoorRecreation findByFishing(String fishing);
    OutdoorRecreation findByHiking(String hiking);
    OutdoorRecreation findByDistance(Double distance);
}
