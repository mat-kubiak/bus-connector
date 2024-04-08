package com.github.mat_kubiak.tqs.bus_connector.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "cars")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long tripId;

    @NotBlank
    @Size(max = 100)
    private String name;

    public Trip() {

    }

    public Trip(String name) {
        this.name = name;
    }

    public Trip(Long tripId, String name) {
        this.tripId = tripId;
        this.name = name;
    }

    public void setTripId(Long id) {
        this.tripId = id;
    }

    public Long getTripId() {
        return tripId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
