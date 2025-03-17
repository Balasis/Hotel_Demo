package gr.balasis.hotel.engine.core.validation;

public interface BaseValidator<T> {
   T validate(T type);
   T validateForUpdate(Long id,T type);
}
