package cgm.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
@NoArgsConstructor
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "There is no such item in GrouMan")
public class ItemNotFoundException extends RuntimeException {

    private Long itemId;

    public ItemNotFoundException(Long itemId) {

        super("Item with ID " + itemId + "no found!");

        this.itemId = itemId;
    }
}

