package onlinebookstore.mapper;

import onlinebookstore.config.MapperConfig;
import onlinebookstore.dto.cartitem.CartItemRequestDto;
import onlinebookstore.dto.cartitem.CartItemResponseDto;
import onlinebookstore.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface CartItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    CartItemResponseDto toDto(CartItem cartItem);

    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookFromId")
    CartItem toEntity(CartItemRequestDto dto);
}
