package com.saus.bgt.service.common;

import com.saus.bgt.generated.types.PageInfo;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public interface BgtDataFetcher {
    String CURSOR_FIRST = "first";
    String CURSOR_AFTER = "after";

    default <T> PageInfo createPageInfo(Page<T> entities, int pageNum) {
        return PageInfo.newBuilder()
                .startCursor(cursorHelper().cursorFrom(entities.hasPrevious() ? pageNum - 1 : pageNum).getValue())
                .endCursor(cursorHelper().cursorFrom(entities.hasNext() ? pageNum + 1 : pageNum).getValue())
                .hasPreviousPage(entities.hasPrevious())
                .hasNextPage(entities.hasNext())
                .build();
    }

    default PageRequest createPageRequest(DataFetchingEnvironment dataFetchingEnvironment, int pageNum) {
        return PageRequest.of(pageNum, dataFetchingEnvironment.getArgumentOrDefault(CURSOR_FIRST, 10))
                .withSort(Sort.Direction.ASC, "name");
    }

    default int getOffsetFromDataFetchingEnvironment(DataFetchingEnvironment dataFetchingEnvironment) {
        if (dataFetchingEnvironment.containsArgument(CURSOR_AFTER)) {
            return cursorHelper().fromBase64String(dataFetchingEnvironment.getArgument(CURSOR_AFTER));
        }
        return 0;
    }

    CursorHelper cursorHelper();
}
