package com.khaoula.clinique.restcontrollers;

import com.khaoula.clinique.entities.Notification;
import com.khaoula.clinique.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationRESTController {
    
    @Autowired
    private NotificationService notificationService;
    
    @PostMapping("/save")
    public Notification saveNotification(@RequestBody Notification notification) {
        return notificationService.saveNotification(notification);
    }
    
    @PostMapping("/envoyer")
    public Notification envoyerNotification(
            @RequestParam Long patientId,
            @RequestParam String message) {
        return notificationService.envoyerNotificationPatient(patientId, message);
    }
    
    @GetMapping("/all")
    public List<Notification> getAllNotifications() {
        return notificationService.getAllNotifications();
    }
    
    @GetMapping("/patient/{patientId}")
    public List<Notification> getNotificationsByPatient(@PathVariable Long patientId) {
        return notificationService.getNotificationsByPatient(patientId);
    }
    
    @PutMapping("/lire/{id}")
    public void marquerCommeLue(@PathVariable Long id) {
        notificationService.marquerCommeLue(id);
    }
}