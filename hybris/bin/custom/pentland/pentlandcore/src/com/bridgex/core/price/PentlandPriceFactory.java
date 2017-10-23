package com.bridgex.core.price;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.price.impl.DefaultPentlandPDTRowsQueryBuilder;

import de.hybris.platform.core.PK;
import de.hybris.platform.europe1.channel.strategies.RetrieveChannelStrategy;
import de.hybris.platform.europe1.constants.Europe1Constants;
import de.hybris.platform.europe1.constants.Europe1Tools;
import de.hybris.platform.europe1.enums.PriceRowChannel;
import de.hybris.platform.europe1.jalo.Europe1PriceFactory;
import de.hybris.platform.europe1.jalo.PriceRow;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.util.PriceValue;
import de.hybris.platform.util.localization.Localization;

/**
 * Created by Lenar on 10/13/2017.
 *
 * getProductPriceInformations method and all the subsequent methods chain was rewritten in order to use not only one user price group (upg)
 * which is out of the box Europe1PriceFactory functionality,
 * but the list of user price groups, because the project-specific requirement is for user to allow multiple price groups.
 */

@SuppressWarnings("deprecation")
public class PentlandPriceFactory extends Europe1PriceFactory {

  private static final Logger LOG = Logger.getLogger(PentlandPriceFactory.class);

  private RetrieveChannelStrategy retrieveChannelStrategy;

  @Required
  @Override
  public void setRetrieveChannelStrategy(RetrieveChannelStrategy retrieveChannelStrategy) {
    this.retrieveChannelStrategy = retrieveChannelStrategy;
  }

  public List<EnumerationValue> getUPGs(SessionContext ctx, User user) throws JaloPriceFactoryException {
    List<EnumerationValue> groups = this.getEnumsFromContextOrItem(ctx, user, "Europe1PriceFactory_UPG");
    if(groups == null){
      EnumerationValue userPriceGroup = this.getEnumFromGroups(user, "userPriceGroup");
      if(userPriceGroup != null){
        groups = Collections.singletonList(userPriceGroup);
      }
    }
    return groups;
  }

  @SuppressWarnings("unchecked")
  protected List<EnumerationValue> getEnumsFromContextOrItem(SessionContext ctx, ExtensibleItem item, String qualifier) {
    List<EnumerationValue> enums = (List<EnumerationValue>)(ctx != null?ctx.getAttribute(qualifier):null);
    if(enums == null && item != null) {
      EnumerationValue property = (EnumerationValue) item.getProperty(ctx, qualifier);
      if(property != null) {
        enums = Collections.singletonList(property);
      }
    }

    return enums;
  }

  public List getProductPriceInformations(SessionContext ctx, Product product, Date date, boolean net) throws JaloPriceFactoryException {
    List<EnumerationValue> upGs = this.getUPGs(ctx, ctx.getUser());
    if(CollectionUtils.isNotEmpty(upGs)) {
      return this.getPriceInformations(ctx, product, this.getPPG(ctx, product), ctx.getUser(), upGs, ctx.getCurrency(), net, date, null);
    }else{
      return Collections.EMPTY_LIST;
    }
  }

  protected List getPriceInformations(SessionContext ctx, Product product, EnumerationValue productGroup, User user, List<EnumerationValue> upgs, Currency curr, boolean net, Date date, Collection taxValues) throws JaloPriceFactoryException {
    List priceRows = this.filterPriceRows(this.matchPriceRowsForInfo(ctx, product, productGroup, user, upgs, curr, date, net));
    ArrayList priceInfos = new ArrayList(priceRows.size());
    Collection theTaxValues = taxValues;
    ArrayList defaultPriceInfos = new ArrayList(priceRows.size());
    PriceRowChannel channel = this.retrieveChannelStrategy.getChannel(ctx);
    Iterator var16 = priceRows.iterator();

    while(true) {
      while(var16.hasNext()) {
        PriceRow row = (PriceRow)var16.next();
        PriceInformation pInfo = Europe1Tools.createPriceInformation(row, curr);
        if(pInfo.getPriceValue().isNet() != net) {
          if(theTaxValues == null) {
            theTaxValues = Europe1Tools.getTaxValues(this.getTaxInformations(product, this.getPTG(ctx, product), user, this.getUTG(ctx, user), date));
          }

          pInfo = new PriceInformation(pInfo.getQualifiers(), pInfo.getPriceValue().getOtherPrice(theTaxValues));
        }

        if(row.getChannel() == null) {
          defaultPriceInfos.add(pInfo);
        }

        if(channel == null && row.getChannel() == null) {
          priceInfos.add(pInfo);
        } else if(channel != null && row.getChannel() != null && row.getChannel().getCode().equalsIgnoreCase(channel.getCode())) {
          priceInfos.add(pInfo);
        }
      }

      if(priceInfos.size() == 0) {
        return defaultPriceInfos;
      }

      return priceInfos;
    }
  }

