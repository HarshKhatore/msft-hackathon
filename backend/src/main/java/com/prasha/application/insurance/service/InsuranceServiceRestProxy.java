package com.prasha.application.insurance.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.prasha.application.exception.service.StreamServiceException;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/*TODO: Use slf4j instead of java logger*/
import java.io.IOException;
import java.util.logging.Logger;

@Path("/insurance")
public class InsuranceServiceRestProxy {
   private static final Logger log = Logger.getLogger(InsuranceServiceRestProxy.class.getName());
   private static final InsuranceService insuranceServiceImpl = InsuranceServiceImpl.getInstance();
   private static ObjectMapper mapper = new ObjectMapper();

   @POST
   @Path("/create")
   @Produces(MediaType.APPLICATION_JSON)
   public Response createInsuranceDetails(String requestString) {
      validateRequestString(requestString);
      String id;
      ObjectNode responseNode = mapper.createObjectNode();
      try {
         JsonNode requestJson;
         log.info(String.format("Request body: %s", requestString));
         requestJson = mapper.readTree(requestString);
         id = insuranceServiceImpl.createInsuranceDetailsForDevice(requestJson);
         responseNode.put("id", id);
      } catch (IOException e) {
         throw new StreamServiceException(Response.Status.INTERNAL_SERVER_ERROR);
      }
      return Response.ok(responseNode.toString()).build();
   }

   @PUT
   @Path("/update/{id}")
   @Produces(MediaType.APPLICATION_JSON)
   public Response updateInsuranceDetails(@PathParam("id") String deviceId, String requestString) {
      validateRequestString(requestString);
      String id;
      ObjectNode responseNode = mapper.createObjectNode();
      try {
         JsonNode requestJson;
         requestJson = mapper.readTree(requestString);
         id = insuranceServiceImpl.createInsuranceDetailsForDevice(requestJson);
         responseNode.put("id", id);
      } catch (IOException e) {
         throw new StreamServiceException(Response.Status.INTERNAL_SERVER_ERROR);
      }
      return Response.ok(responseNode.toString()).build();
   }

   @GET
   @Path("/anomalies-detected/{id}")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getAnomalyDetectedDetails(@PathParam("id") String deviceId) {
      JsonNode responseNode = insuranceServiceImpl.getAnomalyDetails(deviceId);
      return Response.ok(responseNode.toString()).build();
   }

   @GET
   @Path("/get-insurance-price/{id}")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getInsurancePrice(@PathParam("id") String deviceId) {
      JsonNode responseNode = insuranceServiceImpl.getInsurancePrice(deviceId);
      return Response.ok(responseNode.toString()).build();
   }

   @GET
   @Path("/get-insurance-price")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getAllInsurancePrice() {
      ArrayNode result = insuranceServiceImpl.getAllInsurancePrice();
      return Response.ok(result.toString()).build();
   }

   @GET
   @Path("/get-details/{id}")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getInsuranceDetailsForDevice(@PathParam("id") String deviceId) {
      validateRequestString(deviceId);
      log.info(String.format("DeviceId is: %s", deviceId));
      return Response.ok(insuranceServiceImpl.getInsuranceDetailsForDevice(deviceId).toString()).build();
   }

   @GET
   @Path("/get-details")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getAllInsuranceDetails() {
      ArrayNode result = insuranceServiceImpl.getInsuranceDetailsForAllDevice();
      return Response.ok(result.toString()).build();
   }

   private void validateRequestString(final String requestString) {
      if (requestString == null || requestString.isEmpty()) {
         throw new StreamServiceException(Response.Status.BAD_REQUEST);
      }
   }
}
