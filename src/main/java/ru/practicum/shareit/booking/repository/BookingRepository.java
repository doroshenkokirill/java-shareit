package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    // Для бронирований по идентификатору бронирующего
    List<Booking> findByBookerId(int bookerId);

    List<Booking> findByBookerIdAndStartBeforeAndEndAfterOrderByStart(int bookerId, LocalDateTime start, LocalDateTime end);

    List<Booking> findByBookerIdAndEndBeforeOrderByStart(int bookerId, LocalDateTime now);

    List<Booking> findByBookerIdAndStartAfterOrderByStart(int bookerId, LocalDateTime now);

    List<Booking> findByBookerIdAndStatus(int bookerId, BookingStatus status);

    // Для предыдущего и следующего бронирования
    Optional<Booking> findFirstByItemIdAndStartAfterAndStatusNotOrderByStart(int itemId, LocalDateTime now, BookingStatus status);

    Optional<Booking> findFirstByBookerIdAndEndBeforeAndStatusNot(int bookerId, LocalDateTime now, BookingStatus status);

    Optional<Booking> findFirstByItemIdAndStartBeforeAndStatusNotOrderByStartDesc(int itemId, LocalDateTime now, BookingStatus status);

    // Для бронирований по идентификатору владельца вещи
    List<Booking> findByItemOwnerIdOrderByStartDesc(int ownerId);

    List<Booking> findByItemOwnerIdAndStartAfterOrderByStartDesc(int ownerId, LocalDateTime now);

    List<Booking> findByItemOwnerIdAndEndBeforeOrderByStartDesc(int ownerId, LocalDateTime now);

    List<Booking> findByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(int itemOwnerId, LocalDateTime start, LocalDateTime end);

    List<Booking> findByItemOwnerIdAndStatusOrderByStartDesc(int ownerId, BookingStatus status);
}
