package com.otpapp.otp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.otpapp.otp.Repository.NewsRepository;
import com.otpapp.otp.dto.NewsDto;
import com.otpapp.otp.model.News;

import java.time.LocalDate;
import java.util.List;

@Service
public class NewsService {
    @Autowired
    private NewsRepository newsRepository;

    // Existing saveNews method
    public void saveNews(NewsDto newsDto) {
        News news = new News();
        news.setTitle(newsDto.getTitle());
        news.setDescription(newsDto.getDescription());
        news.setPublishedDate(LocalDate.now()); // Save the current date and time
        newsRepository.save(news);
    }

    // New method to fetch all news
    public List<News> getAllNews() {
        return newsRepository.findAll(); // Fetch all news from the repository
    }
}
