package com.bidflare.backend.repository;

import com.bidflare.backend.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AddressRepository extends JpaRepository<Address, UUID> {
    List<Address> findByUserId(UUID userId);
    Optional<Address> findByIdAndUserId(UUID addressId, UUID userId);
    Optional<Address> findByUserIdAndIsDefaultTrue(UUID userId);

    @Modifying
    @Query("UPDATE Address a SET a.isDefault = false WHERE a.user.id = :userId AND a.id <> :newDefaultAddressId")
    void unsetOtherDefaultAddresses(UUID userId, UUID newDefaultAddressId);
}
