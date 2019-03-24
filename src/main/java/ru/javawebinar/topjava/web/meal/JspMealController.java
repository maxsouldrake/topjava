package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.Util;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping("/meals")
public class JspMealController extends AbstractMealController {
    @GetMapping
    public String meals(Model model) {
        model.addAttribute("meals", super.getAll());
        return "meals";
    }

    @PostMapping("/filter")
    public String getAllFiltered(HttpServletRequest request, Model model) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        model.addAttribute("meals", super.getBetween(startDate, startTime, endDate, endTime));
        return "meals";
    }

    @GetMapping("/create")
    public String create(Model model) {
        LocalDateTime localDateTime = getCurrentTime();
        Meal meal = new Meal(localDateTime, getDescription(localDateTime), 1000);
        model.addAttribute("meal", meal);
        return "mealForm";
    }


    @GetMapping("/update")
    public String update(HttpServletRequest request, Model model) {
        Meal meal = super.get(getId(request));
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @PostMapping
    public String createUpdate(HttpServletRequest request) {
        Meal meal = new Meal(LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.valueOf(request.getParameter("calories")));
        if (request.getParameter("id").isEmpty()) {
            super.create(meal);
        } else {
            super.update(meal, getId(request));
        }
        return "redirect:/meals";
    }

    @GetMapping("/delete")
    public String delete(HttpServletRequest request) {
        super.delete(getId(request));
        return "redirect:/meals";
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

    private LocalDateTime getCurrentTime() {
        return LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    }

    private String getDescription(LocalDateTime localDateTime) {
        String description = "";
        if (Util.isBetween(localDateTime.toLocalTime(), LocalTime.of(3, 0), LocalTime.of(12,0))) {
            description = "Завтрак";
        }
        if (Util.isBetween(localDateTime.toLocalTime(), LocalTime.of(12, 0), LocalTime.of(17,0))) {
            description = "Обед";
        }
        if (Util.isBetween(localDateTime.toLocalTime(), LocalTime.of(17, 0), LocalTime.of(3,0))) {
            description = "Ужин";
        }
        return description;
    }
}
