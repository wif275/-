package diplom.com.controllers;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import diplom.com.domain.News;
import diplom.com.repos.NewsRepos;

@Controller
public class NewsController {

    @Autowired
    private NewsRepos newsRepos;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/news")
    public String newsMain(
            Model model,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<News> page = newsRepos.findAll(pageable);
        model.addAttribute("title", "Новости");
        model.addAttribute("page", page);
        model.addAttribute("url", "/news");
        return "news";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/news/add")
    public String addNews(
            @RequestParam String text,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        News news = new News(text);

        saveFile(news, file);

        newsRepos.save(news);
        return "redirect:/news";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/news/edit")
    public String editNewsForm(@RequestParam("id") Long id, Model model) {
        Optional<News> opt = newsRepos.findById(id);
        if (!opt.isPresent()) {
            return "redirect:/news";
        }
        News news = opt.get();
        model.addAttribute("id", news.getId());
        model.addAttribute("text", news.getText());
        model.addAttribute("filename", news.getFilename());
        model.addAttribute("title", "Редактирование новости");
        return "newsEdit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/news/edit")
    public String updateNews(
            @RequestParam("id") Long id,
            @RequestParam("text") String text,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        Optional<News> opt = newsRepos.findById(id);
        if (!opt.isPresent()) {
            return "redirect:/news";
        }

        News news = opt.get();
        news.setText(text);

        if (file != null && !file.getOriginalFilename().isEmpty()) {
            if (news.getFilename() != null && !news.getFilename().isEmpty()) {
                File old = new File(uploadPath + "/" + news.getFilename());
                if (old.exists()) {
                    old.delete();
                }
            }
            saveFile(news, file);
        }

        newsRepos.save(news);
        return "redirect:/news";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/news/delete")
    public String deleteNews(@RequestParam("id") Long id) {
        Optional<News> opt = newsRepos.findById(id);
        if (!opt.isPresent()) {
            return "redirect:/news";
        }

        News news = opt.get();

        if (news.getFilename() != null && !news.getFilename().isEmpty()) {
            File imageFile = new File(uploadPath + "/" + news.getFilename());
            if (imageFile.exists()) {
                imageFile.delete();
            }
        }

        newsRepos.delete(news);
        return "redirect:/news";
    }

    private void saveFile(News news, MultipartFile file) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFilename));

            news.setFilename(resultFilename);
        }
    }
}


Мюллер, Р.Д. Проектирование баз данных и UML / Р.Д. Мюллер; Пер.,
с англ. Е.Н. Молодцова. - М.: Лори, 2013. - 420 c.
