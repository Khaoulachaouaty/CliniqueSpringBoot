package com.khaoula.clinique.restcontrollers;

import com.khaoula.clinique.entities.Patient;
import com.khaoula.clinique.entities.User;
import com.khaoula.clinique.service.PatientService;  // ← AJOUTE
import com.khaoula.clinique.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserRESTController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private PatientService patientService;  // ← AJOUTE
    
    // ✅ CORRIGÉ : Utilise PatientService pour l'inscription patient
    @PostMapping("/register")
    public Patient register(@RequestBody Patient patient) {
        return patientService.savePatient(patient);
    }
    
    @PostMapping("/save")
    public User saveUser(@RequestBody User user) {
        return userService.saveUser(user);
    }
    
    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
    
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }
    
    @GetMapping("/find")
    public User findByUsername(@RequestParam String username) {
        return userService.findByUsername(username);
    }
    
    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}