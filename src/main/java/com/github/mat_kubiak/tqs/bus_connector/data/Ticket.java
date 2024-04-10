package com.github.mat_kubiak.tqs.bus_connector.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;

@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date date;

    @NotBlank
    @Size(max = 100)
    private String firstName;

    @NotBlank
    @Size(max = 100)
    private String lastName;

    public Ticket() {

    }

    public Ticket(Trip trip, Date date, String firstName, String lastName) {
        this.trip = trip;
        this.date = date;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Ticket(Long id, Trip trip, Date date, String firstName, String lastName) {
        this.ticketId = id;
        this.trip = trip;
        this.date = date;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
