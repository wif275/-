package diplom.com.controllers;

import diplom.com.domain.Product;
import diplom.com.repos.ProductRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CalculatorController {

    @Autowired
    private ProductRepos productRepos;

    @GetMapping("/calculator/{id}")
    public String calculator(@PathVariable(value = "id") Long id, Model model) {
        Product product = productRepos.findById(id).orElse(null);

        if (product == null) {
            return "redirect:/main";
        }

        model.addAttribute("id", product.getId());
        model.addAttribute("tag", product.getTag());
        model.addAttribute("text", product.getText());
        model.addAttribute("price", product.getPrice());
        model.addAttribute("title", "Калькулятор");

        return "calculator";
    }

    @PostMapping("/calculator/{id}")
    public String calculate(
            @PathVariable(value = "id") Long id,
            @RequestParam Double area,
            Model model) {

        Product product = productRepos.findById(id).orElse(null);

        if (product == null) {
            return "redirect:/main";
        }

        double total = 0.0;
        if (area != null && area > 0 && product.getPrice() != null) {
            total = product.getPrice() * area;
        }

        model.addAttribute("id", product.getId());
        model.addAttribute("tag", product.getTag());
        model.addAttribute("text", product.getText());
        model.addAttribute("price", product.getPrice());
        model.addAttribute("area", area);
        model.addAttribute("total", total);
        model.addAttribute("title", "Калькулятор");

        return "calculator";
    }
}
