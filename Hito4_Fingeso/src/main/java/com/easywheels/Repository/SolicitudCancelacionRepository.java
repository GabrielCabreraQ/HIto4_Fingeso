package com.easywheels.Repository;

import com.easywheels.Model.SolicitudCancelacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitudCancelacionRepository  extends JpaRepository<SolicitudCancelacion, Integer> {
}
