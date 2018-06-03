package edu.mcw.GeneralSurgery.UI.Content;

import edu.mcw.GeneralSurgery.models.Image;

/**
 * Created by arham on 3/19/18.
 */

interface OnImageInteractionListener {
    void onImageFragmentInteraction(Image image);

    void onImageFeedbackFragmentInteraction(Image image);
}
