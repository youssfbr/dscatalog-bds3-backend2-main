package com.devsuperior.dscatalog.services.validation;

import com.devsuperior.dscatalog.dto.UserUpdateDTO;
import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.repositories.UserRepository;
import com.devsuperior.dscatalog.resources.exceptions.FieldMessage;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserUpdateDTO> {

    private final UserRepository repository;
    private final HttpServletRequest request;

    public UserUpdateValidator(UserRepository repository , HttpServletRequest request) {
        this.repository = repository;
        this.request = request;
    }

    @Override
    public void initialize(UserUpdateValid ann) {
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean isValid(UserUpdateDTO dto, ConstraintValidatorContext context) {

        final var uriVars = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        final long userId = Long.parseLong(uriVars.get("id"));

        final List<FieldMessage> list = new ArrayList<>();

        // Coloque aqui seus testes de validação, acrescentando objetos FieldMessage à lista

        final User user = repository.findByEmail(dto.getEmail());
        if (user != null && userId != user.getId()) list.add(new FieldMessage("email", "e-mail já existe"));

        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }
        return list.isEmpty();
    }
}
