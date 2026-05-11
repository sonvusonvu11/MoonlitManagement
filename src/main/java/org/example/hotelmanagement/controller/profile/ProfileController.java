package org.example.hotelmanagement.controller.profile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.hotelmanagement.dto.auth.UpdateProfileRequest;
import org.example.hotelmanagement.entity.Guest;
import org.example.hotelmanagement.entity.User;
import org.example.hotelmanagement.service.user.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    @GetMapping
    public String profilePage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy người dùng"));
        Guest guest = user.getGuest();

        UpdateProfileRequest request = UpdateProfileRequest.builder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(guest != null ? guest.getAddress() : null)
                .dateOfBirth(guest != null ? guest.getDateOfBirth() : null)
                .build();

        model.addAttribute("user", user);
        if (!model.containsAttribute("profileRequest")) {
            model.addAttribute("profileRequest", request);
        }
        return "profile/index";
    }

    @PostMapping("/update")
    public String updateProfile(@Valid @ModelAttribute("profileRequest") UpdateProfileRequest request,
                                BindingResult bindingResult,
                                @AuthenticationPrincipal UserDetails userDetails,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            User user = userService.findByUsername(userDetails.getUsername()).orElse(null);
            model.addAttribute("user", user);
            return "profile/index";
        }
        try {
            userService.updateProfile(userDetails.getUsername(), request);
            redirectAttributes.addFlashAttribute("success", "Cập nhật hồ sơ thành công");
        } catch (RuntimeException ex) {
            User user = userService.findByUsername(userDetails.getUsername()).orElse(null);
            model.addAttribute("user", user);
            bindingResult.reject("profileError", ex.getMessage());
            return "profile/index";
        }
        return "redirect:/profile";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 RedirectAttributes redirectAttributes) {
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu xác nhận không khớp");
            return "redirect:/profile";
        }
        try {
            userService.changePassword(userDetails.getUsername(), oldPassword, newPassword);
            redirectAttributes.addFlashAttribute("success", "Đổi mật khẩu thành công");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/profile";
    }
}
