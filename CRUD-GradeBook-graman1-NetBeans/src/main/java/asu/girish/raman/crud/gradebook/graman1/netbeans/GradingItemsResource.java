package asu.girish.raman.crud.gradebook.graman1.netbeans;

import static asu.girish.raman.crud.gradebook.graman1.netbeans.AppealsResource.appeals;
import static asu.girish.raman.crud.gradebook.graman1.netbeans.StudentProfilesResource.students;
import asu.girish.raman.crud.gradebook.models.Appeal;
import asu.girish.raman.crud.gradebook.models.GradingItem;
import asu.girish.raman.crud.gradebook.models.Student;
import static java.net.HttpURLConnection.*;
import java.net.URI;
import java.util.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.json.*;

@Path("Gradebook/GradingItems")
public class GradingItemsResource {

    @Context
    private UriInfo context;
    static List<GradingItem> gradingItems = null;
    static int GRADING_ITEM_ID = 1;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createGradingItem(String jsonRequest) {
        String jsonResponse;
        try {
            JSONObject root = new JSONObject(jsonRequest);
            if (!root.getString("requestType").equals("createGradingItem")) {
                throw new JSONException("Bad Request");
            }
            String type = root.getString("type");
            String name = root.getString("name");
            double percentage = root.getDouble("percentage");
            GradingItem gradingItem = new GradingItem(GRADING_ITEM_ID, type, name, percentage);
            if (gradingItems == null) {
                gradingItems = new ArrayList<>();
            } else {
                double totalWeightageSofar = 0;
                for (GradingItem gi : gradingItems) {
                    if (gi.getName().equals(name)) {
                        jsonResponse = "{\n"
                                + "\"responseType\" : \"failure\",\n"
                                + "\"reason\" : \"GradeItem with this name already exists!\",\n"
                                + "\"request\" : " + jsonRequest + "\n"
                                + "}";
                        return Response.status(HTTP_CONFLICT).entity(jsonResponse).build();
                    }
                    totalWeightageSofar += gi.getPercentage();
                }
                if (totalWeightageSofar + percentage > 100) {
                    jsonResponse = "{\n"
                            + "\"responseType\":\"failure\",\n"
                            + "\"reason\":\"Total weightage exceeds 100%\",\n"
                            + "\"request\":" + jsonRequest + "\n"
                            + "}";
                    System.out.println(jsonResponse);
                    return Response.status(HTTP_BAD_REQUEST).entity(jsonResponse).build();
                }
            }
            gradingItems.add(gradingItem);
            jsonResponse = "{\n"
                    + "\"responseType\":\"success\",\n"
                    + "\"id\":" + GRADING_ITEM_ID + "\n"
                    + "}";
            GRADING_ITEM_ID++;
            return Response.status(HTTP_CREATED).entity(jsonResponse).location(new URI(context.getAbsolutePath() + "/" + (GRADING_ITEM_ID - 1))).build();
        } catch (JSONException e) {
            jsonResponse = "{\n"
                    + "\"responseType\":\"failure\",\n"
                    + "\"request\":" + jsonRequest + "\n"
                    + "}";
            return Response.status(HTTP_BAD_REQUEST).entity(jsonResponse).build();
        } catch (Exception e) {
            jsonResponse = "{\n"
                    + "\"responseType\":\"failure\",\n"
                    + "\"request\":" + jsonRequest + "\n"
                    + "}";
            return Response.status(HTTP_INTERNAL_ERROR).entity(jsonResponse).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response readAllGradingItems() {
        StringBuilder jsonResponse = new StringBuilder();
        try {
            if (gradingItems == null) {
                jsonResponse.append("{\n"
                        + "\"responseType\" : \"failure\"\n"
                        + "}");
                return Response.status(HTTP_GONE).entity(jsonResponse.toString()).build();
            }

            jsonResponse.append("{\n"
                    + "\"responseType\" : \"success\",\n"
                    + "\"gradingItems\" : [\n");
            for (GradingItem gradingItem : gradingItems) {
                jsonResponse.append("{\n"
                        + "\"id\" : \"" + gradingItem.getId() + "\",\n"
                        + "\"type\" : \"" + gradingItem.getType() + "\",\n"
                        + "\"name\" : \"" + gradingItem.getName() + "\",\n"
                        + "\"percentage\" : " + gradingItem.getPercentage() + "\n"
                        + "},\n");
            }
            if (jsonResponse.toString().endsWith("[\n")) {
                jsonResponse.append("]\n}");
            } else {
                jsonResponse = new StringBuilder(jsonResponse.substring(0, jsonResponse.length() - 2));
                jsonResponse.append("\n]\n}");
            }
            return Response.status(HTTP_OK).entity(jsonResponse.toString()).build();
        } catch (JSONException e) {
            jsonResponse = new StringBuilder("{\n"
                    + "\"responseType\" : \"failure\"\n"
                    + "}");
            return Response.status(HTTP_BAD_REQUEST).entity(jsonResponse.toString()).build();
        } catch (Exception e) {
            jsonResponse = new StringBuilder("{\n"
                    + "\"responseType\" : \"failure\"\n"
                    + "}");
            return Response.status(HTTP_INTERNAL_ERROR).entity(jsonResponse.toString()).build();
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response readGradingItem(@PathParam("id") int id) {
        String jsonResponse;
        try {
            if (gradingItems == null) {
                jsonResponse = "{\n"
                        + "\"responseType\" : \"failure\",\n"
                        + "\"request-id\" : \"" + id + "\"\n"
                        + "}";
                return Response.status(HTTP_GONE).entity(jsonResponse).build();
            }

            for (GradingItem gradingItem : gradingItems) {
                if (gradingItem.getId() == id) {
                    jsonResponse = "{\n"
                            + "\"responseType\" : \"success\",\n"
                            + "\"id\" : \"" + gradingItem.getId() + "\",\n"
                            + "\"type\" : \"" + gradingItem.getType() + "\",\n"
                            + "\"name\" : \"" + gradingItem.getName() + "\",\n"
                            + "\"percentage\" : " + gradingItem.getPercentage() + "\n"
                            + "}";
                    return Response.status(HTTP_OK).entity(jsonResponse).build();
                }
            }
            jsonResponse = "{\n"
                    + "\"responseType\" : \"failure\",\n"
                    + "\"request-id\" : \"" + id + "\"\n"
                    + "}";
            return Response.status(HTTP_NOT_FOUND).entity(jsonResponse).build();
        } catch (JSONException e) {
            jsonResponse = "{\n"
                    + "\"responseType\" : \"failure\",\n"
                    + "\"request-id\" : \"" + id + "\"\n"
                    + "}";
            return Response.status(HTTP_BAD_REQUEST).entity(jsonResponse).build();
        } catch (Exception e) {
            jsonResponse = "{\n"
                    + "\"responseType\" : \"failure\",\n"
                    + "\"request-id\" : \"" + id + "\"\n"
                    + "}";
            return Response.status(HTTP_INTERNAL_ERROR).entity(jsonResponse).build();
        }
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateGradingItem(String jsonRequest) {
        String jsonResponse;
        try {
            JSONObject root = new JSONObject(jsonRequest);
            if (!root.getString("requestType").equals("updateGradingItem")) {
                throw new JSONException("Bad Request");
            }
            int id = root.getInt("id");

            if (gradingItems == null) {
                jsonResponse = "{\n"
                        + "\"responseType\" : \"failure\",\n"
                        + "\"reason\" : \"Resource GONE.\",\n"
                        + "\"request\" : " + jsonRequest + "\n"
                        + "}";
                return Response.status(HTTP_CONFLICT).entity(jsonResponse).build();
            }

            double totalWeightageSofar = 0;
            for (GradingItem gi : gradingItems) {
                if (gi.getName().equals(root.getString("name")) && gi.getId() != id) {
                    jsonResponse = "{\n"
                            + "\"responseType\" : \"failure\",\n"
                            + "\"reason\" : \"Grading Item with this name already exists!\",\n"
                            + "\"request\" : " + jsonRequest + "\n"
                            + "}";
                    return Response.status(HTTP_CONFLICT).entity(jsonResponse).build();
                }
                if (gi.getId() != id) {
                    totalWeightageSofar += gi.getPercentage();
                }
            }
            if (totalWeightageSofar + root.getDouble("percentage") > 100) {
                jsonResponse = "{\n"
                        + "\"responseType\":\"failure\",\n"
                        + "\"reason\":\"Total weightage exceeds 100%\",\n"
                        + "\"request\":" + jsonRequest + "\n"
                        + "}";
                return Response.status(HTTP_CONFLICT).entity(jsonResponse).build();
            }

            for (GradingItem gradingItem : gradingItems) {
                if (gradingItem.getId() == id) {
                    gradingItems.remove(gradingItem);
                    gradingItem.setType(root.getString("type"));
                    gradingItem.setName(root.getString("name"));
                    gradingItem.setPercentage(root.getDouble("percentage"));
                    gradingItems.add(gradingItem);

                    jsonResponse = "{\n"
                            + "\"responseType\" : \"success\",\n"
                            + "\"id\" : " + gradingItem.getId() + ",\n"
                            + "\"type\" : \"" + gradingItem.getType() + "\",\n"
                            + "\"name\" : \"" + gradingItem.getName() + "\",\n"
                            + "\"percentage\" : " + gradingItem.getPercentage() + "\n"
                            + "}";
                    return Response.status(HTTP_OK).entity(jsonResponse).build();
                }
            }
            jsonResponse = "{\n"
                    + "\"responseType\" : \"failure\",\n"
                    + "\"reason\" : \"Invalid Grading Item ID!\",\n"
                    + "\"request\" : " + jsonRequest + "\n"
                    + "}";
            return Response.status(HTTP_CONFLICT).entity(jsonResponse).build();
        } catch (JSONException e) {
            jsonResponse = "{\n"
                    + "\"responseType\" : \"failure\",\n"
                    + "\"request\" : " + jsonRequest + "\n"
                    + "}";
            return Response.status(HTTP_BAD_REQUEST).entity(jsonResponse).build();
        } catch (Exception e) {
            jsonResponse = "{\n"
                    + "\"responseType\" : \"failure\",\n"
                    + "\"request\" : " + jsonRequest + "\n"
                    + "}";
            return Response.status(HTTP_INTERNAL_ERROR).entity(jsonResponse).build();
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAllGradingItems() {
        String jsonResponse;
        try {
            if (gradingItems == null) {
                jsonResponse = "{\n"
                        + "\"responseType\" : \"failure\",\n"
                        + "\"reason\" : \"Resources GONE.\"\n"
                        + "}";
                return Response.status(HTTP_CONFLICT).entity(jsonResponse).build();
            }
            if (gradingItems.isEmpty()) {
                jsonResponse = "{\n"
                        + "\"responseType\" : \"failure\",\n"
                        + "\"reason\" : \"Nothing to Delete.\"\n"
                        + "}";
                return Response.status(HTTP_CONFLICT).entity(jsonResponse).build();
            }
            gradingItems.clear();
            appeals.clear();
            for (Student student : students) {
                student.setPoints(new LinkedHashMap<Integer, Double>());
                student.setFeedbacks(new LinkedHashMap<Integer, String>());
            }
            return Response.status(HTTP_NO_CONTENT).build();
        } catch (JSONException e) {
            jsonResponse = "{\n"
                    + "\"responseType\" : \"failure\"\n"
                    + "}";
            return Response.status(HTTP_BAD_REQUEST).entity(jsonResponse).build();
        } catch (Exception e) {
            jsonResponse = "{\n"
                    + "\"responseType\" : \"failure\"\n"
                    + "}";
            return Response.status(HTTP_INTERNAL_ERROR).entity(jsonResponse).build();
        }
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteGradingItem(@PathParam("id") int id) {
        String jsonResponse;
        try {
            if (gradingItems == null) {
                jsonResponse = "{\n"
                        + "\"responseType\" : \"failure\",\n"
                        + "\"reason\" : \"Resource GONE.\"\n"
                        + "}";
                return Response.status(HTTP_CONFLICT).entity(jsonResponse).build();
            }

            for (GradingItem gradingItem : gradingItems) {
                if (gradingItem.getId() == id) {
                    gradingItems.remove(gradingItem);
                    //Delete grades related to the deleted grading item from student data
                    for (Student student : students) {
                        HashMap<Integer, Double> allPoints = (HashMap<Integer, Double>) student.getPoints();
                        HashMap<Integer, String> allFeedbacks = (HashMap<Integer, String>) student.getFeedbacks();
                        if (allPoints.containsKey(id)) {
                            allPoints.remove(id);
                        }
                        if (allFeedbacks.containsKey(id)) {
                            allFeedbacks.remove(id);
                        }
                    }
                    //Delete appeals related to the deleted grading item from appeals data
                    for (Appeal appeal : appeals) {
                        if (appeal.getGradingItemId() == id) {
                            appeals.remove(appeal);
                        }
                    }
                    return Response.status(HTTP_NO_CONTENT).build();
                }
            }
            jsonResponse = "{\n"
                    + "\"responseType\" : \"failure\",\n"
                    + "\"reason\" : \"Grading Item with given ID doesn't exist.\"\n"
                    + "}";
            return Response.status(HTTP_CONFLICT).entity(jsonResponse).build();
        } catch (JSONException e) {
            jsonResponse = "{\n"
                    + "\"responseType\" : \"failure\",\n"
                    + "\"request-id\" : \"" + id + "\"\n"
                    + "}";
            return Response.status(HTTP_BAD_REQUEST).entity(jsonResponse).build();
        } catch (Exception e) {
            jsonResponse = "{\n"
                    + "\"responseType\" : \"failure\",\n"
                    + "\"request-id\" : \"" + id + "\"\n"
                    + "}";
            return Response.status(HTTP_INTERNAL_ERROR).entity(jsonResponse).build();
        }
    }

    @GET
    @Path("getAllIDs")
    @Produces(MediaType.TEXT_PLAIN)
    public String getAllGradingItemIDs() {
        List<Integer> allIDs = new ArrayList<>();
        if (gradingItems != null) {
            for (GradingItem gradingItem : gradingItems) {
                allIDs.add(gradingItem.getId());
            }
        }
        return allIDs.toString();
    }
}
