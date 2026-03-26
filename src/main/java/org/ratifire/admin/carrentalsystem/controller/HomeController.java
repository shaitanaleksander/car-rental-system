package org.ratifire.admin.carrentalsystem.controller;

import lombok.RequiredArgsConstructor;
import org.ratifire.admin.carrentalsystem.dto.CarDto;
import org.ratifire.admin.carrentalsystem.dto.ReservationDto;
import org.ratifire.admin.carrentalsystem.enums.CarType;
import org.ratifire.admin.carrentalsystem.repository.UserRepository;
import org.ratifire.admin.carrentalsystem.service.CarService;
import org.ratifire.admin.carrentalsystem.service.ReservationService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private static final int CAR_LIST_LIMIT = 5;

    private final CarService carService;
    private final ReservationService reservationService;
    private final UserRepository userRepository;

    @GetMapping("/")
    public String home(@RequestParam(required = false) CarType type,
                       Authentication authentication,
                       Model model) {
        List<CarDto> cars;
        if (type != null) {
            cars = carService.getByCarType(type, CAR_LIST_LIMIT);
        } else {
            cars = carService.getAll(CAR_LIST_LIMIT);
        }

        Long userId = getUserId(authentication);
        List<ReservationDto> reservations = reservationService.getByUserId(userId);

        Map<Long, CarDto> carMap = reservations.stream()
                .map(ReservationDto::getCarId)
                .distinct()
                .map(carService::getById)
                .collect(Collectors.toMap(CarDto::getId, Function.identity()));

        model.addAttribute("cars", cars);
        model.addAttribute("reservations", reservations);
        model.addAttribute("carMap", carMap);
        model.addAttribute("selectedType", type);
        model.addAttribute("carTypes", CarType.values());

        return "index";
    }

    @PostMapping("/reserve")
    public String reserve(@RequestParam Long carId,
                          @RequestParam String startDateTime,
                          @RequestParam Integer days,
                          Authentication authentication,
                          RedirectAttributes redirectAttributes) {
        try {
            Long userId = getUserId(authentication);
            ReservationDto dto = ReservationDto.builder()
                    .userId(userId)
                    .carId(carId)
                    .startDateTime(LocalDateTime.parse(startDateTime))
                    .days(days)
                    .build();
            reservationService.create(dto);
            redirectAttributes.addFlashAttribute("success", "Car reserved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/";
    }

    @PostMapping("/reservations/{id}/delete")
    public String deleteReservation(@PathVariable Long id,
                                    RedirectAttributes redirectAttributes) {
        try {
            reservationService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Reservation cancelled.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/";
    }

    private Long getUserId(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow()
                .getId();
    }
}
