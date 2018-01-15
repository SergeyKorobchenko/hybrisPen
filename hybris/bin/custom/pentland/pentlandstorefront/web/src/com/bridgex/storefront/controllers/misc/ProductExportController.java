package com.bridgex.storefront.controllers.misc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import com.bridgex.facades.csv.PentlandCsvFacade;
import com.bridgex.facades.export.PentlandExportFacade;
import com.bridgex.facades.order.data.MiniOrderEntry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 11/14/2017.
 */
@Controller
@RequestMapping(value = "**/export")
public class ProductExportController extends AbstractPageController {

  @Resource
  private PentlandCsvFacade csvFacade;

  @Resource
  private PentlandExportFacade pentlandExportFacade;

  private ObjectMapper mapper = new ObjectMapper();

  private static final Logger LOG = Logger.getLogger(ProductExportController.class);

  @RequestMapping(value = "/csv", method = RequestMethod.GET, produces = "text/csv")
  public String exportCsvFile(@RequestParam String content, final HttpServletResponse response) throws IOException {
    String fileName = String.format("product_%d.csv", System.currentTimeMillis());
    response.setHeader("Content-Disposition", String.format("attachment;filename=%s", fileName));

    try (final StringWriter writer = new StringWriter()) {
      try {
        final List<String> headers = new ArrayList<>();
        headers.add(getMessageSource().getMessage("basket.export.cart.item.stylecode", null, getI18nService().getCurrentLocale()));
        headers.add(getMessageSource().getMessage("basket.export.cart.item.materialKey", null, getI18nService().getCurrentLocale()));
        headers.add(getMessageSource().getMessage("basket.export.cart.item.sku", null, getI18nService().getCurrentLocale()));
        headers.add(getMessageSource().getMessage("basket.export.cart.item.name", null, getI18nService().getCurrentLocale()));
        headers
          .add(getMessageSource().getMessage("basket.export.cart.item.upc", null, getI18nService().getCurrentLocale()));
        headers.add(
          getMessageSource().getMessage("basket.export.cart.item.quantity", null, getI18nService().getCurrentLocale()));
        headers
          .add(getMessageSource().getMessage("basket.export.cart.item.price", null, getI18nService().getCurrentLocale()));

        List<MiniOrderEntry> collectedProducts =  mapper.readValue(content, TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, MiniOrderEntry.class));

        csvFacade.generateCsvFromJson(headers, true, collectedProducts, writer);


        StreamUtils.copy(writer.toString(), StandardCharsets.UTF_8, response.getOutputStream());
      }
      catch (final IOException e)
      {
        LOG.error(e.getMessage(), e);
      }

    }

    return null;
  }

  @RequestMapping(value = "/images", method = RequestMethod.GET, produces = "text/csv")
  public String exportImagesArchive(@RequestParam String[] content, final HttpServletResponse response) throws IOException {

    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);

    pentlandExportFacade.exportImagesForProductList(zipOutputStream, new HashSet<>(Arrays.asList(content)), true);

    zipOutputStream.finish();

    String fileName = String.format("images_%d.zip", System.currentTimeMillis());
    response.setHeader("Content-Disposition", String.format("attachment;filename=%s", fileName));
    response.setContentType("application/zip");

    try{
      response.getOutputStream().write(byteArrayOutputStream.toByteArray());
      response.flushBuffer();
    }catch (IOException e){
      LOG.error(e.getMessage(), e);
    } finally{
      byteArrayOutputStream.close();
    }

    return null;
  }
}
