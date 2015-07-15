package com.github.atdi.office.server.services.repositories;

import com.github.atdi.office.model.Office;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by aurelavramescu on 14/07/15.
 */
public interface OfficeRepository extends CrudRepository<Office, String> {
}
