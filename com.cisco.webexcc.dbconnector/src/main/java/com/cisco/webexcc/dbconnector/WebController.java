package com.cisco.webexcc.dbconnector;

import com.cisco.webexcc.dbconnector.service.EndpointTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.Map;

@Controller
public class WebController {

    @Autowired
    private EndpointTrackingService trackingService;

    @GetMapping("/")
    public String index(@AuthenticationPrincipal OAuth2User principal) {
        if (principal != null) {
            return "redirect:/home";
        }
        return "index";
    }

    @GetMapping("/home")
    public String home(Model model, @AuthenticationPrincipal OAuth2User principal) {
        if (principal != null) {
            model.addAttribute("name", principal.getAttribute("displayName"));
            model.addAttribute("email", principal.getAttribute("emails")); // Webex returns emails as a list usually, or just 'email' depending on scope
            model.addAttribute("attributes", principal.getAttributes());
            model.addAttribute("envStats", trackingService.getEnvironmentStats());
            model.addAttribute("detailedEnvStats", trackingService.getDetailedEnvironmentStats());
        }
        return "home";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @PostMapping("/admin/stats/reset/{env}")
    public String resetStats(@PathVariable String env) {
        trackingService.resetEnvironment(env);
        return "redirect:/home";
    }

    @PostMapping("/admin/stats/cleanup/{env}")
    public String cleanupStats(@PathVariable String env) {
        trackingService.cleanupEnvironment(env);
        return "redirect:/home";
    }

    @GetMapping("/api/session/remaining")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getSessionRemaining(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("authenticated", false));
        }

        int maxInactiveInterval = session.getMaxInactiveInterval();
        if (maxInactiveInterval <= 0) {
            return ResponseEntity.ok(Map.of(
                "authenticated", true,
                "remainingSeconds", -1,
                "warningThresholdSeconds", 120,
                "expiringSoon", false
            ));
        }

        long elapsedSeconds = (System.currentTimeMillis() - session.getLastAccessedTime()) / 1000L;
        long remainingSeconds = Math.max(0L, maxInactiveInterval - elapsedSeconds);

        return ResponseEntity.ok(Map.of(
            "authenticated", true,
            "remainingSeconds", remainingSeconds,
            "warningThresholdSeconds", 120,
            "expiringSoon", remainingSeconds <= 120
        ));
    }
}
