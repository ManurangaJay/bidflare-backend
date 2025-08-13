package com.bidflare.backend.mapper;

import com.bidflare.backend.dto.wishlist.WatchlistDTO;
import com.bidflare.backend.entity.WatchlistItem;
import org.springframework.stereotype.Component;

@Component
public class WatchlistMapper {

    public WatchlistDTO.WishlistResponse toResponse(WatchlistItem entity) {
        return new WatchlistDTO.WishlistResponse(
                entity.getId(),
                entity.getProductId(),
                entity.getCreatedAt()
        );
    }
}
