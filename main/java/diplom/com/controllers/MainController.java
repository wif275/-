package diplom.com.controllers;

import diplom.com.repos.NewsRepos;
import diplom.com.repos.ReviewRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

	@Autowired
	private ReviewRepos reviewRepos;

	@Autowired
	private NewsRepos newsRepos;

	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("title", "Добро Пожаловать");
		model.addAttribute("topReviews", reviewRepos.findTopRandomReviews());
		model.addAttribute("topNews", newsRepos.findTop4ByOrderByIdDesc());
		return "home";
	}

}
