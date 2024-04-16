package com.bookStore.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bookStore.Entity.BookInfo;
import com.bookStore.Entity.User;
import com.bookStore.Repository.BookRepository;
import com.bookStore.Service.BookService;

@Controller
public class ScraperController {
	
	private BookService bookService;
	@Autowired
    private BookRepository bookRepository;
	
	@Autowired
	public ScraperController(BookService bookService) {
		this.bookService = bookService;
	}
	
	@GetMapping("/scrape")
	public String scrapeBookInfo(@RequestParam String bookTitle, Model model) {
	    try {
	        String searchQuery = URLEncoder.encode(bookTitle, "UTF-8");
	        String baseUrl = "https://www.barnesandnoble.com";
	        String searchUrl = baseUrl + "/s/" + searchQuery;
	        
	        Document searchResultsDocument = Jsoup.connect(searchUrl).get();

	        Element booksCategoryLink = searchResultsDocument.select(".bread-crumbs__item[href*=/N-]").first();
	        if (booksCategoryLink != null) {
	            String booksCategoryUrl = baseUrl + booksCategoryLink.attr("href");
	            searchResultsDocument = Jsoup.connect(booksCategoryUrl).get();
	        }

	        Elements bookElements = searchResultsDocument.select("#gridView");

	        List<BookInfo> bookInfoList = new ArrayList<>();

	        System.out.println("Found " + bookElements.size() + " book elements.");

	        if (bookElements.isEmpty()) {
	            System.out.println("No results found for: " + bookTitle);
	            model.addAttribute("errorMessage", "No results found for: " + bookTitle);
	            return "error";
	        }

	        for (Element bookElement : bookElements) {
	            String bookUrl = baseUrl + bookElement.select(".current a").attr("href");

	            try {
	                Document bookPageDocument = Jsoup.connect(bookUrl).get();

	                String name = bookElement.select(".product-shelf-title.product-info-title.pt-xs a").attr("title");

	                Elements priceElements = bookPageDocument.select(".price.current-price");
	                String price = "";
	                if (!priceElements.isEmpty() && priceElements.first() != null) {
	                    price = priceElements.first().text();
	                }

	                if (name.toLowerCase().contains(bookTitle.toLowerCase())) {
	                    System.out.println("Name: " + name);
	                    System.out.println("Price: " + (price.isEmpty() ? "No price" : price));
	                    System.out.println("URL: " + bookUrl);

	                    bookInfoList.add(new BookInfo(name, (price.isEmpty() ? "No price" : price), bookUrl));
	                }
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }

	        if (bookInfoList.isEmpty()) {
	            System.out.println("No matching books found for: " + bookTitle);
	            model.addAttribute("errorMessage", "No matching books found for: " + bookTitle);
	            return "error";
	        }

	        model.addAttribute("bookInfoList", bookInfoList);

	        return "shop";
	    } catch (IOException e) {
	        e.printStackTrace();
	        return "error";
	    }
	}
}
