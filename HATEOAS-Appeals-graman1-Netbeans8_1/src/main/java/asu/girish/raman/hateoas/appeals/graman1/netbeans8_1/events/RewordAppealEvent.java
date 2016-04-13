/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asu.girish.raman.hateoas.appeals.graman1.netbeans8_1.events;

import asu.girish.raman.hateoas.appeals.graman1.netbeans8_1.exceptions.AppealAlreadyAbandonedException;
import asu.girish.raman.hateoas.appeals.graman1.netbeans8_1.exceptions.AppealHasNotStartedProcessingYetException;
import asu.girish.raman.hateoas.appeals.graman1.netbeans8_1.exceptions.AppealProcessingAlreadyStartedException;
import asu.girish.raman.hateoas.appeals.graman1.netbeans8_1.exceptions.InvalidAppealIDException;
import asu.girish.raman.hateoas.appeals.graman1.netbeans8_1.models.Appeal;
import static asu.girish.raman.hateoas.appeals.graman1.netbeans8_1.repositories.AppealRepository.APPEAL_REPOSITORY;
import asu.girish.raman.hateoas.appeals.graman1.netbeans8_1.representations.AppealRepresentation;
import static asu.girish.raman.hateoas.appeals.graman1.netbeans8_1.representations.AllUris.*;

/**
 *
 * @author Girish
 */
public class RewordAppealEvent {

    public RewordAppealEvent() {
    }

    public AppealRepresentation rewordAppeal(int id, String appealMessage) {
        if (APPEAL_REPOSITORY.containsAppealID(id)) {
            Appeal appeal = APPEAL_REPOSITORY.getAppealByID(id);
            switch (appeal.getAppealStatus()) {
                case ABANDONED:
                    throw new AppealAlreadyAbandonedException();
                case PROCESSING:
                    throw new AppealProcessingAlreadyStartedException();
                case FINISHED:
                    throw new AppealHasNotStartedProcessingYetException();
                default:
                    break;
            }

            appeal.setAppealMessage(appealMessage);
            APPEAL_REPOSITORY.resetAppealByID(id, appeal);

            String addAppealItemUri = ADD_APPEAL_ITEM_URI + "/" + id;
            String rewordAppealUri = REWORD_URI + "/" + id;
            String addImageUri = ADD_IMAGE_URI + "/" + id;
            String reviewAppealUri = REVIEW_URI + "/" + id;
            String abandonAppealUri = ABANDON_URI + "/" + id;
            return new AppealRepresentation(appeal, addAppealItemUri, rewordAppealUri, addImageUri, reviewAppealUri, abandonAppealUri);
        } else {
            throw new InvalidAppealIDException();
        }
    }
}