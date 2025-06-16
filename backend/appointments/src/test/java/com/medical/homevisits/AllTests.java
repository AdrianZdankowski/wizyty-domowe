package com.medical.homevisits.appointments;

import com.medical.homevisits.appointments.appointment.entity.Appointment;
import com.medical.homevisits.appointments.appointment.entity.AppointmentStatus;
import com.medical.homevisits.appointments.doctor.entity.Doctor;
import com.medical.homevisits.appointments.emergency.entity.EmergencyReport;
import com.medical.homevisits.appointments.emergency.entity.EmergencyStatus;
import com.medical.homevisits.appointments.nurse.entity.Nurse;
import com.medical.homevisits.appointments.paramedic.entity.Paramedic;
import com.medical.homevisits.appointments.patient.entity.Patient;
import com.medical.homevisits.appointments.prescription.entity.Prescription;
import com.medical.homevisits.appointments.workplace.entity.Workplace;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ActiveProfiles("test")
public class AllTests {

    @Autowired
    private MockMvc mvc;

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
    public void testDoctorWithWorkplace() {
        Workplace wp = new Workplace();
        wp.setID(UUID.randomUUID());
        wp.setName("Szpital Miejski");

        Doctor doctor = new Doctor();
        doctor.setID(UUID.randomUUID());
        doctor.setFirstName("Jan");
        doctor.setLastName("Kowalski");
        doctor.setWorkPlace(wp.getName());

        assertEquals("Szpital Miejski", doctor.getWorkPlace());
    }

    @Test
    public void testNurseWithWorkplace() {
        Workplace wp = new Workplace();
        wp.setID(UUID.randomUUID());
        wp.setName("Przychodnia Zdrowie");

        Nurse nurse = new Nurse();
        nurse.setID(UUID.randomUUID());
        nurse.setFirstName("Ewa");
        nurse.setLastName("Nowak");

        assertNotNull(nurse.getFirstName());
        assertEquals("Ewa", nurse.getFirstName());
    }

    @Test
    public void testParamedicWithWorkplace() {
        Workplace wp = new Workplace();
        wp.setID(UUID.randomUUID());
        wp.setName("Pogotowie");

        Paramedic paramedic = new Paramedic();
        paramedic.setID(UUID.randomUUID());
        paramedic.setFirstName("Karol");
        paramedic.setLastName("Wiśniewski");

        assertEquals("Karol", paramedic.getFirstName());
    }

    @Test
    public void testEmergencyReportStatusChange() {
        EmergencyReport report = new EmergencyReport();
        report.setStatus(EmergencyStatus.Available);
        assertEquals(EmergencyStatus.Available, report.getStatus());

        report.setStatus(EmergencyStatus.In_progress);
        assertEquals(EmergencyStatus.In_progress, report.getStatus());
    }

    @Test
    public void testAppointmentCreation() {
        Appointment appointment = new Appointment();
        appointment.setID(UUID.randomUUID());
        appointment.setDoctor(new Doctor());
        appointment.setPatient(new Patient());
        appointment.setAppointmentStartTime(LocalDateTime.now().plusDays(1));
        appointment.setStatus(AppointmentStatus.AVAILABLE);
        appointment.setNotes("Kontrola po interwencji");

        assertEquals(AppointmentStatus.AVAILABLE, appointment.getStatus());
    }

    @Test
    public void testPrescriptionDetails() {
        Prescription prescription = new Prescription();
        prescription.setMedication("Ibuprofen");
        prescription.setDosage("200mg");

        assertEquals("Ibuprofen", prescription.getMedication());
        assertEquals("200mg", prescription.getDosage());
    }

    @Test
    public void testChangingAppointmentStatus() {
        Appointment appointment = new Appointment();
        appointment.setStatus(AppointmentStatus.AVAILABLE);
        appointment.setStatus(AppointmentStatus.RESERVED);

        assertEquals(AppointmentStatus.RESERVED, appointment.getStatus());
    }


    @Transactional
    @Test
    public void createAppointmentApiTest() throws Exception {
        String json = """
            {
              "doctorId": "11111111-1111-1111-1111-111111111111",
              "patientId": "22222222-2222-2222-2222-222222222222",
              "appointmentStartTime": "2025-07-01T10:00:00",
              "status": "AVAILABLE",
              "notes": "Kontrola stanu zdrowia"
            }
        """;

        mvc.perform(post("/api/appointments")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isOk());
    }

    @Transactional
    @Test
    public void createPrescriptionApiTest() throws Exception {
        String json = """
            {
              "doctorId": "11111111-1111-1111-1111-111111111111",
              "patientId": "22222222-2222-2222-2222-222222222222",
              "prescriptionTime": "2025-07-01T11:00:00",
              "medication": "Ibuprofen",
              "dosage": "200mg co 8h",
              "notes": "Po posiłku"
            }
        """;

        mvc.perform(post("/api/prescriptions")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isOk());
    }

    @Transactional
    @Test
    public void createEmergencyReportApiTest() throws Exception {
        String json = """
            {
              "patientId": "22222222-2222-2222-2222-222222222222",
              "address": "ul. Zdrowa 15, Warszawa",
              "description": "Zasłabnięcie",
              "emergencyReportTime": "2025-07-01T09:30:00",
              "status": "Available"
            }
        """;

        mvc.perform(post("/api/emergencies")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isOk());
    }
}
