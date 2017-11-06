package com.bridgex.facades.order.ordergridform.impl;

import java.util.*;

import org.apache.commons.collections.CollectionUtils;

import de.hybris.platform.acceleratorfacades.ordergridform.impl.DefaultOrderGridFormFacade;
import de.hybris.platform.acceleratorfacades.product.data.LeafDimensionData;
import de.hybris.platform.acceleratorfacades.product.data.ReadOnlyOrderGridData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.BaseOptionData;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.VariantOptionQualifierData;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 11/3/2017.
 */
public class PentlandOrderGridFormFacade extends DefaultOrderGridFormFacade{

  public static final String SIZE_VARIANT = "ApparelSizeVariantProduct";

  @Override
  public Map<String, ReadOnlyOrderGridData> getReadOnlyOrderGrid(final List<OrderEntryData> orderEntryDataList) {
    final Map<String, ReadOnlyOrderGridData> readOnlyMultiDMap = new LinkedHashMap<>();

    for (final OrderEntryData dimensionEntry : orderEntryDataList) {

      final Optional<AbstractMap.SimpleImmutableEntry<String, ReadOnlyOrderGridData>> readOnlyMultiDMapEntry = populateReadonlyGridMapFromVariants(dimensionEntry, readOnlyMultiDMap);

      if (readOnlyMultiDMapEntry.isPresent()) {
        final AbstractMap.SimpleImmutableEntry<String, ReadOnlyOrderGridData> entry = readOnlyMultiDMapEntry.get();
        readOnlyMultiDMap.put(entry.getKey(), entry.getValue());
      }
    }

    return readOnlyMultiDMap;
  }

  protected Optional<AbstractMap.SimpleImmutableEntry<String, ReadOnlyOrderGridData>> populateReadonlyGridMapFromVariants(final OrderEntryData dimensionEntry,
                                                                                                                          final Map<String, ReadOnlyOrderGridData> readOnlyOrderGridDataMap) {
    final Map<String, String> dimensionHeaderMap = new LinkedHashMap<>();
    final LeafDimensionData leafDimensionData = new LeafDimensionData();
    final StringBuilder hashKey = new StringBuilder();
    Optional<AbstractMap.SimpleImmutableEntry<String, ReadOnlyOrderGridData>> entry = Optional.empty();

    List<BaseOptionData> baseOptions = dimensionEntry.getProduct().getBaseOptions();
    if(CollectionUtils.isNotEmpty(baseOptions)){
      BaseOptionData sizeLevelOptions = baseOptions.stream().filter(option -> SIZE_VARIANT.equals(option.getVariantType())).findFirst().orElse(null);
      if(sizeLevelOptions != null && sizeLevelOptions.getSelected() != null){
        Collection<VariantOptionQualifierData> variantOptionQualifiers = sizeLevelOptions.getSelected().getVariantOptionQualifiers();
        for(VariantOptionQualifierData qualifier: variantOptionQualifiers){
          if ("style".equals(qualifier.getQualifier())) {
            dimensionHeaderMap.put(qualifier.getName(), qualifier.getValue());
            hashKey.append(qualifier.getValue());
          }else {
            populateLeafDimensionData(qualifier, dimensionEntry, leafDimensionData, qualifier.hashCode());
          }
        }
      }
    }

    final Optional<ReadOnlyOrderGridData> readOnlyOrderGridDataOptional = groupOrderEntriesWithCategory(hashKey.toString(),
                                                                                                        dimensionEntry, leafDimensionData, dimensionHeaderMap, readOnlyOrderGridDataMap);
    if (readOnlyOrderGridDataOptional.isPresent()) {
      entry = Optional.of(new AbstractMap.SimpleImmutableEntry<>(hashKey.toString(), readOnlyOrderGridDataOptional.get()));
    }

    return entry;
  }

  protected void populateLeafDimensionData(final VariantOptionQualifierData qualifier, final OrderEntryData dimensionEntry,
                                           final LeafDimensionData leafDimensionData, Integer sequence) {
    leafDimensionData.setLeafDimensionHeader(qualifier.getName());
    leafDimensionData.setLeafDimensionValue(qualifier.getValue());
    leafDimensionData.setLeafDimensionData(dimensionEntry.getQuantity().toString());
    leafDimensionData.setPrice(dimensionEntry.getTotalPrice());
    leafDimensionData.setSequence(sequence);
  }

}
