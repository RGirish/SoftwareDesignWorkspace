/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asu.girish.raman.hateoas.appeals.graman1.netbeans8_1.representations;

import asu.girish.raman.hateoas.appeals.graman1.netbeans8_1.events.CreateAppealEvent;
import asu.girish.raman.hateoas.appeals.graman1.netbeans8_1.models.Appeal;
import asu.girish.raman.hateoas.appeals.graman1.netbeans8_1.models.AppealStatus;
import static asu.girish.raman.hateoas.appeals.graman1.netbeans8_1.representations.AllUris.ENTRY_POINT;
import java.util.Arrays;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Girish
 */
public class AppealRepresentation {

    private final int appealID, studentID, gradingItemID;
    private final String appealMessage;
    private final List<String> images;
    private final List<String> appealItems;
    private final AppealStatus appealStatus;
    private final List<String> nextStateUris;

    public AppealRepresentation(Appeal appeal, String... appealsUris) {
        this.appealID = appeal.getAppealID();
        this.studentID = appeal.getStudentID();
        this.gradingItemID = appeal.getGradingItemID();
        this.appealMessage = appeal.getAppealMessage();
        this.images = appeal.getImages();
        this.appealItems = appeal.getAppealItems();
        this.appealStatus = appeal.getAppealStatus();
        this.nextStateUris = Arrays.asList(appealsUris);
    }

    public static AppealRepresentation createAppealJsonRequestToObject(String jsonString) throws JSONException {
        JSONObject root = new JSONObject(jsonString);
        Appeal appeal = new Appeal(root.getInt("studentID"), root.getInt("gradingItemID"), root.getString("appealMessage"));
        return new CreateAppealEvent().createAppeal(appeal);
    }

    public String getLocationURI() {
        return ENTRY_POINT + "/" + appealID;
    }

}