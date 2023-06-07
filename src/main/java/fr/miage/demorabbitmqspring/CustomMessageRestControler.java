package fr.miage.demorabbitmqspring;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/CustomMessage")
public class CustomMessageRestControler {

    private final Sender sender;

    public CustomMessageRestControler(Sender sender) {
        this.sender = sender;
    }

    @PostMapping
    public CustomMessage createCustomMessage(@RequestBody CustomMessage customMessage) {
        System.out.println("Post de "+customMessage);
        sender.sendMessage(customMessage, "foo.bar.baz", "baz");
        System.out.println("Sent...");
        return customMessage;
    }
}

