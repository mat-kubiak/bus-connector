package com.github.mat_kubiak.tqs.bus_connector.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.sql.Time;

@Entity
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long tripId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "source_city_id")
    private City sourceCity;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "destination_city_id")
    private City destinationCity;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private Weekday weekday;

    @NotNull
    @Temporal(TemporalType.TIME)
    private Time departureTime;

    @NotNull
    @Temporal(TemporalType.TIME)
    private Time arrivalTime;

    @NotNull
    private Integer priceEuro;

    @NotNull
    private Integer seatsTotal;

    @Transient
    private Integer seatsAvailable;

    public Trip() {

    }

    public Trip(City source, City dest, Weekday day, Time departureTime, Time arrivalTime, Integer price, Integer seatsTotal) {
        this.sourceCity = source;
        this.destinationCity = dest;
        this.weekday = day;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.priceEuro = price;
        this.seatsTotal = seatsTotal;
    }

    public Trip(Long id, City source, City dest, Weekday day, Time departureTime, Time arrivalTime, Integer price, Integer seatsTotal) {
        this.tripId = id;
        this.sourceCity = source;
        this.destinationCity = dest;
        this.weekday = day;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.priceEuro = price;
        this.seatsTotal = seatsTotal;
    }

    public Long getTripId() {
        return tripId;
    }

    public City getSourceCity() {
        return sourceCity;
    }

    public void setSourceCity(City sourceCity) {
        this.sourceCity = sourceCity;
    }

    public City getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(City destinationCity) {
        this.destinationCity = destinationCity;
    }

    public Weekday getWeekday() {
        return weekday;
    }

    public void setWeekday(Weekday weekday) {
        this.weekday = weekday;
    }

    public Time getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Time time) {
        this.departureTime = time;
    }

    public Integer getPriceEuro() {
        return priceEuro;
    }

    public void setPriceEuro(Integer priceEuro) {
        this.priceEuro = priceEuro;
    }

    public Integer getSeatsTotal() {
        return seatsTotal;
    }

    public void setSeatsTotal(Integer seatsFull) {
        this.seatsTotal = seatsFull;
    }

    public Integer getSeatsAvailable() {
        return seatsAvailable;
    }

    public void setSeatsAvailable(Integer seatsAvailable) {
        this.seatsAvailable = seatsAvailable;
    }

    public Time getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Time arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
}
