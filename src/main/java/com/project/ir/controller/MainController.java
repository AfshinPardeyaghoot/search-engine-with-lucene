package com.project.ir.controller;

import com.project.ir.model.SearchResult;
import com.project.ir.util.ListUtilComponent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class MainController {

    private final ListUtilComponent listUtilComponent;

    public MainController(ListUtilComponent listUtilComponent) {
        this.listUtilComponent = listUtilComponent;
    }

    @RequestMapping
    public String getHomePage(Model model,
                              @PageableDefault(size = 5) Pageable pageable) {


        Page<SearchResult> searchResults = listUtilComponent.getPage(pageable, new ArrayList<>());
        int totalPages = searchResults.getTotalPages();

        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("results", searchResults);
        return "search";
    }

    @GetMapping("/static/search.css")
    public String createTradeRequestStyle() {
        return "../static/search.css";
    }

    @RequestMapping("/search")
    public String search(Model model,
                         @RequestParam String query,
                         @PageableDefault Pageable pageable) {


        List<SearchResult> searchResultList = new ArrayList<>();

        Page<SearchResult> searchResults = listUtilComponent.getPage(pageable, searchResultList);

        model.addAttribute("results", searchResults);

        int totalPages = searchResults.getTotalPages();

        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "search";
    }


}
