package com.medical.homevisits.appointments.appointment.entity;

import com.medical.homevisits.appointments.doctor.entity.Doctor;
import com.medical.homevisits.appointments.patient.entity.Patient;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status; //status of the appointment (reserved, available, cancelled or completed)
    private LocalDateTime appointmentStartTime;
    private LocalDateTime appointmentEndTime;
    @ManyToOne
    @JoinColumn(name="doctors")
    private Doctor doctor;
    @ManyToOne
    @JoinColumn(name="patients")
    private Patient patient;
    private String address;
    private String notes;   //available for doctors to add additional info about appointment
}

