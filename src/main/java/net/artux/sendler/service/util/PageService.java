package net.artux.sendler.service.util;

import net.artux.sendler.model.page.QueryPage;
import net.artux.sendler.model.page.ResponsePage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.function.Function;

@Service
public class PageService {

    public Pageable getPageable(QueryPage page) {
        return createPageableWithSort(page);
    }

    private Pageable createPageableWithSort(QueryPage page) {
        Sort sort = Sort.by(page.getSortDirection(), page.getSortBy());
        return PageRequest.of(subtractOne(page.getNumber()), page.getSize(), sort);
    }

    private int subtractOne(int pageNumber) {
        int result = pageNumber - 1;
        if (result < 0) {
            throw new RuntimeException("Номер страницы должен быть не меньше 1!");
        }
        return result;
    }

    // Основной маппинг всех данных для возврата результирующей страницы
    public <T, R> ResponsePage<R> mapDataPageToResponsePage(Page<T> dataPage, Function<T, R> mapFunc) {

        LinkedList<R> data = new LinkedList<>();
        for (T t : dataPage.getContent()) {
            data.add(mapFunc.apply(t));
        }

        return ResponsePage
                .<R>builder()
                .lastPage(dataPage.getTotalPages())
                .data(data)
                .dataSize(data.size())
                .queryDataSize(dataPage.getTotalElements())
                .number(dataPage.getNumber() + 1) // +1 так как отсчет идет от 0
                .size(dataPage.getSize())
                .sortBy(dataPage.getSort().stream().iterator().next().getProperty())
                .sortDirection(dataPage.getSort().stream().iterator().next().getDirection())
                .build();
    }
}
