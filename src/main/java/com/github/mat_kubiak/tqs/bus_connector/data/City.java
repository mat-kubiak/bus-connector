package com.github.mat_kubiak.tqs.bus_connector.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "cities")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long cityId;

    @NotBlank
    @Size(max = 100)
    private String name;

    public City() {

    }

    public City(String name) {
        this.name = name;
    }

    public City(Long cityId, String name) {
        this.cityId = cityId;
        this.name = name;
    }

    public void setCityId(Long id) {
        this.cityId = id;
    }

    public Long getCityId() {
        return cityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
