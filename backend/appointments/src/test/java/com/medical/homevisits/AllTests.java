package com.medical.homevisits.appointments;

import com.medical.homevisits.appointments.appointment.entity.Appointment;
import com.medical.homevisits.appointments.appointment.entity.AppointmentStatus;
import com.medical.homevisits.appointments.doctor.entity.Doctor;
import com.medical.homevisits.appointments.paramedic.entity.Paramedic;
import com.medical.homevisits.appointments.patient.entity.Patient;
import com.medical.homevisits.appointments.prescription.entity.Prescription;

import jakarta.transaction.Transactional;

import com.medical.homevisits.appointments.emergency.entity.EmergencyReport;
import com.medical.homevisits.appointments.emergency.entity.EmergencyStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.awt.PageAttributes.MediaType;
import java.time.LocalDateTime;
import java.util.UUID;


import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import static org.junit.jupiter.api.Assertions.*;


public class AllTests {

    @Test
    public void testPatientFields() {
        UUID patientId = UUID.randomUUID();
        Patient patient = new Patient();
        patient.setID(patientId);
        patient.setFirstName("Anna");
        patient.setLastName("Kowalska");

        assertEquals(patientId, patient.getID());
        assertEquals("Anna", patient.getFirstName());
        assertEquals("Kowalska", patient.getLastName());
    }

    @Test
    void shouldFailWhenDoctorFirstNameIsNull() {
        Doctor doctor = Doctor.builder()
                .ID(UUID.randomUUID())
                .firstName(null)
                .lastName("Nowak")
                .specialization("Kardiolog")
                .workPlace("Szpital")
                .build();

        assertNotNull(doctor.getFirstName(), "Imię nie powinno być nullem");
    }
    @Test
    public void testDoctorCreation() {
        UUID doctorId = UUID.randomUUID();
        Doctor doctor = new Doctor();
        doctor.setID(doctorId);
        doctor.setFirstName("Marek");
        doctor.setLastName("Nowak");

        assertEquals("Marek", doctor.getFirstName());
        assertEquals("Nowak", doctor.getLastName());
        assertEquals(doctorId, doctor.getID());
    }

    @Test
    public void testParamedicFields() {
        UUID paramedicId = UUID.randomUUID();
        Paramedic paramedic = new Paramedic();
        paramedic.setID(paramedicId);
        paramedic.setFirstName("Karol");
        paramedic.setLastName("Wiśniewski");

        assertEquals("Karol", paramedic.getFirstName());
        assertEquals("Wiśniewski", paramedic.getLastName());
    }

    @Test
    public void testEmergencyReportStatusChange() {
        EmergencyReport report = new EmergencyReport();
        report.setStatus(EmergencyStatus.Available);
        assertEquals(EmergencyStatus.Available, report.getStatus());

        Paramedic paramedic = new Paramedic();
        paramedic.setID(UUID.randomUUID());
        report.setParamedic(paramedic);
        report.setStatus(EmergencyStatus.In_progress);

        assertEquals(EmergencyStatus.In_progress, report.getStatus());
        assertEquals(paramedic.getID(), report.getParamedic().getID());
    }

    @Test
    public void testEmergencyReportFields() {
        Patient patient = new Patient();
        patient.setID(UUID.randomUUID());

        EmergencyReport report = new EmergencyReport();
        report.setPatient(patient);
        report.setAddress("ul. Zdrowa 15, Warszawa");
        report.setDescription("Zasłabnięcie");
        report.setEmergencyReportTime(LocalDateTime.now());

        assertEquals("ul. Zdrowa 15, Warszawa", report.getAddress());
        assertEquals("Zasłabnięcie", report.getDescription());
        assertEquals(patient, report.getPatient());
        assertNotNull(report.getEmergencyReportTime());
    }

    @Test
    public void testAppointmentCreation() {
        Appointment appointment = new Appointment();
        appointment.setID(UUID.randomUUID());
        appointment.setDoctor(new Doctor());
        appointment.setPatient(new Patient());
        appointment.setAppointmentStartTime(LocalDateTime.now().plusDays(1));
        appointment.setStatus(AppointmentStatus.AVAILABLE);
        appointment.setNotes("Kontrola po interwencji ratunkowej");

        assertEquals(AppointmentStatus.AVAILABLE, appointment.getStatus());
        assertEquals("Kontrola po interwencji ratunkowej", appointment.getNotes());
    }

