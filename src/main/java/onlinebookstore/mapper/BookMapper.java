package onlinebookstore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import onlinebookstore.config.MapperConfig;
import onlinebookstore.dto.book.BookDto;
import onlinebookstore.dto.book.CreateBookRequestDto;
import onlinebookstore.model.Book;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Book toModel(CreateBookRequestDto requestDto);
}
