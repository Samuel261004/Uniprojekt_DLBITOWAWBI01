package com.example.demo.controller;

import com.example.demo.PostForm;
import com.example.demo.model.answer;
import com.example.demo.model.post;
import com.example.demo.model.user;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserVerificationRepository;

import jakarta.servlet.http.HttpServletRequest;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.demo.repository.answerRepository;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Controller
public class HomeController {

    @Autowired
    private answerRepository answerRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserVerificationRepository verificationRepository;

    private final AuthenticationManager authenticationManager;

    public HomeController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        System.out.println("start POST login");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("redirect to dashboard");
                return "redirect:/dashboard";
    }

    @GetMapping("/login")
    public String login() {
        System.out.println("Login");
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(defaultValue = "0") int page, Model model, Authentication authentication) {
        int pageSize = 20;

        Page<post> postsPage = postRepository.findAll(
            PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "date"))
        );

        model.addAttribute("posts", postsPage.getContent());
        model.addAttribute("totalPages", postsPage.getTotalPages());
        model.addAttribute("currentPage", page);

        user currentUser = userRepository.findByName(authentication.getName());
        model.addAttribute("currentUser", currentUser);

        return "dashboard";
    }


    @GetMapping("/myPosts")
    public String myPosts(@RequestParam(defaultValue = "0") int page, Model model, Authentication authentication) {
    	int pageSize = 20;
        String currentUsername = authentication.getName();
        user currentUser = userRepository.findByName(currentUsername);
        Long id= currentUser.getUSERID();
        Page<post> userPosts;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "date"));
        if (currentUser.isMod()) {
            userPosts = postRepository.findAll(pageable);
        }
        else {
            userPosts = postRepository.findByUserID(id, pageable);
        }

        model.addAttribute("userPosts", userPosts.getContent());
        model.addAttribute("totalPages", userPosts.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("currentUser", currentUser);
        return "myPosts";
    }
    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable("id") int id) {
        List<answer> answer = answerRepository.findAllByPostid(id);
        post postToDelete = postRepository.findById(id).orElse(null);


        if (postToDelete != null) {
            if (!answer.isEmpty()) {
                answerRepository.deleteAll(answer);
            }
            postRepository.deleteById(id);
        }

        return "redirect:/myPosts";
    }


    @GetMapping("/write")
    public String write(Authentication authentication, Model model) {
        user currentUser = userRepository.findByName(authentication.getName());
        model.addAttribute("currentUser", currentUser);
        System.out.println("Write");
        return "write";
    }

    @PostMapping("/write")
    public String postWrite(@ModelAttribute PostForm form, Authentication authentication, Model model) {
        user currentUser = userRepository.findByName(authentication.getName());

        if (form.getTitle().length()>255){
            model.addAttribute("TitleError", "Titel ist zu lang. Fassen sie sich kürzer");
            return "write";
        }
        if (form.getContent().length()>3000){
            model.addAttribute("ContentError", "Inhalt ist zu lang. Fassen sie sich kürzer");
            return "write";
        }

        Parser parser = Parser.builder().build();
        HtmlRenderer renderer = HtmlRenderer.builder().build();

        String markdown = form.getContent();
        String html = renderer.render(parser.parse(markdown));



        post newPost = new post();
        newPost.setTitle(form.getTitle());
        String safeHtml = Jsoup.clean(html, Safelist.relaxed());
        newPost.setContent(safeHtml);
        newPost.setUserID(currentUser.getUSERID());
        newPost.setDate(LocalDateTime.now());

        postRepository.save(newPost);
        return "redirect:/dashboard";
    }

    @GetMapping("/post/{id}")
    public String postDetails(@PathVariable int id,
                              @RequestParam(defaultValue = "0") int page,
                              Model model,
                              Authentication authentication) {

        post specificPost = postRepository.findById(id).orElse(null);

        int pageSize = 5;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "id"));

        Page<answer> answerPage = answerRepository.findByPostid(id, pageable);
        
        List<answer> answerList = answerPage.getContent();

        long cnt = answerRepository.countByPostid(id);
        System.out.println("Anzahl Antworten für post " + id + ": " + cnt);

        
        for (answer ans : answerList) {
            user u = userRepository.findById(ans.getUserid()).orElse(null);
            ans.setUser(u);
        }

        user currentUser = userRepository.findByName(authentication.getName());

        model.addAttribute("post", specificPost);
        model.addAttribute("answerList", answerList);
        model.addAttribute("totalAnswerPages", answerPage.getTotalPages());
        model.addAttribute("currentAnswerPage", page);
        model.addAttribute("currentUser", currentUser);

        return "post";
    }


    @PostMapping("/post/{id}")
    public String answerPost(@PathVariable int id, @ModelAttribute post post, Authentication authentication, @RequestParam String content) {
        user currentUser = userRepository.findByName(authentication.getName());
        answer newAnswer= new answer();
        newAnswer.setPostid(id);
        newAnswer.setUserid(currentUser.getUSERID());
        newAnswer.setContent(content);
        answerRepository.save(newAnswer);
        int pageSize = 5; 
        long answerCount = answerRepository.countByPostid(id);
        System.out.println("Anzahl Antworten für post " + id + ": " + answerCount);
        int lastPage = (int) (answerCount - 1) / pageSize;

        return "redirect:/post/" + id + "?page=" + lastPage;
    }

    @PostMapping("/post/{postId}/answer/{answerId}/delete")
    public String deleteAnswer(@PathVariable int postId,
                               @PathVariable int answerId,
                               Authentication authentication) {
    	System.out.println("Delete-Methode wurde aufgerufen: postId=" + postId + ", answerId=" + answerId);
        answer answerToDelete = answerRepository.findById(answerId).orElse(null);
        if (answerToDelete == null) {
            return "redirect:/post/" + postId;
        }

        user currentUser = userRepository.findByName(authentication.getName());
        
        System.out.println("Antwort wurde erstellt von UserID: " + answerToDelete.getUserid());
        System.out.println("Aktueller Benutzer hat UserID: " + currentUser.getUSERID());
        System.out.println("Gleich? " + (answerToDelete.getUserid() == currentUser.getUSERID()));
        
        if ((answerToDelete.getUser() != null && answerToDelete.getUser().getUSERID().equals(currentUser.getUSERID()))
        	    || currentUser.isMod()) {
        	    answerRepository.deleteById(answerId);
        	}

        return "redirect:/post/" + postId;
    }



    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model, Authentication authentication) {
        post specificPost = postRepository.findById(id).orElse(null);
        user currentUser = userRepository.findByName(authentication.getName());
        if (specificPost != null) {
            model.addAttribute("post", specificPost);
            model.addAttribute("currentUser", currentUser);
        }
        return "edit";
    }

    @PostMapping("/edit/{id}")
    public String editPost(@ModelAttribute post post, Model model, @PathVariable int id, @RequestParam String title, @RequestParam String content) {
        post specificPost = postRepository.findById(id).orElse(null);
        if (specificPost != null) {
            if (title.length()>255){
                model.addAttribute("TitleError", "Titel ist zu lang. Fassen sie sich kürzer");
                model.addAttribute("post", specificPost);
                return "edit";
            }
            if (content.length()>3000){
                model.addAttribute("ContentError", "Inhalt ist zu lang. Fassen sie sich kürzer");
                model.addAttribute("post", specificPost);
                return "edit";
            }
            Parser parser = Parser.builder().build();
            HtmlRenderer renderer = HtmlRenderer.builder().build();

            String html = renderer.render(parser.parse(content));
            String safeHtml = Jsoup.clean(html, Safelist.relaxed());
            model.addAttribute("post", specificPost);
            specificPost.setTitle(title);
            specificPost.setContent(safeHtml);
            postRepository.save(specificPost);
        }
        return "redirect:/dashboard";
    }

    @GetMapping("/dAccount")
    public String delete(Model model, Authentication authentication) {
        user currentUser = userRepository.findByName(authentication.getName());
        model.addAttribute("currentUser", currentUser);
        System.out.println("delete");
        return "dAccount";
    }
    @Transactional
    @PostMapping("/dAccount")
    public String postDelete(Model model, Authentication authentication, @RequestParam String password, HttpServletRequest request) {
        user currentUser = userRepository.findByName(authentication.getName());
        if (currentUser == null) {
            return "redirect:/login"; 
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(password, currentUser.getPassword())) {
            if (answerRepository.existsByuserid(currentUser.getUSERID())) {
                answerRepository.deleteByuserid(currentUser.getUSERID());
            }
            List<post> userPosts=postRepository.findAllByUserID(currentUser.getUSERID());
            if (userPosts.size() > 0) {
                for (post p : userPosts) {
                    answerRepository.deleteByPostid(p.getPostID());
                }
                postRepository.deleteAll(userPosts);
            }
            String email= currentUser.getEmail();
            verificationRepository.deleteAllByEmail(email);

            userRepository.deleteById(currentUser.getUSERID());
            SecurityContextHolder.clearContext();
            request.getSession().invalidate();
            return "redirect:/dSuccess";
        }
        else {
            model.addAttribute("param.error", "Falsches Passwort");
            return "dAccount";
        }
    }
    @GetMapping("/dSuccess")
    public String deleteSuccess() {
        System.out.println("deleteSuccess");
        return "dSuccess";
    }
}

