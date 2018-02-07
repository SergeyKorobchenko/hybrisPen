package com.bridgex.storefront.forms.validation;

import org.springframework.validation.Validator;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import de.hybris.platform.acceleratorstorefrontcommons.forms.SaveCartForm;

/**
 * @author Created by ekaterina.agievich@masterdata.ru on 2/7/2018.
 */
@Component("pentlandSaveCartFormValidator")
public class PentlandSaveCartFormValidator implements Validator {
  @Override
  public boolean supports(final Class<?> aClass)
  {
    return SaveCartForm.class.equals(aClass);
  }

  @Override
  public void validate(final Object object, final Errors errors)
  {
    final SaveCartForm saveCartForm = (SaveCartForm) object;
    final String name = saveCartForm.getName();
    final String description = saveCartForm.getDescription();

    if (StringUtils.isBlank(name))
    {
      errors.rejectValue("name", "basket.save.cart.validation.name.notBlank");
      return;
    }


    if (StringUtils.length(name) > 255)
    {
      errors.rejectValue("name", "basket.save.cart.validation.name.size");
      return;
    }

    if (StringUtils.length(description) > 255)
    {
      errors.rejectValue("description", "basket.save.cart.validation.description.size");
      return;
    }
  }
}
