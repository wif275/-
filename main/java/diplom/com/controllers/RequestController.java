package diplom.com.controllers;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

import diplom.com.domain.Request;
import diplom.com.domain.User;
import diplom.com.repos.RequestRepos;

@Controller
public class RequestController {
    @Autowired
    private RequestRepos requestRepos;

    @GetMapping("/request")
    public String requestMain(@RequestParam(required = false, defaultValue = "false") Boolean filter,
                              Model model,
                              @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        model.addAttribute("title", "Заявки");
        Page<Request> page;
        if (filter != null && filter == true) {
            page = requestRepos.findByFinishedFalse(pageable);
        } else {
            page = requestRepos.findAll(pageable);
        }
        model.addAttribute("page", page);
        model.addAttribute("url", "/request");
        model.addAttribute("filter", filter);
        return "ReqRecieved";
    }

    @PostMapping("/request")
    public String requestFinished(@RequestParam("id") Long id,
                                  @RequestParam(required = false, defaultValue = "false") Boolean filter) {
        Optional<Request> requestOpt = requestRepos.findById(id);
        if (requestOpt.isPresent()) {
            Request request = requestOpt.get();
            request.setFinished(true);
            requestRepos.save(request);
        }
        return "redirect:/request?filter=" + filter;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/request/delete")
    public String requestDelete(@RequestParam("id") Long id,
                                @RequestParam(required = false, defaultValue = "false") Boolean filter) {
        Optional<Request> requestOpt = requestRepos.findById(id);
        if (requestOpt.isPresent()) {
            requestRepos.delete(requestOpt.get());
        }
        return "redirect:/request?filter=" + filter;
    }

    @GetMapping("/request.send")
    public String SendRequest(@AuthenticationPrincipal User user, Model model) {
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("username", user.getUsername());
        model.addAttribute("number", user.getNumber());
        return "ReqSender";
    }

    @PostMapping("/request.send")
    public String add(@AuthenticationPrincipal User user,
                      @RequestParam String textR,
                      Model model
    ) throws IOException {
        if (user == null) {
            return "redirect:/login";
        }

        if (textR != null && !textR.trim().isEmpty()) {
            Request request = new Request(user, textR.trim());
            requestRepos.save(request);
            model.addAttribute("message", "Заявка отправлена");
        }

        model.addAttribute("username", user.getUsername());
        model.addAttribute("number", user.getNumber());
        return "ReqSender";
    }
}
