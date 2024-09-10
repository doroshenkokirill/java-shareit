package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findAllByBookerId(int bookerId);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStart(int bookerId, LocalDateTime cur, LocalDateTime now);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStart(int bookerId, LocalDateTime now);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStart(int bookerId, LocalDateTime now);

    List<Booking> findAllByBookerIdAndStatus(int bookerId, BookingStatus status);

    List<Booking> findAllByItemOwnerIdOrderByStartDesc(int ownerId);

    List<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(int itemOwnerId, LocalDateTime start, LocalDateTime end);

    List<Booking> findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(int ownerId, LocalDateTime now);

    List<Booking> findAllByItemOwnerIdAndStartAfterOrderByStartDesc(int ownerId, LocalDateTime now);

    List<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(int ownerId, BookingStatus status);

    List<Booking> findAllByBookerIdAndItemId(int bookerId, int itemId);

    Optional<Booking> findFirstByItemIdAndStartBeforeAndStatusNotOrderByStartDesc(int itemId, LocalDateTime now, BookingStatus status);

    Optional<Booking> findFirstByItemIdAndStartAfterAndStatusNotOrderByStart(int itemId, LocalDateTime now, BookingStatus status);
}
