package fr.miage.demorabbitmqspring;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/CustomMessage")
public class CustomMessageRestControler {

    private final Sender sender;

    public CustomMessageRestControler(Sender sender) {
        this.sender = sender;
    }

    @PostMapping
    public CustomMessage createCustomMessage(@RequestBody CustomMessage customMessage,
                                             @RequestParam(name = "routingKey", defaultValue = "foo.bar.baz") String routingKey,
                                             @RequestParam(name = "barHeader", defaultValue = "baz") String barHeader) {
        System.out.println("Post de "+customMessage);
        sender.sendMessage(customMessage, routingKey, barHeader);
        System.out.println("Sent...");
        return customMessage;
    }
}