    @Test
    public void testAppointmentTimeInFuture() {
        Appointment appointment = new Appointment();
        LocalDateTime futureDate = LocalDateTime.now().plusDays(3);
        appointment.setAppointmentStartTime(futureDate);

        assertTrue(appointment.getAppointmentStartTime().isAfter(LocalDateTime.now()));
    }

    @Test
    public void testPrescriptionBasicDetails() {
        Doctor doctor = new Doctor();
        doctor.setID(UUID.randomUUID());

        Patient patient = new Patient();
        patient.setID(UUID.randomUUID());

        Prescription prescription = new Prescription();
        prescription.setId(UUID.randomUUID());
        prescription.setDoctor(doctor);
        prescription.setPatient(patient);
        prescription.setPrescriptionTime(LocalDateTime.now().plusDays(1));
        prescription.setMedication("Ibuprofen");
        prescription.setDosage("200mg co 8h");
        prescription.setNotes("Nie przyjmować na pusty żołądek");

        assertEquals("Ibuprofen", prescription.getMedication());
        assertEquals("200mg co 8h", prescription.getDosage());
        assertEquals(doctor.getID(), prescription.getDoctor().getID());
        assertEquals(patient.getID(), prescription.getPatient().getID());
    }

    @Test
    public void testPrescriptionMedicationUpdate() {
        Prescription prescription = new Prescription();
        prescription.setMedication("Ibuprofen");
        prescription.setDosage("200mg co 8h");

        prescription.setMedication("Paracetamol");
        prescription.setDosage("500mg co 6h");

        assertEquals("Paracetamol", prescription.getMedication());
        assertEquals("500mg co 6h", prescription.getDosage());
    }

    @Test
    public void testPrescriptionWithoutDoctorIsNull() {
        Prescription prescription = new Prescription();
        assertNull(prescription.getDoctor());
    }
    
   



    @Test
    public void testChangingAppointmentStatus() {
        Appointment appointment = new Appointment();
        appointment.setStatus(AppointmentStatus.AVAILABLE);
        assertEquals(AppointmentStatus.AVAILABLE, appointment.getStatus());

        appointment.setStatus(AppointmentStatus.RESERVED);
        assertEquals(AppointmentStatus.RESERVED, appointment.getStatus());
    }
    @SpringBootTest
    @AutoConfigureMockMvc
    @AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
    @ActiveProfiles("test")
    class AppointmentsApiTests {

        @Autowired
        private MockMvc mvc;

        @Transactional
        @Test
        public void createAppointmentTest() throws Exception {
            String appointmentJson = """
                {
                  "doctorId": "d290f1ee-6c54-4b01-90e6-d701748f0851",
                  "patientId": "c4a7d123-7b0e-4af0-b302-356d8f8b25b1",
                  "appointmentStartTime": "2025-07-01T10:00:00",
                  "status": "AVAILABLE",
                  "notes": "Routine follow-up"
                }
            """;

            mvc.perform(post("/api/appointments")
                    .contentType("application/json")
                    .content(appointmentJson))
                    .andExpect(status().isOk());
        }

        @Transactional
        @Test
        public void createPrescriptionTest() throws Exception {
            String prescriptionJson = """
                {
                  "doctorId": "d290f1ee-6c54-4b01-90e6-d701748f0851",
                  "patientId": "c4a7d123-7b0e-4af0-b302-356d8f8b25b1",
                  "prescriptionTime": "2025-07-01T11:00:00",
                  "medication": "Ibuprofen",
                  "dosage": "200mg co 8h",
                  "notes": "After meals"
                }
            """;

            mvc.perform(post("/api/prescriptions")
                    .contentType("application/json")
                    .content(prescriptionJson))
                    .andExpect(status().isOk());
        }

        @Transactional
        @Test
        public void createEmergencyReportTest() throws Exception {
            String emergencyJson = """
                {
                  "patientId": "c4a7d123-7b0e-4af0-b302-356d8f8b25b1",
                  "address": "ul. Zdrowa 15, Warszawa",
                  "description": "Zasłabnięcie",
                  "emergencyReportTime": "2025-07-01T09:30:00",
                  "status": "Available"
                }
            """;

            mvc.perform(post("/api/emergencies")
                    .contentType("application/json")
                    .content(emergencyJson))
                    .andExpect(status().isOk());
        }
    }

}
