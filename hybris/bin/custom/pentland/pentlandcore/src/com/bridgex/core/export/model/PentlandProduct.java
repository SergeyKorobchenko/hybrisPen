package com.bridgex.core.export.model;

import de.hybris.platform.acceleratorservices.dataexport.generic.output.csv.DelimitedFile;
import de.hybris.platform.acceleratorservices.dataexport.generic.output.csv.DelimitedFileMethod;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 10/24/2017.
 */
@DelimitedFile(delimiter = ";")
public class PentlandProduct {

  //product attributes
  private String code;
  private String name;
  private String description;
  private String features;
  private Integer packSize;
  private String videoURL;
  private String externalURL;
  private String sizeChartImage;
  private String categories;

  //classification attributes
  private String teamwearType;
  private String trainingwearType;
  private String sport;
  private String accessoryType;
  private String useageOccasion;
  private String surface;
  private String trainingMethod;

  public String getCode() {
    return code;
  }

  @DelimitedFileMethod(position = 1)
  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  @DelimitedFileMethod(position = 2)
  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  @DelimitedFileMethod(position = 3)
  public void setDescription(String description) {
    this.description = description;
  }

  public String getFeatures() {
    return features;
  }

  @DelimitedFileMethod(position = 4)
  public void setFeatures(String features) {
    this.features = features;
  }

  public Integer getPackSize() {
    return packSize;
  }

  @DelimitedFileMethod(position = 5)
  public void setPackSize(Integer packSize) {
    this.packSize = packSize;
  }

  public String getVideoURL() {
    return videoURL;
  }

  @DelimitedFileMethod(position = 6)
  public void setVideoURL(String videoURL) {
    this.videoURL = videoURL;
  }

  public String getExternalURL() {
    return externalURL;
  }

  @DelimitedFileMethod(position = 7)
  public void setExternalURL(String externalURL) {
    this.externalURL = externalURL;
  }

  public String getSizeChartImage() {
    return sizeChartImage;
  }

  @DelimitedFileMethod(position = 8)
  public void setSizeChartImage(String sizeChartImage) {
    this.sizeChartImage = sizeChartImage;
  }

  public String getCategories() {
    return categories;
  }

  @DelimitedFileMethod(position = 9)
  public void setCategories(String categories) {
    this.categories = categories;
  }

  public String getTeamwearType() {
    return teamwearType;
  }

  @DelimitedFileMethod(position = 10)
  public void setTeamwearType(String teamwearType) {
    this.teamwearType = teamwearType;
  }

  public String getTrainingwearType() {
    return trainingwearType;
  }

  @DelimitedFileMethod(position = 11)
  public void setTrainingwearType(String trainingwearType) {
    this.trainingwearType = trainingwearType;
  }

  public String getSport() {
    return sport;
  }

  @DelimitedFileMethod(position = 12)
  public void setSport(String sport) {
    this.sport = sport;
  }

  public String getAccessoryType() {
    return accessoryType;
  }

  @DelimitedFileMethod(position = 13)
  public void setAccessoryType(String accessoryType) {
    this.accessoryType = accessoryType;
  }

  public String getUseageOccasion() {
    return useageOccasion;
  }

  @DelimitedFileMethod(position = 14)
  public void setUseageOccasion(String useageOccasion) {
    this.useageOccasion = useageOccasion;
  }

  public String getSurface() {
    return surface;
  }

  @DelimitedFileMethod(position = 15)
  public void setSurface(String surface) {
    this.surface = surface;
  }

  public String getTrainingMethod() {
    return trainingMethod;
  }

  @DelimitedFileMethod(position = 16)
  public void setTrainingMethod(String trainingMethod) {
    this.trainingMethod = trainingMethod;
  }

}
