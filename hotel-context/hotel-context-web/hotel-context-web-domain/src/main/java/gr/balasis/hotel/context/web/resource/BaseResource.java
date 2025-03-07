package gr.balasis.hotel.context.web.resource;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class BaseResource implements Serializable {
    protected Long id;
}
