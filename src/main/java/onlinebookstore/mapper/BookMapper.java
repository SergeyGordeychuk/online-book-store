package onlinebookstore.mapper;

import onlinebookstore.dto.book.BookDto;
import onlinebookstore.dto.book.BookDtoWithoutCategoryIds;
import onlinebookstore.model.Category;
import org.mapstruct.*;
import onlinebookstore.config.MapperConfig;
import onlinebookstore.dto.book.CreateBookRequestDto;
import onlinebookstore.model.Book;

@Mapper(config = MapperConfig.class)
public interface BookMapper {

    BookDto toDto(Book book);


    Book toModel(CreateBookRequestDto requestDto);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        bookDto.setCategoriesId(book.getCategories()
                .stream()
                .map(Category::getId)
                .toList());
    }
}
