package com.prasha.application.insurance.service;

import static com.prasha.application.CommonUtils.ANOMALIES_DETAILS_COLLECTION;
import static com.prasha.application.CommonUtils.DEVICE_DETAILS_COLLECTION;
import static com.prasha.application.CommonUtils.DEVICE_ID;
import static com.prasha.application.CommonUtils.DEVICE_INSURANCE_DETAILS_COLLECTION;
import static com.prasha.application.CommonUtils.ID;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.prasha.application.authenticator.service.AuthenticatorServiceImpl;
import com.prasha.application.database.service.DatabaseServiceImpl;

public class InsuranceServiceImpl implements InsuranceService {
   private static final Logger log = Logger.getLogger(AuthenticatorServiceImpl.class.getName());
   private static final InsuranceService INSURANCE_SERVICE = new InsuranceServiceImpl();
   private static final DatabaseServiceImpl dbService = DatabaseServiceImpl.getInstance();
   ObjectMapper mapper = new ObjectMapper();
   Map<String, Long> devicePriceMap = new HashMap<String, Long>() {{
      put("HP ProBook 440 G2", 60000L);
      put("SM-G935F", 30000L);
      put("BMW-X7", 3800000L);
      put("BMW-X1", 1800000L);
      put("Asus ZenBook", 38000L);
      put("SM-GN46F", 48000L);
   }};

   public static InsuranceService getInstance() {
      return INSURANCE_SERVICE;
   }

   public String createInsuranceDetailsForDevice(JsonNode request) {
      ObjectNode requestNode = (ObjectNode) request;
      String id = UUID.randomUUID().toString();
      requestNode.put(ID, id);
      dbService.insertOne(DEVICE_DETAILS_COLLECTION, requestNode);
      // Insert for device insurance after calculation
      dbService.insertOne(DEVICE_INSURANCE_DETAILS_COLLECTION, calculateInsurance(request));
      return id;
   }

   public JsonNode getInsuranceDetailsForDevice(String deviceId) {
      ArrayNode array = mapper.createArrayNode();
      ObjectNode queryNode = mapper.createObjectNode();
      array.add(mapper.createObjectNode().put(ID, deviceId));

      JsonNode result = DatabaseServiceImpl.getInstance().findOne(DEVICE_DETAILS_COLLECTION, queryNode);
      predictedAnomalies(result);
      return result;
   }

   public ArrayNode getInsuranceDetailsForAllDevice() {
      return dbService.findAll(DEVICE_DETAILS_COLLECTION);
   }

   @Override
   public JsonNode getAnomalyDetails(String deviceId) {
      ArrayNode array = mapper.createArrayNode();
      ObjectNode queryNode = mapper.createObjectNode();
      array.add(mapper.createObjectNode().put(DEVICE_ID, deviceId));

      JsonNode result = DatabaseServiceImpl.getInstance().findOne(DEVICE_DETAILS_COLLECTION, queryNode);
      predictedAnomalies(result);
      return result;
   }

   @Override
   public JsonNode getInsurancePrice(String deviceId) {
      ArrayNode array = mapper.createArrayNode();
      ObjectNode queryNode = mapper.createObjectNode();
      array.add(mapper.createObjectNode().put(ID, deviceId));

      JsonNode result = DatabaseServiceImpl.getInstance().findOne(DEVICE_INSURANCE_DETAILS_COLLECTION, queryNode);
      return result;
   }

   @Override
   public ArrayNode getAllInsurancePrice() {
      return dbService.findAll(DEVICE_INSURANCE_DETAILS_COLLECTION);
   }

   private JsonNode calculateInsurance(JsonNode result) {
      ObjectNode resultNode = (ObjectNode) result;
      log.info(String.format("Result of calcInsurance: %s", result.toString()));
      String modelName = "";
      if (resultNode.has("modelName")) {
         modelName = resultNode.get("modelName").textValue();
      }
      String deviceType = resultNode.get("deviceType").textValue();
      Long devicePrice = 0L;
      if (this.devicePriceMap.containsKey(modelName)) {
        devicePrice = this.devicePriceMap.get(modelName);
      }

      double premiumCost = 0.0;
      if (deviceType.equals("ELECTRONICS")) {
         premiumCost = (devicePrice * 0.5) * getAgePremium(resultNode.get("activationDate").asLong());
      } else if (deviceType.equals("AUTOMOBILE")) {
         premiumCost = (devicePrice * 0.5) * getAgePremium(resultNode.get("activationDate").asLong());
      }
      resultNode.put(ID, result.get(ID).textValue());
      resultNode.put("previousPrice", devicePrice);
      resultNode.put("premiumPrice", premiumCost);
      return resultNode;
   }

   private JsonNode predictedAnomalies(JsonNode result) {
      ObjectNode resultNode = (ObjectNode) result;
      JsonNode anomalyResult = mapper.createObjectNode();
      String deviceType = resultNode.get("deviceType").toString();
      if (deviceType.equals("ELECTRONICS")) {
         anomalyResult = predictOnBasisOfElectronics(result);
      } else if (deviceType.equals("AUTOMOBILE")) {
         anomalyResult = predictOnBasisOfAutomobiles(result);
      }

      dbService.insertOne(ANOMALIES_DETAILS_COLLECTION, anomalyResult);
      return anomalyResult;
   }

   private double getAgePremium(long activationDate) {
      double agePremium = 1.0;
      // 1 years
      if (activationDate < 31556926) {
         agePremium = 1.20;
      } else if (activationDate > 31556926 && activationDate <= 157784630) {
         agePremium = 1.15;
      } else if (activationDate > 157784630 && activationDate <= 315569260) {
         agePremium = 1.05;
      }

      return agePremium;
   }

   private JsonNode predictOnBasisOfElectronics(JsonNode result) {
      ObjectNode anomalyResult = mapper.createObjectNode();
      int batteryCapacity = result.get("batteryMaxCapacity").asInt();
      if (batteryCapacity < 50) {
         // Notify_User
      }

      anomalyResult.put("userNotifiedTimes", 2);
      anomalyResult.put("savedAmountForInsuranceCompany", 100000);
      return anomalyResult;
   }

   private JsonNode predictOnBasisOfAutomobiles(JsonNode result) {
      return null;
   }
}