  public List<PriceRow> matchPriceRowsForInfo(SessionContext ctx, Product product, EnumerationValue productGroup, User user, List<EnumerationValue> upgs, Currency currency, Date date, boolean net) throws JaloPriceFactoryException {
    if(product == null && productGroup == null) {
      throw new JaloPriceFactoryException("cannot match price info without product and product group - at least one must be present", 0);
    } else if(user == null && upgs == null) {
      throw new JaloPriceFactoryException("cannot match price info without user and user groups - at least one must be present", 0);
    } else if(currency == null) {
      throw new JaloPriceFactoryException("cannot match price info without currency", 0);
    } else if(date == null) {
      throw new JaloPriceFactoryException("cannot match price info without date", 0);
    } else {
      Collection rows = this.queryPriceRows4Price(ctx, product, productGroup, user, upgs);
      if(!rows.isEmpty()) {
        PriceRowChannel channel = this.retrieveChannelStrategy.getChannel(ctx);
        ArrayList ret = new ArrayList(rows);
        if(ret.size() > 1) {
          Collections.sort(ret, new PriceRowInfoComparator(currency, net));
        }

        return this.filterPriceRows4Info(ret, currency, date, channel);
      } else {
        return Collections.EMPTY_LIST;
      }
    }
  }

  protected Collection<PriceRow> queryPriceRows4Price(SessionContext ctx, Product product, EnumerationValue productGroup, User user, List<EnumerationValue> upgs) {
    PK productPk = product == null ? null : product.getPK();
    PK productGroupPk = productGroup == null?null:productGroup.getPK();
    PK userPk = user == null?null:user.getPK();
    List<PK> userGroupsPk = upgs == null?null:upgs.stream().map(Item::getPK).collect(Collectors.toList());
    String productId = this.extractProductId(ctx, product);
    PentlandPDTRowsQueryBuilder builder = getPDTRowsQueryBuilderFor(Europe1Constants.TC.PRICEROW);
    PentlandPDTRowsQueryBuilder.QueryWithParams queryAndParams = builder.withAnyProduct().withAnyUser().withProduct(productPk).withProductId(productId).withProductGroup(productGroupPk).withUser(userPk).withUserGroups(userGroupsPk).build();
    return FlexibleSearch.getInstance().search(ctx, queryAndParams.getQuery(), queryAndParams.getParams(), PriceRow.class).getResult();
  }

  @Override
  public PriceValue getBasePrice(AbstractOrderEntry entry) throws JaloPriceFactoryException {
    SessionContext ctx = this.getSession().getSessionContext();
    AbstractOrder order = entry.getOrder(ctx);
    Currency currency = null;
    EnumerationValue productGroup = null;
    User user = null;
    List<EnumerationValue> userGroups = null;
    Unit unit = null;
    long quantity = 0L;
    boolean net = false;
    Date date = null;
    Product product = entry.getProduct();
    boolean giveAwayMode = entry.isGiveAway(ctx).booleanValue();
    boolean entryIsRejected = entry.isRejected(ctx).booleanValue();
    PriceRow row;
    if(giveAwayMode && entryIsRejected) {
      row = null;
    } else {
      row = this.matchPriceRowForPrice(ctx, product, productGroup = this.getPPG(ctx, product), user = order.getUser(), userGroups = this.getUPGs(ctx, user), quantity = entry.getQuantity(ctx).longValue(), unit = entry.getUnit(ctx), currency = order.getCurrency(ctx), date = order.getDate(ctx), net = order.isNet().booleanValue(), giveAwayMode);
    }

    if(row != null) {
      Currency msg1 = row.getCurrency();
      double price;
      if(currency.equals(msg1)) {
        price = row.getPriceAsPrimitive() / (double)row.getUnitFactorAsPrimitive();
      } else {
        price = msg1.convert(currency, row.getPriceAsPrimitive() / (double)row.getUnitFactorAsPrimitive());
      }

      Unit priceUnit = row.getUnit();
      Unit entryUnit = entry.getUnit();
      double convertedPrice = priceUnit.convertExact(entryUnit, price);
      return new PriceValue(currency.getIsoCode(), convertedPrice, row.isNetAsPrimitive());
    } else if(giveAwayMode) {
      return new PriceValue(order.getCurrency(ctx).getIsoCode(), 0.0D, order.isNet().booleanValue());
    } else {
      String msg = Localization.getLocalizedString("exception.europe1pricefactory.getbaseprice.jalopricefactoryexception1", new Object[]{product, productGroup, user, userGroups, Long.toString(quantity), unit, currency, date, Boolean.toString(net)});
      throw new JaloPriceFactoryException(msg, 0);
    }
  }

