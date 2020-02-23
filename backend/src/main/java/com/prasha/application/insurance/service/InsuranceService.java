package com.prasha.application.insurance.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

public interface InsuranceService {
   String createInsuranceDetailsForDevice(JsonNode request);
   JsonNode getInsuranceDetailsForDevice(String deviceId);
   ArrayNode getInsuranceDetailsForAllDevice();
   JsonNode getAnomalyDetails(String deviceId);
   JsonNode getInsurancePrice(String deviceId);
   ArrayNode getAllInsurancePrice();
}
