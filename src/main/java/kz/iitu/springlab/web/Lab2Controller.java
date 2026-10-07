package kz.iitu.springlab.web;

import kz.iitu.springlab.lifecycle.LifecycleDemo;
import kz.iitu.springlab.notify.NotificationService;
import kz.iitu.springlab.notify.Notifier;
import kz.iitu.springlab.scope.TicketOffice;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lab2")
public class Lab2Controller {

    // 1. Объявляем поля класса для всех сервисов и бинов
    private final NotificationService notifications;
    private final LifecycleDemo lifecycle;
    private final TicketOffice ticketOffice;
    private final Notifier upperNotifier; // Наш индивидуальный бин

    // 2. В Конструкторе принимаем все зависимости.
    // С помощью @Qualifier("upper") мы точно указываем Spring,
    // что сюда нужно внедрить именно UpperNotifier!
    public Lab2Controller(NotificationService notifications,
                          LifecycleDemo lifecycle,
                          TicketOffice ticketOffice,
                          @Qualifier("upper") Notifier upperNotifier) {
        this.notifications = notifications;
        this.lifecycle = lifecycle;
        this.ticketOffice = ticketOffice;
        this.upperNotifier = upperNotifier;
    }

    // Эндпоинт 1: Проверка базовых нотификаторов
    @GetMapping("/notify")
    public Map<String, Object> notify(@RequestParam(defaultValue = "Hello") String text) {
        return Map.of("primary",  notifications.viaPrimary(text),
                "console",  notifications.viaConsole(text),
                "all",      notifications.viaAll(text),
                "beanNames", notifications.names());
    }

    // Эндпоинт 2: Лог жизненного цикла
    @GetMapping("/lifecycle")
    public List<String> lifecycle() {
        return lifecycle.events();
    }

    // Эндпоинт 3: Демонстрация scopes (Singleton vs Prototype)
    @GetMapping("/scopes")
    public Map<String, Object> scopes() {
        return ticketOffice.demo();
    }

    // Эндпоинт 4: Твой индивидуальный эндпоинт (Вариант 1)
    @GetMapping("/custom")
    public String custom(@RequestParam(defaultValue = "hello world") String text) {
        return upperNotifier.send(text);
    }
}