package com.khaoula.clinique.repository;

import com.khaoula.clinique.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByPatientId(Long patientId);
    List<Notification> findByDestinataire(String destinataire);
}