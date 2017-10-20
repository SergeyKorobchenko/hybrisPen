package com.bridgex.core.search.solrfacetsearch.provider.impl;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.europe1.enums.UserPriceGroup;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.provider.Qualifier;
import de.hybris.platform.solrfacetsearch.provider.impl.CurrencyQualifierProvider;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 10/19/2017.
 */
public class UserPriceGroupCurrencyQualifierProvider extends CurrencyQualifierProvider {

  private static final Logger LOG = Logger.getLogger(UserPriceGroupCurrencyQualifierProvider.class);

  private final Set<Class<?>> supportedTypes;

  private EnumerationService enumerationService;

  private SessionService sessionService;

  private ModelService modelService;

  public UserPriceGroupCurrencyQualifierProvider(){
    HashSet<Class<?>> types = new HashSet<>(super.getSupportedTypes());
    types.add(UserPriceGroup.class);
    this.supportedTypes = Collections.unmodifiableSet(types);
  }

  @Override
  public Set<Class<?>> getSupportedTypes() {
    return supportedTypes;
  }

  @Override
  public Collection<Qualifier> getAvailableQualifiers(FacetSearchConfig facetSearchConfig, IndexedType indexedType) {
    Objects.requireNonNull(facetSearchConfig, "facetSearchConfig is null");
    Objects.requireNonNull(indexedType, "indexedType is null");

    List<UserPriceGroup> userPriceGroups = new ArrayList<>(enumerationService.getEnumerationValues(UserPriceGroup.class));

    return getQualifiers(facetSearchConfig, indexedType, userPriceGroups);
  }

  List<Qualifier> getQualifiers(FacetSearchConfig facetSearchConfig, IndexedType indexedType, List<UserPriceGroup> userPriceGroups) {
    List<Qualifier> qualifiers = new ArrayList<>();
    Collection<Qualifier> currencyQualifiers = super.getAvailableQualifiers(facetSearchConfig, indexedType);

    for (Qualifier currencyQualifier : currencyQualifiers) {
      CurrencyModel currency = currencyQualifier.getValueForType(CurrencyModel.class);

      qualifiers.addAll(userPriceGroups.stream().map(userPriceGroup -> new UserPriceGroupCurrencyQualifier(userPriceGroup, currency)).collect(Collectors.toList()));

    }
    return Collections.unmodifiableList(qualifiers);
  }

  @Override
  public boolean canApply(IndexedProperty indexedProperty) {
    Objects.requireNonNull(indexedProperty, "indexedProperty is null");
    return true;
  }

  @Override
  public void applyQualifier(Qualifier qualifier) {
    Objects.requireNonNull(qualifier, "qualifier is null");

    super.applyQualifier(qualifier);

    UserPriceGroup userPriceGroup = qualifier.getValueForType(UserPriceGroup.class);

    sessionService.setAttribute(UserModel.EUROPE1PRICEFACTORY_UPG, userPriceGroup != null ? Collections.singletonList(modelService.getSource(userPriceGroup)) : null);

  }

  @Override
  public Qualifier getCurrentQualifier() {
    return null;
  }

  @Required
  public void setEnumerationService(EnumerationService enumerationService) {
    this.enumerationService = enumerationService;
  }

  @Required
  public void setSessionService(SessionService sessionService) {
    this.sessionService = sessionService;
  }

  @Required
  public void setModelService(ModelService modelService) {
    this.modelService = modelService;
  }

  public static class UserPriceGroupCurrencyQualifier extends CurrencyQualifierProvider.CurrencyQualifier {
    private final UserPriceGroup userPriceGroup;

    public UserPriceGroupCurrencyQualifier(UserPriceGroup priceGroup, CurrencyModel currency) {
      super(currency);

      this.userPriceGroup = priceGroup;
    }

    public CurrencyModel getCurrency() {
      return super.getCurrency();
    }

    public <U> U getValueForType(Class<U> type) {
      Objects.requireNonNull(type, "type is null");
      if(Objects.equals(type, CurrencyModel.class)) {
        return (U) this.getCurrency();
      } else if(Objects.equals(type, UserPriceGroup.class)){
        return (U) this.userPriceGroup;
      } else {
        throw new IllegalArgumentException("type not supported");
      }
    }

    public String toFieldQualifier() {
      return (this.userPriceGroup != null ? this.userPriceGroup.getCode().toLowerCase() + "_" : "") + this.getCurrency().getIsocode();
    }

    public boolean equals(Object obj) {
      if(this == obj) {
        return true;
      } else if(!(obj instanceof UserPriceGroupCurrencyQualifier)) {
        return false;
      } else {
        UserPriceGroupCurrencyQualifier otherQualifier = (UserPriceGroupCurrencyQualifier)obj;
        return this.getCurrency().equals(otherQualifier.getCurrency()) && Objects.equals(userPriceGroup, otherQualifier.userPriceGroup);
      }
    }

    public int hashCode() {
      return this.userPriceGroup.hashCode();
    }
  }
}
