package onlinebookstore.mapper;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import onlinebookstore.config.MapperConfig;
import onlinebookstore.dto.book.BookDtoWithoutCategoryIds;
import onlinebookstore.dto.book.BookDto;
import onlinebookstore.dto.book.CreateBookRequestDto;
import onlinebookstore.model.Book;
import onlinebookstore.model.Category;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    @Mapping(target = "categories", ignore = true)
    Book toEntity(CreateBookRequestDto requestDto);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategories(@MappingTarget BookDto bookDto, Book book) {
        bookDto.setCategories(mapToIds(book.getCategories()));
    }

    default Set<Long> mapToIds(Set<Category> categories) {
        Set<Long> categoryIds;
        categoryIds = categories.stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
        return categoryIds;
    }

    @Named("bookFromId")
    default Book bookFromId(Long id) {
        return Optional.ofNullable(id)
                .map(Book::new)
                .orElse(null);
    }
}
