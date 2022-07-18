package com.project.ir.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.toIntExact;

@Component
public class ListUtilComponent {


    public <T> Page<T> getPage(Pageable pageable, List<T> list) {
        int total = list.size();
        int start = toIntExact(pageable.getOffset());
        int end = Math.min((start + pageable.getPageSize()), total);

        if (pageable.getOffset() >= total)
            return new PageImpl<>(new ArrayList<>(), pageable, total);

        return new PageImpl<>(list.subList(start, end), pageable, total);
    }

}
