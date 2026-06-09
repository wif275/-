package diplom.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import diplom.com.domain.Review;
import diplom.com.domain.User;
import diplom.com.repos.ReviewRepos;

@Controller
public class ReviewController {

    @Autowired
    private ReviewRepos reviewRepos;

    @GetMapping("/reviews")
    public String reviews(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("reviews", reviewRepos.findAllByOrderByCreatedAtDesc());
        model.addAttribute("title", "Отзывы");


        boolean userHasReview = (user != null) && reviewRepos.existsByAuthor(user);
        model.addAttribute("userHasReview", userHasReview);

        return "reviews";
    }

    @PostMapping("/reviews")
    public String addReview(
            @AuthenticationPrincipal User user,
            @RequestParam String text,
            @RequestParam Integer rating,
            Model model) {


        if (user == null) {
            return "redirect:/login";
        }

        if (reviewRepos.existsByAuthor(user)) {
            return "redirect:/reviews";
        }

        if (text != null && !text.trim().isEmpty()
                && rating != null && rating >= 1 && rating <= 5) {

            Review review = new Review(text.trim(), rating, user);
            reviewRepos.save(review);
        }

        return "redirect:/reviews";
    }
}