  public PriceRow matchPriceRowForPrice(SessionContext ctx, Product product, EnumerationValue productGroup, User user, List<EnumerationValue> userGroups, long qtd, Unit unit, Currency currency, Date date, boolean net, boolean giveAwayMode) throws JaloPriceFactoryException {
    if(product == null && productGroup == null) {
      throw new JaloPriceFactoryException("cannot match price without product and product group - at least one must be present", 0);
    } else if(user == null && userGroups == null) {
      throw new JaloPriceFactoryException("cannot match price without user and user group - at least one must be present", 0);
    } else if(currency == null) {
      throw new JaloPriceFactoryException("cannot match price without currency", 0);
    } else if(date == null) {
      throw new JaloPriceFactoryException("cannot match price without date", 0);
    } else if(unit == null) {
      throw new JaloPriceFactoryException("cannot match price without unit", 0);
    } else {
      Collection rows = this.queryPriceRows4Price(ctx, product, productGroup, user, userGroups);
      if(!rows.isEmpty()) {
        PriceRowChannel channel = this.retrieveChannelStrategy.getChannel(ctx);
        List list = this.filterPriceRows4Price(rows, qtd, unit, currency, date, giveAwayMode, channel);
        if(list.isEmpty()) {
          return null;
        } else if(list.size() == 1) {
          return (PriceRow)list.get(0);
        } else {
          Collections.sort(list, new PentlandPriceFactory.PriceRowMatchComparator(currency, net, unit));
          return (PriceRow)list.get(0);
        }
      } else {
        return null;
      }
    }
  }

  protected PentlandPDTRowsQueryBuilder getPDTRowsQueryBuilderFor(String type) {
    return new DefaultPentlandPDTRowsQueryBuilder(type);
  }

  protected class PriceRowInfoComparator implements Comparator<PriceRow> {
    private final Currency curr;
    private final boolean net;

    protected PriceRowInfoComparator(Currency curr, boolean net) {
      this.curr = curr;
      this.net = net;
    }

    public int compare(PriceRow row1, PriceRow row2) {
      long u1Match = row1.getUnit().getPK().getLongValue();
      long u2Match = row2.getUnit().getPK().getLongValue();
      if(u1Match != u2Match) {
        return u1Match < u2Match?-1:1;
      } else {
        long min1 = row1.getMinqtdAsPrimitive();
        long min2 = row2.getMinqtdAsPrimitive();
        if(min1 != min2) {
          return min1 > min2?-1:1;
        } else {
          int matchValue1 = row1.getMatchValueAsPrimitive();
          int matchValue2 = row2.getMatchValueAsPrimitive();
          if(matchValue1 != matchValue2) {
            return matchValue2 - matchValue1;
          } else {
            boolean c1Match = this.curr.equals(row1.getCurrency());
            boolean c2Match = this.curr.equals(row2.getCurrency());
            if(c1Match != c2Match) {
              return c1Match?-1:1;
            } else {
              boolean n1Match = this.net == row1.isNetAsPrimitive();
              boolean n2Match = this.net == row2.isNetAsPrimitive();
              if(n1Match != n2Match) {
                return n1Match?-1:1;
              } else {
                boolean row1Range = row1.getStartTime() != null;
                boolean row2Range = row2.getStartTime() != null;
                return row1Range != row2Range?(row1Range?-1:1):row1.getPK().compareTo(row2.getPK());
              }
            }
          }
        }
      }
    }
  }

  protected class PriceRowMatchComparator implements Comparator<PriceRow> {
    private final Currency curr;
    private final boolean net;
    private final Unit unit;

    protected PriceRowMatchComparator(Currency curr, boolean net, Unit unit) {
      this.curr = curr;
      this.net = net;
      this.unit = unit;
    }

    public int compare(PriceRow row1, PriceRow row2) {
      int matchValue1 = row1.getMatchValueAsPrimitive();
      int matchValue2 = row2.getMatchValueAsPrimitive();
      if(matchValue1 != matchValue2) {
        return matchValue2 - matchValue1;
      } else {
        boolean c1Match = this.curr.equals(row1.getCurrency());
        boolean c2Match = this.curr.equals(row2.getCurrency());
        if(c1Match != c2Match) {
          return c1Match?-1:1;
        } else {
          boolean n1Match = this.net == row1.isNetAsPrimitive();
          boolean n2Match = this.net == row2.isNetAsPrimitive();
          if(n1Match != n2Match) {
            return n1Match?-1:1;
          } else {
            boolean u1Match = this.unit.equals(row1.getUnit());
            boolean u2Match = this.unit.equals(row2.getUnit());
            if(u1Match != u2Match) {
              return u1Match?-1:1;
            } else {
              long min1 = row1.getMinqtdAsPrimitive();
              long min2 = row2.getMinqtdAsPrimitive();
              if(min1 != min2) {
                return min1 > min2?-1:1;
              } else {
                boolean row1Range = row1.getStartTime() != null;
                boolean row2Range = row2.getStartTime() != null;
                if(row1Range != row2Range) {
                  return row1Range?-1:1;
                } else {
                  PentlandPriceFactory.LOG.warn("found ambigous price rows " + row1 + " and " + row2 + " - using PK to distinguish");
                  return row1.getPK().compareTo(row2.getPK());
                }
              }
            }
          }
        }
      }
    }
  }
}
