package com.bridgex.storefront.controllers.cms;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bridgex.core.model.cms.PentlandCategoryNavigationComponentModel;
import com.bridgex.facades.category.CategoryNavigationFacade;
import com.bridgex.storefront.controllers.ControllerConstants;

@Controller("PentlandCategoryNavigationComponentController")
@RequestMapping(value = ControllerConstants.Actions.Cms.PentlandCategoryNavigationComponent)
public class PentlandCategoryNavigationComponentController extends AbstractAcceleratorCMSComponentController<PentlandCategoryNavigationComponentModel> {

  @Resource
  private CategoryNavigationFacade categoryNavigationFacade;

  @Override
  protected void fillModel(HttpServletRequest request, Model model, PentlandCategoryNavigationComponentModel component) {

    model.addAttribute("categoryNavigation", categoryNavigationFacade.getRootCategoryNavigation());
    Integer wrapAfter = component.getWrapAfter();
    model.addAttribute("wrapAfter", wrapAfter != null ? wrapAfter : 10);

  }

}
