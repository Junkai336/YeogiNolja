package com.example.member.reservItem;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservRepository extends JpaRepository<ReservItem, Long> {
}
