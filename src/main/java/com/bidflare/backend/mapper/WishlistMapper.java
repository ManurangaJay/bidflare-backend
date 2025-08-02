package com.bidflare.backend.mapper;

import com.bidflare.backend.dto.wishlist.WishlistDTO;
import com.bidflare.backend.entity.WishlistItem;
import org.springframework.stereotype.Component;

@Component
public class WishlistMapper {

    public WishlistDTO.WishlistResponse toResponse(WishlistItem entity) {
        return new WishlistDTO.WishlistResponse(
                entity.getId(),
                entity.getProductId(),
                entity.getCreatedAt()
        );
    }
}
