package com.github.mat_kubiak.tqs.bus_connector.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    public List<Ticket> findAllByTripAndDate(Trip trip, Date date);
}
