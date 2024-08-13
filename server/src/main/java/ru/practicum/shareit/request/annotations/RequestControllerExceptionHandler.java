package ru.practicum.shareit.request.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация, показывающая RequestControllerHandler
 * с какого контроллера перехватывать ошибки
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestControllerExceptionHandler {

}