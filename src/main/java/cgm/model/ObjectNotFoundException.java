package cgm.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@NoArgsConstructor
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "There is no such item in GrouMan")
public class ObjectNotFoundException extends RuntimeException {

    private Long itemId;

    private String itemType;

    public ObjectNotFoundException(Long itemId, String itemType) {

        super("Item " + itemType + " with ID " + itemId + "no found!");
        this.itemId = itemId;
        this.itemType = itemType;
    }
}

