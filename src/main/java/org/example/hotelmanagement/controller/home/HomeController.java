package org.example.hotelmanagement.controller.home;

import lombok.RequiredArgsConstructor;
import org.example.hotelmanagement.service.room.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final RoomService roomService;

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("rooms", roomService.findAll());
        return "home/index";
    }
}
