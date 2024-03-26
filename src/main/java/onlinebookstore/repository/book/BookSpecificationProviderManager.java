package onlinebookstore.repository.book;

import lombok.RequiredArgsConstructor;
import onlinebookstore.model.Book;
import onlinebookstore.repository.SpecificationProvider;
import onlinebookstore.repository.SpecificationProviderManager;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class BookSpecificationProviderManager
        implements SpecificationProviderManager<Book> {

    private final List<SpecificationProvider<Book>> bookSpecificationProviders;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return bookSpecificationProviders.stream()
                .filter(b -> b.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "Can't find correct specification provider for key: " + key));
    }
}
