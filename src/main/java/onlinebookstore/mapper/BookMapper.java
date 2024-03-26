package onlinebookstore.mapper;

import onlinebookstore.dto.book.BookDto;
import onlinebookstore.dto.book.BookDtoWithoutCategoryIds;
import onlinebookstore.model.Category;
import onlinebookstore.config.MapperConfig;
import onlinebookstore.dto.book.CreateBookRequestDto;
import onlinebookstore.model.Book;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

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
