package org.example.hotelmanagement.controller.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice(basePackages = {
        "org.example.hotelmanagement.controller.home",
        "org.example.hotelmanagement.controller.dashBoard",
        "org.example.hotelmanagement.controller.auth",
        "org.example.hotelmanagement.controller.booking",
        "org.example.hotelmanagement.controller.profile",
        "org.example.hotelmanagement.controller.room"
})
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException ex, Model model) {
        log.warn("Bad request: {}", ex.getMessage());
        model.addAttribute("status", 400);
        model.addAttribute("message", ex.getMessage());
        return "error/general";
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDenied(AccessDeniedException ex, Model model) {
        model.addAttribute("status", 403);
        model.addAttribute("message", "Bạn không có quyền truy cập tài nguyên này");
        return "error/general";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneric(Exception ex, Model model) {
        log.error("Unexpected error", ex);
        model.addAttribute("status", 500);
        model.addAttribute("message", ex.getMessage());
        return "error/general";
    }
}
