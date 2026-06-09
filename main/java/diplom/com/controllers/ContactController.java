package diplom.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import diplom.com.domain.User;
import diplom.com.repos.UserRepos;


@Controller
public class ContactController {

    @Autowired
    private UserRepos userRepos;

    @GetMapping("/contact")
    public String contactMain(
            @RequestParam(required = false, defaultValue = "") String filter,
            Model model,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        model.addAttribute("title", "Контакты");

        Page<User> page;
        if (filter != null && !filter.isEmpty()) {
            page = userRepos.findByActiveTrueAndUsernameContainingIgnoreCase(filter, pageable);
        } else {
            page = userRepos.findByActiveTrue(pageable);
        }

        model.addAttribute("page", page);
        model.addAttribute("url", "/contact");
        model.addAttribute("filter", filter);

        return "contact";
    }
}
