package com.medical.homevisits.appointments.workplace.entity;

import com.medical.homevisits.appointments.doctor.entity.Doctor;
import com.medical.homevisits.appointments.nurse.entity.Nurse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name="workplace")
public class Workplace {
    @Id
    private UUID ID;
    private String name;

    private String street;
    private String city;
    private String postalCode;
    private String country;

    private String phoneNumber;
    private String email;

    @OneToMany
    @JoinColumn(name="doctors", nullable = true)
    private List<Doctor> doctor;
    @OneToMany
    @JoinColumn(name="nurses", nullable = true)
    private List<Nurse> nurse;
}
