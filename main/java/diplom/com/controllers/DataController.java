
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import diplom.com.domain.Product;
import diplom.com.domain.User;
import diplom.com.repos.ProductRepos;


@Controller
public class DataController {
	@Autowired
	private ProductRepos productRepos;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/data")
	public String dataMain(@RequestParam(required=false, defaultValue="") String filter, 
    Model model,
    @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable

) {
		model.addAttribute("title", "Ведутся работы");
		Page<Product> page;
         if (filter != null && !filter.isEmpty()){
        page=productRepos.findByTagContainingIgnoreCase(filter, pageable);
        }
        else {
            page = productRepos.findAll( pageable);
        }
	model.addAttribute("page", page);
    model.addAttribute("url", "/data");
    model.addAttribute("filter", filter);
	return "data";
	}

@PostMapping("/data/add")
    public String add(
        @AuthenticationPrincipal User user,
        @RequestParam String text, 
        @RequestParam String tag, 
        @RequestParam Integer price, 
        Model model,
        @RequestParam("file")MultipartFile file)
        throws IOException
         {
        Product product = new Product(text, tag, user, price);

        if (file !=null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()){
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFilename));

            product.setFilename(resultFilename);
        }

        productRepos.save(product);
            Iterable<Product> products = productRepos.findAll();
        model.addAttribute("product]", products);
        return "redirect:/data";
    }

@GetMapping("/edit")
public String getProduct(@RequestParam("id") Long id, Model model) {
    Optional<Product> productOpt = productRepos.findById(id);
    if (!productOpt.isPresent()) {
        return "error"; 
    }

    Product product = productOpt.get();

    model.addAttribute("id", product.getId());
    model.addAttribute("text", product.getText());
    model.addAttribute("tag", product.getTag());
    model.addAttribute("price", product.getPrice());

    return "edit"; 
}

@PostMapping("/edit")
public String updateProduct(
        @RequestParam("id") Long id,
        @RequestParam("text") String text,
        @RequestParam("tag") String tag,
        @RequestParam("price") Integer price,
        @RequestParam("file") MultipartFile file
) throws IOException {
    Optional<Product> productOpt = productRepos.findById(id);
    if (!productOpt.isPresent()) {
        return "error";
    }

    Product product = productOpt.get();

    product.setText(text);
    product.setTag(tag);
    product.setPrice(price);
    
    if (file != null && !file.getOriginalFilename().isEmpty()) {
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        String uuidFile = UUID.randomUUID().toString();
        String resultFilename = uuidFile + "." + file.getOriginalFilename();
        file.transferTo(new File(uploadPath + "/" + resultFilename));

        product.setFilename(resultFilename);
    }

    productRepos.save(product);

    return "redirect:/data?page=0&size=30"; 
}

@PreAuthorize("hasAuthority('ADMIN')")
@PostMapping("/data/delete")
public String deleteProduct(@RequestParam("id") Long id) {
    Optional<Product> productOpt = productRepos.findById(id);
    if (!productOpt.isPresent()) {
        return "redirect:/data";
    }

    Product product = productOpt.get();

    // Удаляем файл картинки с диска, если он есть
    if (product.getFilename() != null && !product.getFilename().isEmpty()) {
        File imageFile = new File(uploadPath + "/" + product.getFilename());
        if (imageFile.exists()) {
            imageFile.delete();
        }
    }

    productRepos.delete(product);

    return "redirect:/data";
}
}
